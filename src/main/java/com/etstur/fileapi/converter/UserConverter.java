package com.etstur.fileapi.converter;

import com.etstur.fileapi.dto.UserDto;
import com.etstur.fileapi.dto.UserWithRoleDto;
import com.etstur.fileapi.model.UserEntity;

// DTO sınıfları ile entity sınıfı arasında dönüşüm sağlayan bir converter sınıfı
public class UserConverter {

    // UserDto'yu UserEntity'ye dönüştüren bir metod
    public static UserEntity toEntity(UserDto userDto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userDto.getUsername());
        userEntity.setPassword(userDto.getPassword());
        return userEntity;
    }

    // UserWithRoleDto'yu UserEntity'ye dönüştüren bir metod
    public static UserEntity toEntity(UserWithRoleDto userWithRoleDto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userWithRoleDto.getUsername());
        userEntity.setPassword(userWithRoleDto.getPassword());
        userEntity.setRole(userWithRoleDto.getRole());
        return userEntity;
    }

    // UserEntity'yi UserDto'ya dönüştüren bir metod
    public static UserDto toUserDto(UserEntity userEntity) {
        UserDto userDto = new UserDto();
        userDto.setUsername(userEntity.getUsername());
        userDto.setPassword(userEntity.getPassword());
        return userDto;
    }

    // UserEntity'yi UserWithRoleDto'ya dönüştüren bir metod
    public static UserWithRoleDto toUserWithRoleDto(UserEntity userEntity) {
        UserWithRoleDto userWithRoleDto = new UserWithRoleDto();
        userWithRoleDto.setUsername(userEntity.getUsername());
        userWithRoleDto.setPassword(userEntity.getPassword());
        userWithRoleDto.setRole(userEntity.getRole());
        return userWithRoleDto;
    }
}