package com.myplantsdiary.myplantsdiaryapi.security.filter;

import com.google.gson.Gson;
import com.myplantsdiary.myplantsdiaryapi.dto.MemberDTO;
import com.myplantsdiary.myplantsdiaryapi.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

/* OncePerRequestFilter : 모든 request에 대해 필터 제공 */
@Log4j2

public class JWTCheckFilter extends OncePerRequestFilter {

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        String path = request.getRequestURI();
        log.info("check uri ==================== " + path);

        if(path.startsWith("/api/member/")){
            return true;
        }

        //이미지 조회 경로는 체크하지 않는다면
        if(path.startsWith("/api/products/view/")) {
            return true;
        }


        return false;   /* false -> 체크한다는 뜻 */
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("=======================================");

        log.info("=======================================");

        log.info("=======================================");

        String authorization = request.getHeader("Authorization");
        //Bearer //7자
//        String accessToken = authorization.substring("Bearer ".length());

        try{
            String accessToken = authorization.substring(7);
            Map<String, Object> claims = JWTUtil.validateToken(accessToken);

            log.info(claims);

            // 다음 목적지로 가게 하는 용도.
            //filterChain.doFilter(request, response);

            String email = (String) claims.get("email");
            String pw = (String) claims.get("password");
            String nickname = (String) claims.get("nickname");
            Boolean social = (Boolean) claims.get("social");
            List<String> roleNames = (List<String>) claims.get("roleNames");

            MemberDTO memberDTO = new MemberDTO(email, pw, nickname, social.booleanValue(), roleNames);

            log.info("==============================");
            log.info("memberDTO " + memberDTO);
            log.info(memberDTO.getAuthorities());

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberDTO, pw, memberDTO.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(request, response);

        }catch(Exception e){

            log.error("JWT Check Error.........................");
            log.error(e.getMessage());

            Gson gson = new Gson();
            String msg = gson.toJson(Map.of("error", "ERROR_ACCESS_TOKEN"));

            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print(msg);
            out.close();

        }



    }
}
