package com.myplantsdiary.myplantsdiaryapi.auth.google;

import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.myplantsdiary.myplantsdiaryapi.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
public class GoogleLogin {

    private final MemberService memberService;

    @PostMapping("/api/auth/google")
    public Map<String, String> googleLogin(@RequestBody String params){
        log.info("Google login request"  + params);

        AccessToken accessToken = new AccessToken(params, new Date(System.currentTimeMillis() + 30 * 1000));


        System.out.println(accessToken);

        GoogleCredentials credentials = GoogleCredentials.create(accessToken);

        System.out.println(credentials);


        return Map.of("result","success");
    }
}
