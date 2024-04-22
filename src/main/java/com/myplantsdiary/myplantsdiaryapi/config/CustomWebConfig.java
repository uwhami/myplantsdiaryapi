package com.myplantsdiary.myplantsdiaryapi.config;

import com.myplantsdiary.myplantsdiaryapi.controller.fotmatter.LocalDateFormatter;
import lombok.extern.log4j.Log4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Log4j
public class CustomWebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {

        log.info("...............addFormatters");

        registry.addFormatter(new LocalDateFormatter());
    }


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .maxAge(500)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS") //options : pre-flight
                .allowedOrigins("*");
    }

}
