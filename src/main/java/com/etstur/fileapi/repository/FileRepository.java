package com.etstur.fileapi.repository;

import com.etstur.fileapi.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Dosya repository sınıfı
@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {

}
