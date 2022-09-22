package com.codestates.auth;


import com.codestates.member.Member;
import com.codestates.member.MemberService;
import com.codestates.utils.HelloAuthorityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;


@RequiredArgsConstructor
@Component
public class HelloUserAuthenticationProvider implements AuthenticationProvider {

    private final MemberService memberService;
    private final HelloAuthorityUtils helloAuthorityUtils;
    private final PasswordEncoder passwordEncoder;


    //인증하는 부분, 즉 해당 유저가 맞는지 검증하는 부분이라고 할 수 있다.
    //매개변수 Authentication으로 들어오는 데이터는 로그인시 입력한 ID, Password이다.
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken) authentication;

        String username = authToken.getName();

        Optional.ofNullable(username).orElseThrow(()->
                new UsernameNotFoundException("Invalid User name or User Password"));

        Member member = memberService.findMember(username);

        String password = member.getPassword();

        verifyCredentials(authToken.getCredentials(), password);

        Collection<? extends GrantedAuthority> authorities = helloAuthorityUtils.createAuthorities(member.getRoles());

        return new UsernamePasswordAuthenticationToken(username, password, authorities); // 인증됬으므로 유저 객체 반환
    }

    private void verifyCredentials(Object credentials, String password) {

        if(!passwordEncoder.matches((String) credentials, password))
        {
            throw new BadCredentialsException("Invalid User name or User password");
        }
    }


    //구현하는 Custom AuthenticationProvider(HelloUserAuthenticationProvider)가
    // Username/Password 방식의 인증을 지원한다는 것을 Spring Security에게 알려주는 역할을 합니다.
    @Override
    public boolean supports(Class<?> authentication) {

        //true일 경우 해당 클래스의 authenticate() 메소드를 호출하여 인증을 진행
        return UsernamePasswordAuthenticationToken.class.equals(authentication);
    }


}
