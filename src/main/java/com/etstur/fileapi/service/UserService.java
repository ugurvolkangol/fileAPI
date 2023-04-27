package com.etstur.fileapi.service;

import com.etstur.fileapi.security.JwtProvider;
import com.etstur.fileapi.repository.UserRepository;
import com.etstur.fileapi.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

// Kullanıcı servis sınıfı
@Service
public class UserService implements UserDetailsService {

    // Kullanıcı repository bağımlılığı
    @Autowired
    private UserRepository userRepository;

    // Şifre kodlayıcı bağımlılığı
    @Autowired
    private PasswordEncoder passwordEncoder;

    // JWT sağlayıcı bağımlılığı
    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    // Kullanıcının detaylarını yüklemek için bir metod (Spring Security tarafından kullanılır)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Veritabanından kullanıcıyı bul
        Optional<UserEntity> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("Kullanıcı bulunamadı");
        }
        UserEntity userEntity = userOptional.get();

        // Kullanıcının detaylarını dön (Spring Security tarafından tanınan bir sınıf)
        return new User(userEntity.getUsername(), userEntity.getPassword(), getAuthorities(userEntity));
    }

    // Kullanıcının yetkilerini dönmek için bir metod
    private Collection<? extends GrantedAuthority> getAuthorities(UserEntity userEntity) {
        return Collections.singleton(new SimpleGrantedAuthority(userEntity.getRole()));
    }

    // Kullanıcıyı kaydetmek için bir metod
    public UserEntity saveUser(UserEntity userEntity) {
        // Şifreyi kodla ve kullanıcıyı veritabanına kaydet
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        return userRepository.save(userEntity);
    }

    // Kullanıcının giriş yapmasını sağlamak için bir metod
    public String login(UserEntity userEntity) throws Exception {
        try {
            // Kullanıcının kimliğini doğrula (Spring Security tarafından yapılır)
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userEntity.getUsername(), userEntity.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Geçerli kullanıcının detaylarını al
            User userDetails = (User) authentication.getPrincipal();

            // Geçerli kullanıcı için bir JWT tokeni oluştur ve dön
            return jwtProvider.generateToken(userDetails);
        } catch (BadCredentialsException e) {
            throw new Exception("Kullanıcı adı veya şifre hatalı");
        }
    }
}
