package com.codestates.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class SecurityConfigurationV1 {
//    @Bean
//    public UserDetailsManager userDetailsManager() {
//
//        // 아래 인메모리 내용
//        UserDetails user =
//                User.withDefaultPasswordEncoder()
//                        .username("january@gmail.com").password("1234").roles("USER").build();
//
//
//        UserDetails admin = User.withDefaultPasswordEncoder()
//                .username("march@gmail.com").password("1234").roles("ADMIN").build();
//        return new InMemoryUserDetailsManager(user, admin); // User에 대해 관리하는 UserDetailsManaer의 구현체로 InMemory~ 구현체를 반환
//
//
//
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.headers().frameOptions().sameOrigin() // frame, iframe, object와 같은 태그로 인해 동일 출처로부터 들어오는 request만 페이지 렌더링을 허용
                .and()
                .csrf()
                .disable()//csrf 공격 방어 비활성
                .formLogin()//폼 로그인 인증 기능 활성화
                .loginPage("/auths/login-form") //로그인 페이지 설정, 기본 주소는 /login
                .loginProcessingUrl("/process_login") //로그인 Form Action Url을 지정할 수 있다(현재는 단지 URL만을 위한 용도이고, 시큐리티 내부적으로 알아서 검증한다. 검증 부분으 본인이 만들수도 있음), 기본 주소는 /login
                .failureUrl("/auths/login-form?error") //로그인 실패 후 자동으로 이동할 페이지
                .and() //체인 연결 메소드
                .logout()// logout 설정을 위한 LogoutConfigurer 리턴 받아서 세팅값들을 설정받기 위함. (빌더 패턴)
                .logoutUrl("/logout") //logout수행하는 url 설정
                .logoutSuccessUrl("/")//logout 성공시 리다이렉트 url
                .and()
                .exceptionHandling() //예외 처리 기능 활성화
                .accessDeniedPage("/auths/access-denied") //이상하게 설정 해도 문제없음. 존재 이유 모르겠음?, 그냥 접근 불가 패스에 접근했을 경우에 URL 설정인듯.
                .and()
                //아래의 antMatchers 부분은 순서가 중요
                .authorizeHttpRequests(authorize-> authorize //request에 Path별 접근을 람다로 권한 설정
                        .antMatchers("/orders/**").hasRole("ADMIN")
                        .antMatchers("/members/my-page").hasRole("USER")
                        .antMatchers("/**").permitAll());


//                .anyRequest() //어떠한 요청이든 이란 뜻
//                .permitAll(); // loginPage에 대한 접근은 인증 없이 모두 접근 가능. 즉, 로그인을 하기 위한 로그인이 필요하지 않다.
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        //DelegatingPasswordEncoder를 먼저 생성하는데, 이 DelegatingPasswordEncoder가 실질적으로 PasswordEncoder 구현 객체를 생성

        String idForEncode = "bcrypt";
        Map encoders = new HashMap();

        encoders.put(idForEncode, new BCryptPasswordEncoder());
        encoders.put("noop", NoOpPasswordEncoder.getInstance());
        encoders.put("sha256", new StandardPasswordEncoder());

//        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return new DelegatingPasswordEncoder(idForEncode, encoders);
    }
}
