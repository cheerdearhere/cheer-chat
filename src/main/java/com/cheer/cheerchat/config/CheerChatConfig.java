package com.cheer.cheerchat.config;

import jakarta.validation.constraints.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class CheerChatConfig implements JwtConstant{
    @Bean
    public SecurityFilterChain securityFilterChain(final @NotNull HttpSecurity http) throws Exception{
        http.httpBasic(HttpBasicConfigurer::disable)
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize ->
                                authorize.requestMatchers("/api/**").authenticated()
                                        .requestMatchers("/").permitAll()
                                        .requestMatchers("/api-docs/swagger-config","/sign-in", "sign-up").permitAll()
                                        .requestMatchers(HttpMethod.OPTIONS,"/**").permitAll()
//                        .requestMatchers("/api/chat/**").hasRole(ROLE-enum)
                                        .anyRequest().permitAll()
//                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtTokenValidator(), BasicAuthenticationFilter.class)
                .csrf(CsrfConfigurer::disable)
                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()))
//            .exceptionHandling(exceptionConfig ->
//                    exceptionConfig.authenticationEntryPoint(로그인 실패 엔드포인트 설정).accessDeniedHandler(입력 거절 처리)
//            )
//            .formLogin(formLogin ->
//                    formLogin
//                            .loginPage("/login/login")
//                            .usernameParameter("username")
//                            .passwordParameter("password")
//                            .loginProcessingUrl("/login/login-proc")
//                            .defaultSuccessUrl("/", true)
//            )
//            .logout(logoutConfig ->
//                    logoutConfig.logoutSuccessUrl("/")
//            )
//            .userDetailsService(회원정보 관리 UserDetailsService)
        ;
        return http.build();
    }
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000xw" // react 포트
        ));
        cfg.setAllowedMethods(Collections.singletonList("*"));
        cfg.setAllowCredentials(true);
        cfg.setAllowedHeaders(Collections.singletonList("*"));
        cfg.setExposedHeaders(Arrays.asList(JWT_HEADER));
        cfg.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",cfg);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
