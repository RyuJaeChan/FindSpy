package com.project.chat.user;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;

@Data
public class AuthUser implements UserDetails {
	private static final long serialVersionUID = -480169969742996463L;
	private String username;
	private String password;
	private List<GrantedAuthority> authorities;
	
	private String name;

	@SuppressWarnings("unchecked")
	public AuthUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		this.username = username;
		this.password = password;
		this.authorities = (List<GrantedAuthority>) authorities;
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
		// TODO Auto-generated method stub
		return true;
	}

}
