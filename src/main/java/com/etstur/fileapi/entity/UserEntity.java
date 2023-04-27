package com.etstur.fileapi.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

// Kullanıcı entity sınıfı
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class UserEntity {

    // Kullanıcı id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Kullanıcı adı
    @Column(name = "username")
    private String username;

    // Kullanıcı şifresi
    @Column(name = "password")
    private String password;

    // Kullanıcı rolü
    @Column(name = "role")
    private String role;

}
