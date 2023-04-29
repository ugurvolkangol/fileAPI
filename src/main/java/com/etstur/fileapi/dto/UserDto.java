package com.etstur.fileapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Kullanıcı adı ve şifre içeren bir DTO sınıfı
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    // Kullanıcı adı
    private String username;

    // Kullanıcı şifresi
    private String password;
}
