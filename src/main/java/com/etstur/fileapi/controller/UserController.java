package com.etstur.fileapi.controller;

import com.etstur.fileapi.converter.UserConverter;
import com.etstur.fileapi.dto.UserDto;
import com.etstur.fileapi.dto.UserWithRoleDto;
import com.etstur.fileapi.model.UserEntity;
import com.etstur.fileapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(
    name = "File API Kullanıcı Kontrolleri",
    description = "Kullanıcı işlemleri için gerekli methodlar")
public class UserController {

  // Kullanıcı servisi bağımlılığı
  @Autowired private UserService userService;

  // Kullanıcının kayıt olması için bir POST endpointi
  @PostMapping("/register")
  @Operation(
      summary = "Kullanıcı olusturma metodu",
      description =
          "Bu metot kullanıcı adı, sifre ve role ile veritabanına sifreyi encode ederek kaydeder",
      tags = "File API Kullanıcı Kontrolleri")
  @ApiResponse(responseCode = "200", description = "Kullanıcı başarıyla kayıt oldu. ")
  @ApiResponse(responseCode = "500", description = "Kullanıcı kaydolurken hata oluştu.")
  public ResponseEntity<?> registerUser(@RequestBody UserWithRoleDto userWithRoleDto) {
    try {
      // UserWithRoleDto entity sınıfına çeviriliyor
      UserEntity userEntity = UserConverter.toEntity(userWithRoleDto);
      // Kullanıcı servisi ile kullanıcıyı kaydet
      UserEntity savedUser = userService.saveUser(userEntity);

      // Başarılı cevap dön
      return ResponseEntity.ok("Kullanıcı başarıyla kayıt oldu. ");

    } catch (Exception e) {
      // Hata cevabı dön
      System.err.println(e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(
              "Kullanıcı kaydolurken hata oluştu: "
                  + userWithRoleDto.getUsername()
                  + " isimli username kayıtlıdır ! ");
    }
  }

  // Kullanıcının giriş yapması için bir POST endpointi
  @PostMapping("/login")
  @Operation(
      summary = "Kullanıcı giris yapma metodu",
      description =
          "Kullanıcı username ve sifresi ile sisteme giris yapar. Bu giris sonrası 10 dakikalık bir token oluşturulur. Bu token ile dosya islemlerini yapabilirsiniz.",
      tags = "File API Kullanıcı Kontrolleri")
  @ApiResponse(responseCode = "200", description = "Kullanıcı başarıyla giriş yaptı. ")
  @ApiResponse(responseCode = "500", description = "Kullanıcı giriş yaparken hata oluştu.")
  public ResponseEntity<?> loginUser(@RequestBody UserDto userDto) {
    try {

      // UserDto entity sınıfına çeviriliyor
      UserEntity userEntity = UserConverter.toEntity(userDto);
      // Kullanıcı servisi ile kullanıcının giriş yapmasını sağla
      String token = userService.login(userEntity);

      // Başarılı cevap dön
      return ResponseEntity.ok("Kullanıcı başarıyla giriş yaptı. Token: " + token);

    } catch (Exception e) {
      // Hata cevabı dön
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Kullanıcı giriş yaparken hata oluştu: " + e.getMessage());
    }
  }
}
