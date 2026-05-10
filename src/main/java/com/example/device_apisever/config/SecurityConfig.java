package com.example.device_apisever.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/login", "/api/auth/register-init", "/api/auth/register-confirm", "/api/auth/forgot-password-init", "/api/auth/forgot-password-confirm").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                .requestMatchers("/error").permitAll()
                // Danh mục: GET cho tất cả user đã đăng nhập, CUD chỉ ADMIN, THU_KHO
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/danh-muc/**").authenticated()
                .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/danh-muc/**").hasAnyRole("ADMIN", "THU_KHO")
                .requestMatchers(org.springframework.http.HttpMethod.PUT, "/api/danh-muc/**").hasAnyRole("ADMIN", "THU_KHO")
                .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/danh-muc/**").hasAnyRole("ADMIN", "THU_KHO")
                // Loại thiết bị: GET cho tất cả, CUD chỉ ADMIN, THU_KHO
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/loai-thiet-bi/**").authenticated()
                .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/loai-thiet-bi/**").hasAnyRole("ADMIN", "THU_KHO")
                .requestMatchers(org.springframework.http.HttpMethod.PUT, "/api/loai-thiet-bi/**").hasAnyRole("ADMIN", "THU_KHO")
                .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/loai-thiet-bi/**").hasAnyRole("ADMIN", "THU_KHO")
                // Thiết bị: GET cho tất cả, CUD chỉ ADMIN, THU_KHO
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/thiet-bi/**").authenticated()
                .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/thiet-bi/**").hasAnyRole("ADMIN", "THU_KHO")
                .requestMatchers(org.springframework.http.HttpMethod.PUT, "/api/thiet-bi/**").hasAnyRole("ADMIN", "THU_KHO")
                .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/thiet-bi/**").hasAnyRole("ADMIN", "THU_KHO")
                // Ảnh: chỉ ADMIN, THU_KHO mới có quyền upload/xóa
                .requestMatchers("/api/images/**").hasAnyRole("ADMIN", "THU_KHO")
                // Nhà cung cấp: chỉ GET
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/nha-cung-cap/**").authenticated()
                // Giỏ hàng: chỉ Khách hàng mới có quyền
                .requestMatchers("/api/gio-hang/**").hasRole("KHACH_HANG")
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
