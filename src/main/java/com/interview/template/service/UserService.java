package com.interview.template.service;

import com.interview.template.dao.UserDao;
import com.interview.template.exceptions.UserNotFoundException;
import com.interview.template.model.UserEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class UserService {

    private final UserDao userDao;

    @Value("#{'${list.of.reserved.username}'.split(',')}")
    private List<String> reservedUsername;

    public List<UserEntity> getAllUsers() {
        return userDao.findAll();
    }

    public Optional<UserEntity> getUser(long id) {
        return userDao.find(id);
    }

    public void checkUserExists(long id) throws UserNotFoundException {
        userDao.checkExists(id);
    }

    public UserEntity createUser(UserEntity user) {
        return userDao.create(user);
    }

    public UserEntity updateUser(UserEntity user) {
        return userDao.save(user);
    }

    public boolean validateUserName(String userName) {
        return reservedUsername.contains(userName);
    }

}
