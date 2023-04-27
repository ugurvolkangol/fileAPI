package com.etstur.fileapi.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

//Dosya Entity Sınıfı
@Entity
@Table(name = "files")
@Data
@NoArgsConstructor
public class FileEntity {

    // Dosya id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Dosya ismi
    @Column(name = "name")
    private String name;

    // Dosya uzantısı
    @Column(name = "extension")
    private String extension;

    // Dosya boyutu
    @Column(name = "size")
    private Long size;

    // Dosya pathi
    @Column(name = "path")
    private String path;


}
