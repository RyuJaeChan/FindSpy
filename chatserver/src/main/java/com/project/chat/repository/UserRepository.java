package com.project.chat.repository;

import org.springframework.stereotype.Repository;
import com.project.chat.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
}
