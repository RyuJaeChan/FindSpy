package com.project.chat.user;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;

@Data
public class AuthUser implements UserDetails {
	private static final long serialVersionUID = -480169969742996463L;
	private String userId;
	private String userName;
	private String password;
	private List<GrantedAuthority> authorities;
	
	@SuppressWarnings("unchecked")
	public AuthUser(String userId, String password, Collection<? extends GrantedAuthority> authorities, String userName) {
		this.userId = userId;
		this.password = password;
		this.authorities = (List<GrantedAuthority>) authorities;
		this.userName = userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String getUsername() {
		return this.userName;
	}

}
