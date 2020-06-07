package com.interview.template.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

import com.interview.template.dao.UserDao;
import com.interview.template.exceptions.UserNotFoundException;
import com.interview.template.model.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ExtendWith(SpringExtension.class)
class UserServiceTest {

	@Mock
	private UserDao userDao;

	private UserService userService;

	List<String> reservedNames = new ArrayList<>(Arrays.asList("admin","administrator"));
	@BeforeEach
	void beforeEach() {
		userService = new UserService(userDao, reservedNames);
	}

	@Test
	void shouldFindUser() throws UserNotFoundException {
		UserEntity user = UserEntity.builder()
				.id(1L)
				.username("john")
				.password("pass")
				.build();
		doReturn(user).when(userDao).findOrDie(1L);

		//assertEquals(user, userService.getUser(1L));
	}
}
