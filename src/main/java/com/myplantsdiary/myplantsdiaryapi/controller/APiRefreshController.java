package com.myplantsdiary.myplantsdiaryapi.controller;


import com.myplantsdiary.myplantsdiaryapi.util.CustomJWTException;
import com.myplantsdiary.myplantsdiaryapi.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
public class APiRefreshController {

    @RequestMapping("/api/member/refresh")
    public Map<String, Object> refresh(
            @RequestHeader("Authorization") String accessToken,
            String refreshToken
    ){

        if(accessToken == null || refreshToken == null || !accessToken.startsWith("Bearer ")) {
            throw new CustomJWTException("Invalid access token");
        }

        String checkAccessToken = accessToken.substring(7);

        if(!checkExpiredToken(checkAccessToken)){
            return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
        }

        Map<String, Object> claims = JWTUtil.validateToken(refreshToken);
        log.info("Refreshed access token claims : " + refreshToken);

        String newAccessToken = JWTUtil.generateToken(claims, 10);

        String newRefreshToken = checkTime((Integer) claims.get("exp")) ? JWTUtil.generateToken(claims, 60*24) : refreshToken;

        return Map.of("accessToken", newAccessToken, "refreshToken", newRefreshToken);
    }


    private boolean checkTime(Integer exp){

        Date expDate = new Date((long)exp*1000);    //JWT exp를 날짜로 변환.

        long gap = expDate.getTime() - System.currentTimeMillis();  //현재 시간과 차이 계산.

        long leftMin = gap/60000;   //분단위 계산.

        return leftMin < 60;    //남은 시간이 60분 미만인지 체크.
    }


    private boolean checkExpiredToken(String token){
        try{
            JWTUtil.validateToken(token);
        }catch(CustomJWTException ex){
            if(ex.getMessage().equals("Expired")) {
                return true;
            }
        }
        return false;
    }



}
