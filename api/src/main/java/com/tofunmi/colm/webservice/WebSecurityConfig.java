package com.tofunmi.colm.webservice;

import com.tofunmi.colm.webservice.auth.CustomSessionTokenFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Collections;

/**
 * Created By tofunmi on 13/07/2022
 */

@Configuration
public class WebSecurityConfig {

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/*",
                        "/static/**",
                        "/gsi/**",
                        "/api/user/setup/**")
                .antMatchers(HttpMethod.OPTIONS, "/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, CustomSessionTokenFilter customSessionTokenFilter) throws Exception {
        // Configure our custom filter to kick in after UsernamePasswordAuthenticationFilter in the FilterChain
        http.addFilterAfter(customSessionTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests((auth) -> auth.anyRequest().authenticated())
                .sessionManagement((config) -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin().disable()
                .csrf().disable()
                .httpBasic().disable();
        return http.build();
    }

    @Bean
    public CorsFilter corsFilter(@Value("${front-end-url}") String frontEndUrl) {
        return new CorsFilter(configurationSource(frontEndUrl));
    }

    private CorsConfigurationSource configurationSource(String frontEndUrl) {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration(frontEndUrl));
        return source;
    }

    private CorsConfiguration corsConfiguration(String frontEndUrl) {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin(frontEndUrl);
        config.setAllowedHeaders(Collections.singletonList("*"));
        config.setAllowedMethods(Collections.singletonList("*"));
        return config;
    }
}
