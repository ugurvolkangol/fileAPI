package com.etstur.fileapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

// Kullanıcı entity sınıfı
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    // Kullanıcı id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Kullanıcı adı
    @Column(name = "username", unique = true)
    private String username;

    // Kullanıcı şifresi
    @Column(name = "password")
    private String password;

    // Kullanıcı rolü
    @Column(name = "role")
    private String role;

}
