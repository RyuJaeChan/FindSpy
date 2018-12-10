package com.project.chat.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;

public class AuthUserService implements UserDetailsService {
	private UserRepository userRepository;

	public AuthUserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUserId(username);
		
		System.out.println("username : " + username);
		System.out.println("user : " + user);
		
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		AuthUser a = new AuthUser(user.getUserId(), user.getPassword(), authorities);
		System.out.println("A : " + a);
		return a;
	}

	
	
}
