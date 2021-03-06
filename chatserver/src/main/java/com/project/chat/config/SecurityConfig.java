package com.project.chat.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.project.chat.user.AuthUserService;
import com.project.chat.user.UserRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.httpBasic()
		.and()
		.authorizeRequests()
			//.antMatchers("/users/{userId}").access("@authenticationCheckHandler.checkUserId(authentication,#userId)")
			//.antMatchers("/admin/db/**").access("hasRole('ADMIN_MASTER') or hasRole('ADMIN') and hasRole('DBA')")
			//.antMatchers("/register/**").hasRole("ANONYMOUS")
			//.antMatchers("/game").hasRole("USER")
			//.antMatchers("/main").hasRole("USER")
			//.anyRequest().authenticated()
			.antMatchers("/css/**", "/js/**", "/img/**").permitAll()
			.antMatchers("/").permitAll()
			.anyRequest().authenticated()
		.and()
		.formLogin()
			.loginPage("/loginform")
			.loginProcessingUrl("/auth")
			.usernameParameter("id")
			.passwordParameter("password")
			.defaultSuccessUrl("/")
			.failureUrl("/loginform")
			//.successHandler(successHandler())
			//.failureHandler(failureHandler())
			.permitAll()
			.and()
		.logout()
			.logoutUrl("/logout")
			.logoutSuccessUrl("/")
			.invalidateHttpSession(true)
			.deleteCookies("JSESSIONID")
			.and()
		.csrf().disable();
	}
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
		.userDetailsService(new AuthUserService(userRepository));		
	}
	
	

}
