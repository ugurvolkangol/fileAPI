package com.etstur.fileapi.service;

import com.etstur.fileapi.entity.FileEntity;
import com.etstur.fileapi.repository.FileRepository;
import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

// Dosya servis sınıfı
@Service
public class FileService {

    // Dosya repository bağımlılığı
    @Autowired
    private FileRepository fileRepository;

    // Dosyayı kaydetmek için bir metod
    public FileEntity saveFile(MultipartFile file) throws Exception {
        // Dosyanın boyutunu kontrol et
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new Exception("Dosya boyutu en fazla 5mb olmalı");
        }

        // Dosyanın uzantısını kontrol et
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (!extension.matches("png|jpeg|jpg|docx|pdf|xlsx")) {
            throw new Exception("Dosya uzantısı png, jpeg, jpg, docx, pdf veya xlsx olmalı");
        }

        // Dosyayı sunucuda bir klasöre kaydet
        String path = "C:/files/" + file.getOriginalFilename();
        File dest = new File(path);
        file.transferTo(dest);

        // Dosya entity oluştur ve veritabanına kaydet
        FileEntity fileEntity = new FileEntity();
        fileEntity.setName(file.getOriginalFilename());
        fileEntity.setExtension(extension);
        fileEntity.setSize(file.getSize());
        fileEntity.setPath(path);
        return fileRepository.save(fileEntity);
    }

    // Dosyaları listelemek için bir metod
    public List<FileEntity> getFiles() {
        return fileRepository.findAll();
    }

    // Dosya içeriğini byte array olarak dönmek için bir metod
    public byte[] getFileContent(Long id) throws Exception {
        // Veritabanından dosyayı bul
        Optional<FileEntity> fileOptional = fileRepository.findById(id);
        if (fileOptional.isEmpty()) {
            throw new Exception("Dosya bulunamadı");
        }
        FileEntity fileEntity = fileOptional.get();

        // Sunucudan dosyayı oku ve byte array olarak dön
        File file = new File(fileEntity.getPath());
        return Files.readAllBytes(file.toPath());
    }

    // Dosyayı güncellemek için bir metod
    public FileEntity updateFile(Long id, MultipartFile file) throws Exception {
        // Veritabanından dosyayı bul
        Optional<FileEntity> fileOptional = fileRepository.findById(id);
        if (fileOptional.isEmpty()) {
            throw new Exception("Dosya bulunamadı");
        }
        FileEntity fileEntity = fileOptional.get();

        // Eski dosyayı sunucudan sil
        File oldFile = new File(fileEntity.getPath());
        oldFile.delete();

        // Yeni dosyayı sunucuya kaydet
        String path = "C:/files/" + file.getOriginalFilename();
        File dest = new File(path);
        file.transferTo(dest);

        // Dosya entity güncelle ve veritabanına kaydet
        fileEntity.setName(file.getOriginalFilename());
        fileEntity.setExtension(FilenameUtils.getExtension(file.getOriginalFilename()));
        fileEntity.setSize(file.getSize());
        fileEntity.setPath(path);
        return fileRepository.save(fileEntity);
    }

    // Dosyayı silmek için bir metod
    public void deleteFile(Long id) throws Exception {
        // Veritabanından dosyayı bul
        Optional<FileEntity>fileOptional = fileRepository.findById(id);
        if (fileOptional.isEmpty()) {
            throw new Exception("Dosya bulunamadı");
        }
        FileEntity fileEntity = fileOptional.get();

        // Dosyayı sunucudan sil
        File file = new File(fileEntity.getPath());
        file.delete();

        // Dosyayı veritabanından sil
        fileRepository.delete(fileEntity);
    }
}
