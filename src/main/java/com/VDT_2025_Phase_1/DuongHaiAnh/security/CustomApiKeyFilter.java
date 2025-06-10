package com.VDT_2025_Phase_1.DuongHaiAnh.security;

import com.VDT_2025_Phase_1.DuongHaiAnh.entity.AuthAccount;
import com.VDT_2025_Phase_1.DuongHaiAnh.repository.AuthAccountRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class CustomApiKeyFilter extends OncePerRequestFilter {

    @Value("${jwt.apiKey.secret}")
    private String jwtApiKeySecret;

    @Autowired
    private AuthAccountRepository authAccountRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();
        if (!path.startsWith("/system/config")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Apikey ")) {
            filterChain.doFilter(request, response);
//            response.setStatus(HttpStatus.UNAUTHORIZED.value());
//            response.getWriter().write("Missing or invalid Authorization header (expected 'Apikey <token>')");
            return;
        }

        String apiKey = authHeader.substring(7);
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtApiKeySecret)))
                    .build()
                    .parseSignedClaims(apiKey)
                    .getPayload();

            if (!"API_KEY".equals(claims.get("type"))) {
                throw new JwtException("Invalid token type");
            }

            String account = claims.getSubject();
            AuthAccount authAccount = authAccountRepository.findByAccount(account);
            if (authAccount == null) {
                throw new UsernameNotFoundException("Account not found");
            }

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(account, null, List.of());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (JwtException | UsernameNotFoundException e) {
            System.out.println(e);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Invalid API Key: " + e.getMessage());
            return;
        }

        filterChain.doFilter(request, response);
    }
}
