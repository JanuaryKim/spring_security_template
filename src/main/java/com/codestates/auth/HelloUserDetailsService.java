package com.codestates.auth;


import com.codestates.exception.BusinessLogicException;
import com.codestates.exception.ExceptionCode;
import com.codestates.member.Member;
import com.codestates.member.MemberRepository;
import com.codestates.utils.HelloAuthorityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class HelloUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final HelloAuthorityUtils authorityUtils;



    //로그인 요청시 해당 유저가 입력한 username (unique한 id든, 이메일이든) 자동으로 들어옴
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Member> optionalMember = memberRepository.findByEmail(username);
        Member findMember = optionalMember.orElseThrow(()-> {throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND);});

//        Collection<? extends GrantedAuthority> authorities =  authorityUtils.createAuthorities(findMember.getEmail());

//        return new User(findMember.getEmail(), findMember.getPassword(), authorities);
        return new HelloUserDetails(findMember);
    }

    // UserDetails가 UserDetails로써 역할을 하기 위한 메소드를 다 가지고 있음
    private final class HelloUserDetails extends Member implements UserDetails{

        public HelloUserDetails(Member member) {
            setMemberId(member.getMemberId());
            setEmail(member.getEmail());
            setFullName(member.getFullName());
            setPassword(member.getPassword());
            setRoles(member.getRoles());

        }

//        @Override
//        public Collection<? extends GrantedAuthority> getAuthorities() {
//            return authorityUtils.createAuthorities(this.getEmail());
//        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return authorityUtils.createAuthorities(this.getRoles());
        }

        @Override
        public String getUsername() {
            return getName();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }
}
