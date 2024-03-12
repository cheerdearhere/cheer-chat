package com.cheer.cheerchat.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class CheerChatConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(final @NotNull HttpSecurity http) throws Exception{
        http.httpBasic(HttpBasicConfigurer::disable)
            .csrf(CsrfConfigurer::disable)
            .cors(Customizer.withDefaults())
            .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize ->
                authorize
                    .requestMatchers("/api/**").permitAll()
                    .requestMatchers("/api-docs/swagger-config","/sign-in", "sign-up").permitAll()
                    .requestMatchers(HttpMethod.OPTIONS,"/**").permitAll()
//                      .requestMatchers("/api/chat/**").hasRole(ROLE-enum)
//                    .anyRequest().authenticated()
            )
//            .exceptionHandling(exceptionConfig ->
//                    exceptionConfig.authenticationEntryPoint(로그인 실패 엔드포인트 설정).accessDeniedHandler(입력 거절 처리)
//            )
            .formLogin((formLogin) ->
                    formLogin
                            .loginPage("/login/login")
                            .usernameParameter("username")
                            .passwordParameter("password")
                            .loginProcessingUrl("/login/login-proc")
                            .defaultSuccessUrl("/", true)
            )
            .logout((logoutConfig) ->
                    logoutConfig.logoutSuccessUrl("/")
            )
//            .userDetailsService(회원정보 관리 UserDetailsService)
        ;
        return http.build();
    }
}
