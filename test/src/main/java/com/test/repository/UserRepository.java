package com.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.test.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, String>{ 

	//사용할 model을 리턴값으로 그안에있는 요소로 찾는다.
	 public User findByUsername(String username);//우리가 만들어쓰는 메소드 규칙

	
}
