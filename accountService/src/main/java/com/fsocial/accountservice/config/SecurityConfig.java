//package com.fsocial.accountservice.config;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@RequiredArgsConstructor
//@Configuration
//@EnableWebSecurity
////@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
//public class SecurityConfig {
//    private final UserAuthenticationEntryPoint userAuthenticationEntryPoint;
//    private final String[] AUTH_LIST_HEAD = {""};
//    private final String[] AUTH_LIST_INSTRUCTOR = {""};
//    private final String[] AUTH_LIST = {""};
//    private final String[] PERMIT_LIST = {
//            "/oauth2/login/**", "/api/areas", "/api/students", "/swagger-ui/**",
//            "/api-docs/**", "/v3/api-docs/**", "/v3/api-docs", "/v3/api-docs/swagger-config",
//            "/api/activities/**", "/api/subjects/**",
//            "/api/invigilation-registers/**", "/api/activitytypes", "/api/activitytypes/**"
////            ,"/api/busy-exam-schedules","/api/busy-exam-schedules/**","/api/exam-schedules","/api/exam-schedules/**","/api/exam-schedule-logs","/api/exam-schedule-logs/**"
////            ,"/api/invigilation-registers","/api/invigilation-registers/**","/api/invigilation-plans","/api/invigilation-plans/**","/api/notifications/**","/api/notifications"
//    };
//    @Autowired
//    CustomJwtFilter jwtAuthFilter;
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .exceptionHandling(ex -> ex.authenticationEntryPoint(userAuthenticationEntryPoint))
//                .csrf(AbstractHttpConfigurer::disable)
//                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authorizeHttpRequests(auth -> {
//                    auth
//                            .requestMatchers(PERMIT_LIST).permitAll()
//                            .anyRequest().permitAll();
//                });
//        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
//        return http.build();
//    }
//}
