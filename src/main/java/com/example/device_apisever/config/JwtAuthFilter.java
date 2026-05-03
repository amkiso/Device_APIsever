package com.example.device_apisever.config;

import com.example.device_apisever.entity.NguoiDung;
import com.example.device_apisever.repository.NguoiDungRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final NguoiDungRepository nguoiDungRepository;

    public JwtAuthFilter(JwtService jwtService, NguoiDungRepository nguoiDungRepository) {
        this.jwtService = jwtService;
        this.nguoiDungRepository = nguoiDungRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String jwt = authHeader.substring(7);
            final String username = jwtService.extractUsername(jwt);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Verify user still exists and is active
                NguoiDung nguoiDung = nguoiDungRepository.findByTaiKhoan(username).orElse(null);

                if (nguoiDung != null && nguoiDung.getTrangThaiId() == 1
                        && jwtService.isTokenValid(jwt, username)) {

                    String role = mapVaiTroToRole(jwtService.extractVaiTroId(jwt));
                    List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(username, null, authorities);
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // Token invalid — do nothing, let request proceed unauthenticated
            logger.warn("JWT authentication failed: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Map VaiTroID sang Spring Security role.
     * 1=Admin, 2=Thủ kho, 3=KTV, 4=Khách hàng
     */
    private String mapVaiTroToRole(Integer vaiTroId) {
        if (vaiTroId == null) return "ROLE_KHACH_HANG";
        return switch (vaiTroId) {
            case 1 -> "ROLE_ADMIN";
            case 2 -> "ROLE_THU_KHO";
            case 3 -> "ROLE_KY_THUAT";
            case 4 -> "ROLE_KHACH_HANG";
            default -> "ROLE_KHACH_HANG";
        };
    }
}
