package com.myplantsdiary.myplantsdiaryapi.controller;

import com.myplantsdiary.myplantsdiaryapi.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Log4j2
public class SocialController {

    private final MemberService memberService;

    @GetMapping("/api/member/kakao")    //JWT Filter를 거치지 않도록 설정함.
    public String[] getMemberFromKakao(String accessToken){

        log.info("accessToken : " + accessToken);

        memberService.getKakaoMember(accessToken);

        return new String[]{"aaa","bbb","ccc"};
    }

}
