package com.test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration // 설정파일지정 에노테이션

@EnableWebSecurity // 이클래스를 이용해서 시큐리티로 등록하겠다.
//스프링 필터 체인에 등록 =>스프링 시큐리티 설정
//지금 시큐리티가 로그인을 가로체므로 그것을 바꾸는 설정을 하는것 같다.
//csrf라고 비슷한 해킹이 존재(들어가는 곳을 위조하는 것)

@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	// 이것을 사용하여 비밀번호를 암호화시킴
	public BCryptPasswordEncoder encodePwd() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		// 해킹방어 메소드, 로그인을 가로채는것을 막는다.disable()

		http.authorizeRequests().antMatchers("/user/**")// 유저등급은
				// 인증요청에대한 메소드 //String으로 패턴을 넣는것
				.authenticated()// 로그인만 하면 사용할 수 있다는 메소드
				.antMatchers("/admin/**")// 어드민등급은(최고관리자)
				.access("hasRole('ROLE_ADMIN')")
				// 이페이지는 ADMIN계정만 들어올 수 있다.
				.anyRequest().permitAll()
				/*
				 * 그이외의 요청은 아무나가 들어갈 수 있다. = 저3개의 페이지를 제외하고 모든것은 아무나가 들어갈 수 있다.
				 */
				.and().formLogin().loginPage("/loginForm")
				// 로그인할 상황이 있으면 /loginform 페이지에서 로그인해라
				.loginProcessingUrl("/login")
				// 로그인을 처리해줌
				.defaultSuccessUrl("/");
		// 그다음 로그인 성공하면 어디로 갈것인지 해주는메소드
		// 중요함 예를 들어 어드민으로 로그인하면 메인으로가는것이 아니라 어드민 페이지로 가게하는것
		
		http.logout() // 로그아웃 기능 작동함
		  .logoutUrl("/logout") // 로그아웃 처리 URL, default: /logout, 원칙적으로 post 방식만 지원
		  .logoutSuccessUrl("/"); // 로그아웃 성공 후 이동페이지
//		  .deleteCookies("JSESSIONID", "remember-me") // 로그아웃 후 쿠키 삭제
//		  .addLogoutHandler( ...생략... ) // 로그아웃 핸들러
//		  .logoutSuccessHandler( ...생략... ); // 로그아웃 성공 후 핸들러
		
	}
}
/*
 * .antMatchers("/manager/**")//매니저등급은
 * .access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')") //이페이지는 ADMIN계정이나
 * MANAGER계정만 들어올수 있다.
 */
