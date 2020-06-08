package com.interview.template.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

import com.interview.template.dao.UserDao;
import com.interview.template.dto.UserRequest;
import com.interview.template.dto.UserRequestMapper;

import com.interview.template.exceptions.UserNotFoundException;
import com.interview.template.model.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Arrays;

@ExtendWith(SpringExtension.class)
class UserServiceTest {

	@Mock
	private UserDao userDao;

	@Mock
	private UserService userService;

	@Mock
	private UserRequestMapper userRequestMapper;

	List<String> reservedUsername = new ArrayList<>(Arrays.asList("admin","administrator"));

	@BeforeEach
	void beforeEach() {
		userService = new UserService(userDao, reservedUsername);
	}

	@Test
	void shouldFindUser() throws UserNotFoundException {

		UserRequest request = UserRequest.builder()
				.id(1L)
				.email("john@gmail.com")
				.username("john")
				.password("pass")
				.build();
		UserEntity user = userRequestMapper.map(request);

		doReturn(user).when(userDao).findOrDie(1L);
	 	assertEquals(Optional.ofNullable(user), userService.getUser(1L));
	}

	@Test
	void shouldPreventAddingUser() {

		UserRequest request_with_user_name_admin = UserRequest.builder()
				.id(1L)
				.email("john@gmail.com")
				.username("admin")
				.password("pass")
				.build();
		UserRequest request_with_user_name_john = UserRequest.builder()
				.id(1L)
				.email("john@gmail.com")
				.username("john")
				.password("pass")
				.build();

		assertEquals(userService.validateUserName(request_with_user_name_admin.getUsername()), true);

		assertEquals(userService.validateUserName(request_with_user_name_john.getUsername()), false);

	}
}
