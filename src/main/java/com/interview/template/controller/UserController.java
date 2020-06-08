package com.interview.template.controller;

import java.text.MessageFormat;
import java.util.UUID;

import com.interview.template.dto.UserRequest;
import com.interview.template.dto.UserRequestMapper;
import com.interview.template.exceptions.InvalidUserNameException;
import com.interview.template.exceptions.UserNotFoundException;
import com.interview.template.model.UserEntity;
import com.interview.template.projections.UserProjection;
import com.interview.template.service.UserService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(UserController.BASE_URL)
@AllArgsConstructor
@Slf4j
class UserController {

    static final String BASE_URL = "/api/v1/users";

    private static final String USER_NOT_FOUND = "User id {0} not found";

    private static final String RESERVED_USERNAME = "Username {0} is not allowed";

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectionFactory factory;

    @Autowired
    private UserRequestMapper userRequestMapper;

    @Autowired
    public UserController(ProjectionFactory factory) {
        this.factory = factory;
    }

    @GetMapping
    public ResponseEntity getAllUsers() {

        return ResponseEntity.ok(userService.getAllUsers()
                .stream()
                .map(user -> factory.createProjection(UserProjection.class, user)));
    }

    @GetMapping("/username/{search}")
    public ResponseEntity getAllUsersByUserName(@PathVariable String search) {

        return ResponseEntity.ok(userService.getAllUsersByUserName(search)
                .stream()
                .map(user -> factory.createProjection(UserProjection.class, user)));
    }

    @GetMapping("/{userId}")
    public ResponseEntity getUser(@PathVariable Long userId) throws UserNotFoundException {

        return userService.getUser(userId)
                .map(user -> ResponseEntity.ok(factory.createProjection(UserProjection.class, user)))
                .orElseThrow(() -> new UserNotFoundException(
                        MessageFormat.format(USER_NOT_FOUND, userId.toString())));
    }

    @PostMapping
    public ResponseEntity createUser(@RequestBody UserRequest userRequest) throws InvalidUserNameException {

        if (userService.validateUserName(userRequest.getUsername())) {
            throw new InvalidUserNameException(MessageFormat.format(RESERVED_USERNAME, userRequest.getUsername()));
        }
        UserEntity user = userRequestMapper.map(userRequest);
        log.info("userRequest {}", userRequest);
        UserEntity e = userService.createUser(user);
        return new ResponseEntity(factory.createProjection(UserProjection.class, e), HttpStatus.CREATED);
    }

    @PutMapping("/{userId}")
    public ResponseEntity updateUser(@PathVariable Long userId, @RequestBody UserRequest userRequest)
            throws UserNotFoundException, InvalidUserNameException {

        if (userService.validateUserName(userRequest.getUsername())) {
            throw new InvalidUserNameException(MessageFormat.format(RESERVED_USERNAME, userRequest.getUsername()));
        }
        return userService.getUser(userId)
                .map(user -> {
                    userRequestMapper.merge(userRequest, user);
                    userService.updateUser(user);
                    return ResponseEntity.ok(factory.createProjection(UserProjection.class, user));
                })
                .orElseThrow(() -> new UserNotFoundException(
                        MessageFormat.format(USER_NOT_FOUND, userId.toString())));
    }

    @DeleteMapping("/{userId}")

    public ResponseEntity deleteUser(@PathVariable Long userId) throws UserNotFoundException {

        String deletedToken = UUID.randomUUID().toString().replace("-", "");
        return userService.getUser(userId)
                .map(user -> {
                    user.setDeleted(true);
                    user.setActive(false);
                    user.setDeletedToken(deletedToken);
                    userService.updateUser(user);
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                })
                .orElseThrow(() -> new UserNotFoundException(
                        MessageFormat.format(USER_NOT_FOUND, userId.toString())));

    }
}
