package com.etstur.fileapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Kullanıcı adı, şifre ve rol içeren bir DTO sınıfı
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserWithRoleDto {

    // Kullanıcı adı
    private String username;

    // Kullanıcı şifresi
    private String password;

    // Kullanıcı rolü
    private String role;
}
