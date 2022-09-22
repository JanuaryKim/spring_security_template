package com.codestates.member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class InMemoryMemberService implements MemberService {

    private final UserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Member createMember(Member member) {

        //시큐리티가 관리하는 유저 만들기
        //1 권한리스트 만듬
        //2 비밀번호 원본 암호화
        //3 유저의 Principal 과 암호화된 비밀번호, 권한리스트로 UserDetails 객체 생성
        //4 UserDetailsManager의 createUser 메소드로 해당 유저 생성



        List<GrantedAuthority> authorities = createAuthorities(Member.MemberRole.ROLE_USER.name());

        String encryptedPassword = passwordEncoder.encode(member.getPassword());


        //만약, 패스워드를 암호화 하지 않고 User를 등록한다면 User 등록은 되지만 로그인 인증 시, 다음과 같은 에러를 만나게 되므로 User의 패스워드는 반드시 암호화 해야 합니다.
        UserDetails userDetails = new User(member.getEmail(), encryptedPassword, authorities);

        //실제 시큐리티가 관리하는 유저 매니저에 유저 정보를 저자하는 부분
        userDetailsManager.createUser(userDetails);

        return member;
    }

    @Override
    public Member findMember(String username) {
        return null;
    }

    private List<GrantedAuthority> createAuthorities(String... roles) { //가진 권한들을 (권한이 여러개일수 있으므로) List에 담아서 리턴

        return Arrays.stream(roles)
                .map(role-> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());
    }
}
