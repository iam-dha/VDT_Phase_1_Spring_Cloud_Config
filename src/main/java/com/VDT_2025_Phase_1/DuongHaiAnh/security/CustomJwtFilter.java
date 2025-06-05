package com.VDT_2025_Phase_1.DuongHaiAnh.security;

import com.VDT_2025_Phase_1.DuongHaiAnh.utils.JWTUtilsHelper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomJwtFilter extends OncePerRequestFilter {


    private final JWTUtilsHelper jwtUtilsHelper;

    public CustomJwtFilter(JWTUtilsHelper jwtUtilsHelper) {
        this.jwtUtilsHelper = jwtUtilsHelper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = getAccessTokenFromHeader(request);
        String path = request.getServletPath();
        if (path.startsWith("/auth") || path.startsWith("/system/config")) {
            filterChain.doFilter(request, response);
            return;
        }
        try{
            if(StringUtils.hasText(accessToken) && jwtUtilsHelper.validateAccessToken(accessToken)){
                String username = jwtUtilsHelper.extractSubject(accessToken, true);
                List<String> roles = jwtUtilsHelper.extractRoles(accessToken, true); // bạn cần viết hàm này
                List<String>  permissions = jwtUtilsHelper.extractPermissions(accessToken, true);
                List<GrantedAuthority> authorities = new ArrayList<>();
                roles.forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role)));
                permissions.forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission)));
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            };
        }catch (ExpiredJwtException e){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"message\": \"Access token expired\"}");
            response.flushBuffer();
            return;
        }catch (JwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            response.setContentType("application/json");
            response.getWriter().write("{\"message\": \"Invalid JWT token\"}");
            response.flushBuffer();
            return;
        }
        filterChain.doFilter(request,response);
    }

    private String getAccessTokenFromHeader(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        String accessToken = null;
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            accessToken = header.substring(7);
        }
        return accessToken;

    }
}
