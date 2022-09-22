package com.codestates.member;

import com.codestates.audit.Auditable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.security.auth.Subject;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Member extends Auditable implements Principal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(length = 100, nullable = false)
    private String fullName;

    @Column(nullable = false, updatable = false, unique = true)
    private String email;

    @Column(length = 100, nullable = false)
    private String password;


    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();


//    @Enumerated(value = EnumType.STRING)
//    @Column(length = 20, nullable = false)
//    private MemberStatus memberStatus = MemberStatus.MEMBER_ACTIVE;

    public Member(String email) {
        this.email = email;
    }

    public Member(String email, String fullName, String password) {
        this.email = email;
        this.fullName = fullName;
        this.password = password;
    }

    //Principal 역할을 하기 위해 필요한 메소드
    @Override
    public String getName() {
        return getEmail();
    }



//    public enum MemberStatus {
//        MEMBER_ACTIVE("활동중"),
//        MEMBER_SLEEP("휴면 상태"),
//        MEMBER_QUIT("탈퇴 상태");
//
//        @Getter
//        private String status;
//
//        MemberStatus(String status) {
//           this.status = status;
//        }
//    }


    @Getter
    public enum MemberRole{ //Spring Security에서는 SimpleGrantedAuthority 를 사용해 Role 베이스 형태의 권한을 지정할 때 ‘Roll_’ + 권한명 형태로 지정해 주어야 합니다. 그렇지 않을 경우 적절한 권한 매핑이 이루어지지 않는다는 사실을 기억하기 바랍니다.
        ROLE_USER("ROLE_USER"),
        ROLE_ADMIN("ROLE_ADMIN");

        private String name;

        MemberRole(String name) {
            this.name = name;

        }
    }
}
