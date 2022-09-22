package com.codestates.member;

import com.codestates.exception.BusinessLogicException;
import com.codestates.exception.ExceptionCode;
import com.codestates.utils.HelloAuthorityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
public class DBMemberService implements MemberService { //멤버를 DB에 저장하기 위한 구현체

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final HelloAuthorityUtils helloAuthorityUtils;

    @Override
    public Member createMember(Member member) {

        verifyExistsEmail(member.getEmail());
        String encryptedPassword = passwordEncoder.encode(member.getPassword());

        member.setPassword(encryptedPassword);

        helloAuthorityUtils.createRole(member.getEmail());
        member.setRoles(helloAuthorityUtils.createRole(member.getEmail()));
        Member savedMember = memberRepository.save(member);

        System.out.println("Member Saved!");
        return savedMember;
    }

    @Override
    public Member findMember(String username) {

        return verifyValidEmail(username);
    }

    private Member verifyValidEmail(String username) {

        return memberRepository.findByEmail(username).orElseThrow(()->
                new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
    }

    private void verifyExistsEmail(String email) {

        memberRepository.findByEmail(email).ifPresent(
                member-> {throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS);});

    }
}
