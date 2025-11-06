package com.fpt.mss.msaccount_se181513.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@RequiredArgsConstructor
@EnableWebMvc
public class WebSecurityConfig {

    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AccessDeniedHandler accessDeniedHandler;

    //  private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Value("${api.prefix}")
    private String apiPrefix;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(AbstractHttpConfigurer::disable) // Disable CORS - handled by API Gateway
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(
                        ex ->
                                ex.authenticationEntryPoint(authenticationEntryPoint)
                                        .accessDeniedHandler(accessDeniedHandler))
                .authorizeHttpRequests(
                        auth ->
                                auth
                                        // === PUBLIC ENDPOINTS (No authentication required) ===

                                        // Public endpoints
                                        .requestMatchers(
                                                String.format("%s/auth/login", apiPrefix),
                                                String.format("%s/auth/logout", apiPrefix),
                                                String.format("%s/auth/validate", apiPrefix),
                                                String.format("%s/auth/activate", apiPrefix),
                                                String.format("%s/auth/refresh", apiPrefix),
                                                String.format("%s/auth/register", apiPrefix),
                                                String.format(
                                                        "%s/auth/reset-password/**", apiPrefix),
                                                String.format("%s/auth/users/**", apiPrefix),
                                                String.format("%s/public/**", apiPrefix))
                                        .permitAll()

                                        // Swagger UI and API docs
                                        .requestMatchers(
                                                apiPrefix + "/performance/**",
                                                "/graphiql",
                                                "/graphql",
                                                "/scalar",
                                                "/scalar/**",
                                                "/error",
                                                "/favicon.ico",
                                                "/v3/api-docs/**",
                                                "/v3/api-docs.yaml",
                                                "/v3/api-docs/swagger-config",
                                                "/swagger-ui/**",
                                                "/swagger-ui.html",
                                                apiPrefix + "/swagger-ui/**",
                                                apiPrefix + "/swagger-ui.html",
                                                apiPrefix + "/api-docs/**",
                                                "/custom-swagger-ui/**",
                                                "/actuator/**")
                                        .permitAll()

                                        // All other endpoints require authentication
                                        .anyRequest()
                                        .authenticated());
        // Add JWT authentication filter before the standard authentication filter
        //        .addFilterBefore(jwtAuthenticationFilter,
        // UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // CORS is now handled by API Gateway - no need for CORS configuration here
}
