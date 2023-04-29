package com.etstur.fileapi.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Dosya Entity Sınıfı
@Entity
@Table(name = "files")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileEntity {

    // Dosya id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    // Dosya ismi
    @Column(name = "name", unique = true)
    @NotBlank
    private String name;

    // Dosya uzantısı
    @Column(name = "extension")
    @NotBlank
    private String extension;

    // Dosya boyutu
    @Column(name = "size")
    private Long size;

    // Dosya pathi
    @Column(name = "path")
    @NotBlank
    private String path;


}
