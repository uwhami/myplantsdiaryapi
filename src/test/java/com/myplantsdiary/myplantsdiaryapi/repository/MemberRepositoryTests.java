package com.myplantsdiary.myplantsdiaryapi.repository;

import com.myplantsdiary.myplantsdiaryapi.domain.Member;
import com.myplantsdiary.myplantsdiaryapi.domain.MemberRole;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
@Log4j2
public class MemberRepositoryTests {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testInsert(){

        for(int i=0; i<10; i++){
            Member member = Member.builder()
                    .email("user"+i+"@aaa.com")
                    .pw(passwordEncoder.encode("1"))
                    .nickname("USER"+i)
                    .build();

            member.addRole(MemberRole.USER);

            if(i > 4){
                member.addRole(MemberRole.MANAGER);
            }
            if(i > 7){
                member.addRole(MemberRole.ADMIN);
            }
            memberRepository.save(member);
        }
    }

    @Test
    public void testRead(){
        String email = "user0@aaa.com";
        Member member = memberRepository.getWithRoles(email);   //자동으로 조인되는지 확인
        log.info("===================");
        log.info(member);
        log.info(member.getMemberRoleList());
    }

}
