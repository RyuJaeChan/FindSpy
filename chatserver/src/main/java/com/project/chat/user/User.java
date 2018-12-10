package com.project.chat.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.project.chat.util.Role;

import lombok.Data;

@Entity
@Table(name = "user")
@Data
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "user_id", length = 45)
	private String userId;
	
	@Column(name = "password", length = 45)
	private String password;
	
	@Column(name = "name", length = 45)
	private String name;
	
	
	@Column(name = "authority", length = 45)
	private String role;
}
