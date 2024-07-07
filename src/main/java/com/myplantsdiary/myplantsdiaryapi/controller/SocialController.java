package com.myplantsdiary.myplantsdiaryapi.controller;

import com.myplantsdiary.myplantsdiaryapi.dto.MemberDTO;
import com.myplantsdiary.myplantsdiaryapi.dto.MemberModifyDTO;
import com.myplantsdiary.myplantsdiaryapi.service.MemberService;
import com.myplantsdiary.myplantsdiaryapi.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
public class SocialController {

    private final MemberService memberService;

    @GetMapping("/api/member/kakao")    //JWT Filter를 거치지 않도록 설정함.
    public Map<String, Object> getMemberFromKakao(String accessToken){

        log.info("accessToken : " + accessToken);

        MemberDTO memberDTO = memberService.getKakaoMember(accessToken);

        Map<String, Object> claims = memberDTO.getClaims();

        String jwtAccessToken = JWTUtil.generateToken(claims, 10);
        String jwtRefreshToken = JWTUtil.generateToken(claims, 60*24);

        claims.put("accessToken", jwtAccessToken);
        claims.put("refreshToken", jwtRefreshToken);

        return claims;
    }


    @PutMapping("/api/member/modify")
    public Map<String, String> modify(@RequestBody MemberModifyDTO modifyDTO){

        log.info("/api/member/modify modifyDTO : " + modifyDTO);

        memberService.modifyMember(modifyDTO);

        return Map.of("result","success");
    }

}
