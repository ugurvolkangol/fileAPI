package com.etstur.fileapi.security;

import com.etstur.fileapi.service.UserService;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

// JWT filtresi sınıfı
@Component
public class JwtFilter extends OncePerRequestFilter {

  // JWT sağlayıcı bağımlılığı
  @Autowired private JwtProvider jwtProvider;

  // Kullanıcı servisi bağımlılığı
  @Autowired private UserService userService;

  // Her istek için filtreleme yapmak için bir metod (Spring Security tarafından çağrılır)
  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    if (request.getServletPath().equals("/users/register")
        || request.getRequestURI().startsWith("/api-docs")) {
      filterChain.doFilter(request, response);
    } else {
      // İstekten JWT tokenini al
      String token = jwtProvider.getTokenFromRequest(request);

      // Token geçerli ise kullanıcının kimliğini doğrula
      if (token != null && jwtProvider.validateToken(token)) {
        // Token içindeki kullanıcı adını al
        String username = jwtProvider.getUsernameFromToken(token);

        // Kullanıcı servisi ile kullanıcının detaylarını yükle
        UserDetails userDetails = userService.loadUserByUsername(username);

        // Kullanıcının kimliğini doğrula (Spring Security tarafından yapılır)
        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
      // Filtre zincirine devam et
      filterChain.doFilter(request, response);
    }
  }
}
