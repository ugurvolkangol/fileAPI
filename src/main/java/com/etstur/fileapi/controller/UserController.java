package com.etstur.fileapi.controller;

import com.etstur.fileapi.service.UserService;
import com.etstur.fileapi.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Kullanıcı kontrolörü sınıfı
@RestController
@RequestMapping("/users")
public class UserController {

    // Kullanıcı servisi bağımlılığı
    @Autowired
    private UserService userService;

    // Kullanıcının kayıt olması için bir POST endpointi
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserEntity userEntity) {
        try {
            // Kullanıcı servisi ile kullanıcıyı kaydet
            UserEntity savedUser = userService.saveUser(userEntity);

            // Başarılı cevap dön
            return ResponseEntity.ok("Kullanıcı başarıyla kayıt oldu: ");

        } catch (Exception e) {
            // Hata cevabı dön
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Kullanıcı kaydolurken hata oluştu: " + e.getMessage());
        }
    }

    // Kullanıcının giriş yapması için bir POST endpointi
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserEntity userEntity) {
        try {
            // Kullanıcı servisi ile kullanıcının giriş yapmasını sağla
            String token = userService.login(userEntity);

            // Başarılı cevap dön
            return ResponseEntity.ok("Kullanıcı başarıyla giriş yaptı. Token: " + token);

        } catch (Exception e) {
            // Hata cevabı dön
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Kullanıcı giriş yaparken hata oluştu: " + e.getMessage());
        }
    }
}
