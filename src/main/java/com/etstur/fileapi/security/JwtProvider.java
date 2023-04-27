package com.etstur.fileapi.security;

import com.etstur.fileapi.config.SecurityConstants;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

// JWT sağlayıcı sınıfı
@Component
public class JwtProvider {

    // Bir kullanıcı için bir JWT tokeni oluşturmak için bir metod
    public String generateToken(UserDetails userDetails) {
        // Geçerli tarihi al
        Date now = new Date();

        // Geçerlilik süresini hesapla
        Date validity = new Date(now.getTime() + SecurityConstants.EXPIRATION_TIME);

        // Tokeni oluştur ve dön
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, SecurityConstants.TOKEN_SECRET)
                .compact();
    }

    // Bir istekten JWT tokenini almak için bir metod
    public String getTokenFromRequest(HttpServletRequest request) {
        // İstek başlığından Authorization değerini al
        String authHeader = request.getHeader(SecurityConstants.HEADER_STRING);

        // Başlık "Bearer " ile başlıyorsa tokeni dön, değilse null dön
        if (authHeader != null && authHeader.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            return authHeader.substring(7);
        }
        return null;
    }

    // Bir JWT tokeninin geçerliliğini kontrol etmek için bir metod
    public boolean validateToken(String token) {
        try {
            // Tokeni ayrıştır ve hata olmazsa geçerlidir
            Jwts.parser().setSigningKey(SecurityConstants.TOKEN_SECRET).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // Hata olursa geçersizdir
            return false;
        }
    }

    // Bir JWT tokeninden kullanıcı adını almak için bir metod
    public String getUsernameFromToken(String token) {
        // Tokeni ayrıştır ve kullanıcı adını dön
        return Jwts.parser().setSigningKey(SecurityConstants.TOKEN_SECRET).parseClaimsJws(token).getBody().getSubject();
    }
}
