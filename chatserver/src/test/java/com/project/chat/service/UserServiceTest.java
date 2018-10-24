package com.project.chat.service;


import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import com.project.chat.entity.User;
import com.project.chat.repository.UserRepository;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
	@Mock
	private UserService userService;
	
	@Mock
	private UserRepository ur;
	
	@Test
	public void 유저디비테스트() {
		User user = new User();
		user.setName("test name");
		
		User user2 = new User();
		user2.setName("test name2");
		
		User t = ur.save(user);
		System.out.println(t);
		
		assertThat(user, is(t));
		assertThat(user2, is(userService.insert(user2)));
	}
	
}
