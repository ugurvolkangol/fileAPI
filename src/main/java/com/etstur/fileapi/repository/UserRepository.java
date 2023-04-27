package com.etstur.fileapi.repository;

import com.etstur.fileapi.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// Kullanıcı repository sınıfı
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    // Kullanıcı adına göre kullanıcı bulmak için bir metod
    Optional<UserEntity> findByUsername(String username);
}
