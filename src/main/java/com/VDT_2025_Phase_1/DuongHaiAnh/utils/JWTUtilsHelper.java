package com.VDT_2025_Phase_1.DuongHaiAnh.utils;

import com.VDT_2025_Phase_1.DuongHaiAnh.dto.AuthenticatedUserDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Component
@Getter
@Setter
public class JWTUtilsHelper {

    @Value("${jwt.access.secret}")
    private String jwtAccessTokenSecret;

    @Value("${jwt.access.expiration}")
    private String jwtAccessTokenExpiration;

    @Value("${jwt.refresh.secret}")
    private String jwtRefreshTokenSecret;

    @Value("${jwt.refresh.expiration}")
    private String jwtRefreshTokenExpiration;

    public String generateJwtToken(AuthenticatedUserDTO user, String tokenSecret, String tokenExpiration) {
        Instant now = Instant.now();
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(tokenSecret));
        Date expiration = ExpirationUtilsHelper.calculateExpiration(tokenExpiration, Instant.now());
        return  Jwts.builder()
                .subject(user.getAccount())
                .claim("userId",  user.getUserId())
                .claim("roles", user.getRoles())
                .claim("permissions",  user.getPermissions())
                .issuedAt(Date.from(now))
                .expiration(expiration)
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    public String generateAccessToken(AuthenticatedUserDTO user){
        return generateJwtToken(user, jwtAccessTokenSecret, jwtAccessTokenExpiration);
    }

    public String generateRefreshToken(AuthenticatedUserDTO user){
        return generateJwtToken(user, jwtRefreshTokenSecret, jwtRefreshTokenExpiration);
    }

    public Boolean validateAccessToken(String accessToken){
        try{
            SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessTokenSecret));
            Jwts.parser().verifyWith(key).build().parseSignedClaims(accessToken);

            return true;
        }catch (Exception e){
            throw e;
        }
    }

    private Claims getClaims(String accessToken, boolean isAccessToken){
        SecretKey key = null;
        if(isAccessToken){
            key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessTokenSecret));
        }
        else{
            key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtRefreshTokenSecret));
        }
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(accessToken)
                .getPayload();
    }

    public String extractSubject(String accessToken, boolean isAccessToken){
        return getClaims(accessToken, isAccessToken).getSubject();
    }

    public List<String> extractRoles(String accessToken, boolean isAccessToken) {
        Claims claims = getClaims(accessToken, isAccessToken);
        return claims.get("roles", List.class); // loi
    }

    public List<String> extractPermissions(String accessToken, boolean isAccessToken) {
        Claims claims = getClaims(accessToken, isAccessToken);
        return claims.get("permissions", List.class); // loi
    }
}
