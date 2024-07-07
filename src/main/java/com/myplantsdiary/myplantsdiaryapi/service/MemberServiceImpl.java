package com.myplantsdiary.myplantsdiaryapi.service;

import com.myplantsdiary.myplantsdiaryapi.domain.Member;
import com.myplantsdiary.myplantsdiaryapi.domain.MemberRole;
import com.myplantsdiary.myplantsdiaryapi.dto.MemberDTO;
import com.myplantsdiary.myplantsdiaryapi.dto.MemberModifyDTO;
import com.myplantsdiary.myplantsdiaryapi.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.LinkedHashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public MemberDTO getKakaoMember(String accessToken) {

        //accessToken을 이용해서 사용자 정보 가져오기.
        String nickname = getEmailFromKakaoAccessToken(accessToken);

        //기존에 DB에 회원 정보가 있는 경우 or 없는 경우
        Optional<Member> result = memberRepository.findById(nickname);

        if(result.isPresent()) {
            Member member = result.get();
            log.info("existed..................");
            return entityToDTO(member);
        }

        Member socialMember = makeSocialMember(nickname);

        memberRepository.save(socialMember);

        return entityToDTO(socialMember);
    }

    @Override
    public void modifyMember(MemberModifyDTO memberModifyDTO) {

        Optional<Member> result = memberRepository.findById(memberModifyDTO.getEmail());

        Member member = result.orElseThrow();
        member.changeNickname(memberModifyDTO.getNickname());
        member.changeSocial(false);
        member.changePassword(passwordEncoder.encode(memberModifyDTO.getPassword()));

        memberRepository.save(member);
    }

    private Member makeSocialMember(String nickname){

        String tempPassword = makeTempPassword();
        log.info("tempPassword : " + tempPassword);

        Member member=  Member.builder()
                .email(nickname)
                .nickname("social Member")
                .password(passwordEncoder.encode(tempPassword))
                .social(true)
                .build();

        member.addRole(MemberRole.USER);

        return member;

    }

    private String getEmailFromKakaoAccessToken(String accessToken) {

        String kakaoGetUserURL = "https://kapi.kakao.com/v2/user/me";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-Type", "Content-type: application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(kakaoGetUserURL).build();

        ResponseEntity<LinkedHashMap> response = restTemplate.exchange(uriComponents.toUri(), HttpMethod.GET, entity, LinkedHashMap.class);

        log.info("response.........................");
        log.info(response.getBody());


        LinkedHashMap<String, LinkedHashMap> bodyMap = response.getBody();
        log.info("bodyMap-------------------------------------------");
        log.info(bodyMap);

        LinkedHashMap<String, LinkedHashMap> kakaoAccount = bodyMap.get("kakao_account");
        LinkedHashMap<String, String> profile = kakaoAccount.get("profile");
        log.info("kakaoAccount.prifile: " + profile);
        String nickname = profile.get("nickname");
        log.info("nickname: " + nickname);
        log.info("bodyMap-------------------------------------------");
        return nickname;

    }

    private String makeTempPassword(){
        StringBuffer buffer = new StringBuffer();
        for(int i=0; i<10 ;i++){
            buffer.append((char)((int)(Math.random()*55)*75));
        }
        return buffer.toString();
    }

}
