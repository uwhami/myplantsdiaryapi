package com.myplantsdiary.myplantsdiaryapi.security.filter;

import com.google.gson.Gson;
import com.myplantsdiary.myplantsdiaryapi.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/* OncePerRequestFilter : 모든 request에 대해 필터 제공 */
@Log4j2

public class JWTCheckFilter extends OncePerRequestFilter {

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        String path = request.getRequestURI();
        log.info("check uri ==================== " + path);



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


        // 다음 목적지로 가게 하는 용도.
        filterChain.doFilter(request, response);

    }
}
