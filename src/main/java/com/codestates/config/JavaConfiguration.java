package com.codestates.config;

import com.codestates.member.DBMemberService;
import com.codestates.member.InMemoryMemberService;
import com.codestates.member.MemberRepository;
import com.codestates.member.MemberService;
import com.codestates.utils.HelloAuthorityUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;

@Configuration
public class JavaConfiguration {

    //여기에 주입되는 UserDetailsManager는 SecurityConfigurationV1에서 빈으로 등록한 InMemory~ 구현체가 주입됨.
//    @Bean
//    public MemberService inMemoryMemberService(UserDetailsManager userDetailsManager,
//                                               PasswordEncoder passwordEncoder) {
//        return new InMemoryMemberService(userDetailsManager, passwordEncoder);
//    }

    @Bean
    public MemberService inMemoryMemberService(MemberRepository memberRepository,
                                               PasswordEncoder passwordEncoder, HelloAuthorityUtils helloAuthorityUtils) {
        return new DBMemberService(memberRepository, passwordEncoder, helloAuthorityUtils);
    }
}
