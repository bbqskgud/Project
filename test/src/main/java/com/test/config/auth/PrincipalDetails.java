package com.test.config.auth;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.test.model.User;

//디테일 클래스 이제 그것을 사용할 서비스만들기 
public class PrincipalDetails implements UserDetails {

	private User user;

	public PrincipalDetails(User user) {
		this.user = user;
	}
	// 생성자까지만들고 밑에는 자동완성 그리고 수정

	@Override // 권한
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collect = new ArrayList<>();
		// 우리가 만든 컬렉트

		// 그곳에 값을 넣음
		collect.add(new GrantedAuthority() {

			@Override
			public String getAuthority() {

				return user.getRoll();
			}
		});

		return collect;// 값넣은 콜렉트를 리턴시킴
	}

	@Override // 비밀번호 받는겟 그래서 리턴을 변경
	public String getPassword() {
		// TODO Auto-generated method stub
		return user.getPassword();
	}

	@Override // 유저이름을 받는 겟 그래서 리턴변경
	public String getUsername() {
		// TODO Auto-generated method stub
		return user.getUsername();
	}

	// 로그인 제어들 true로변경
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
}
