package com.project.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import lombok.AllArgsConstructor;

@EnableWebSecurity // Spring Security를 활성화하는 애노테이션
@AllArgsConstructor // 이 클래스가 Spring의 설정 클래스임을 나타내는 애노테이션
@Configuration // Lombok 애노테이션으로, 생성자를 자동으로 생성해주는 역할
public class SecurityConfig {
	//패스워드 암호화
//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder(); // BCryptPasswordEncoder는 Spring Security에서 제공하는 비밀번호 암호화를 수행하는 클래스
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
         http
        	.cors().disable()
            .csrf().disable()
            .formLogin().disable() // 기본 로그인 페이지 없애기
            .headers().frameOptions().disable(); // X-Frame-Options 비활성화 (Spring Security가 프레임 내에서 페이지를 표시할 수 있도록 함)
     
         return http.build();
    }

	public String encode(String member_pw) {
		// TODO Auto-generated method stub
		return null;
	}

}
