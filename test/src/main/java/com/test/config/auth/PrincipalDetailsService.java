package com.test.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.test.model.User;
import com.test.repository.UserRepository;

//디테일을 활용할 서비스
@Service
public class PrincipalDetailsService implements UserDetailsService {

	// 디비작업을 해야하니 레포지토리 사용해야함
	@Autowired
	UserRepository userRepository;

	// 자동완성
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = userRepository.findByUsername(username);
		// 유저의 객체를 디비로부터 받아옴

		// 바로리턴해도되지만 널이아닐경우 리턴한다는 조건을 줌
		if (user != null) {
			return new PrincipalDetails(user);
		}
		return null;
	}
}
