package com.myplantsdiary.myplantsdiaryapi.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@Log4j2
@RequiredArgsConstructor
@EnableWebSecurity
public class CustomSecurityConfig {


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, HttpSecurity httpSecurity) throws Exception {

        log.info("================================securify config================================");

        http.cors(httpSecurityCorsConfigurer -> {
            httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource());
        });


        http.sessionManagement(sessionConfig ->  sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        /* 스프링 시큐러티 4 버전에서는 스프링 폼에서 CSRF 토큰을 사용해 크로스 사이트 요청 위조(cross-site request forgery)를 방지할 수 있으나, 간단하게 하기 위해 disable() 처리.
        *  disable() : CSFR 토큰을 사용하지 않도록 비활성화.
        *  기본적으로 CSRF 요소 구성없이 만들어진 구성은 유효하지 않으며 모든 로그인 요청은 403으로 이동. --> 전문가를 위한 스프링5 책 16.12 */
        http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());


        http.formLogin(config -> {
            config.loginPage("/api/member/login");});

        return http.build();

    }



    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowCredentials(true);    //header가 없는 응답들은 거르겠다는 뜻

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

}
