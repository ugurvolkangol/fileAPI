package com.etstur.fileapi.controller;

import com.etstur.fileapi.model.FileEntity;
import com.etstur.fileapi.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

// Dosya saklama ve listeleme işlemleri için bir API sınıfı
@RestController
@RequestMapping("/files")
@Tag(name = "File API", description = "Dosya işlemleri için API")
public class FileController {

    // Dosya servisi bağımlılığı
    @Autowired
    private FileService fileService;

    // Dosyaları sunucuya yüklemek için bir POST endpointi
    @PostMapping("/upload")
    @Operation(summary = "Dosya yükle", description = "Bir dosyayı sunucuya yükler ve veritabanında kaydeder", tags = "File API")
    @ApiResponse(responseCode = "200", description = "Dosya başarıyla yüklendi")
    @ApiResponse(responseCode = "500", description = "Dosya yüklenirken hata oluştu")
    public ResponseEntity<?> uploadFile(@RequestParam("file") @Parameter(description = "Yüklenecek dosya", example = "test.pdf") MultipartFile file) {
        try {
            // Dosya servisi ile dosyayı kaydet
            FileEntity fileEntity = fileService.saveFile(file);

            // Başarılı cevap dön
            return ResponseEntity.ok("Dosya başarıyla yüklendi: " + fileEntity);

        } catch (Exception e) {
            // Hata cevabı dön
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Dosya yüklenirken hata oluştu: " + e.getMessage());
        }
    }

    // Dosyaların bilgilerini listelemek için bir GET endpointi
    @GetMapping("/list")
    @Operation(summary = "Dosya listele", description = "Kayıtlı dosyaların bilgilerini listeler", tags = "File API")
    @ApiResponse(responseCode = "200", description = "Dosyalar başarıyla listelendi")
    @ApiResponse(responseCode = "500", description = "Dosyalar listelenirken hata oluştu")
    public ResponseEntity<?> listFiles() {
        try {
            // Dosya servisi ile dosyaları getir
            List<FileEntity> files = fileService.getFiles();

            // Başarılı cevap dön
            return ResponseEntity.ok(files);

        } catch (Exception e) {
            // Hata cevabı dön
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Dosyalar listelenirken hata oluştu: " + e.getMessage());
        }
    }

    // Dosya içeriğini byte array olarak dönmek için bir GET endpointi
    @GetMapping("/content/{id}")
    @Operation(summary = "Dosya içeriği al", description = "Bir dosyanın içeriğini byte array olarak döner", tags = "File API")
    @ApiResponse(responseCode = "200", description = "Dosya içeriği başarıyla alındı")
    @ApiResponse(responseCode = "404", description = "Dosya bulunamadı")
    @ApiResponse(responseCode = "500", description = "Dosya içeriği alınırken hata oluştu")
    public ResponseEntity<?> getFileContent(@PathVariable Long id) {
        try {
            // Dosya servisi ile dosya içeriğini getir
            byte[] content = fileService.getFileContent(id);

            // Başarılı cevap dön
            return ResponseEntity.ok(content);

        } catch (Exception e) {
            // Hata cevabı dön
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Dosya içeriği alınırken hata oluştu: " + e.getMessage());
        }
    }

    // Dosyayı değiştirmek için bir PUT endpointi
    @PutMapping("/update/{id}")
    @Operation(summary = "Dosya güncelle", description = "Veritabanındaki bir dosyayı günceller", tags = "File API")
    @ApiResponse(responseCode = "200", description = "Dosya başarıyla güncellendi")
    @ApiResponse(responseCode = "500", description = "Dosya güncellenirken hata oluştu")
    public ResponseEntity<?> updateFile(@PathVariable Long id, @RequestParam("file") @Parameter(description = "Güncellenecek dosya", example = "test.pdf") MultipartFile file) {
        try {
            // Dosya servisi ile dosyayı güncelle
            FileEntity fileEntity = fileService.updateFile(id, file);

            // Başarılı cevap dön
            return ResponseEntity.ok("Dosya başarıyla güncellendi: " + fileEntity);

        } catch (Exception e) {
            // Hata cevabı dön
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Dosya güncellenirken hata oluştu: " + e.getMessage());
        }
    }

    // Dosyayı silmek için bir DELETE endpointi
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Dosya sil", description = "Bir dosyayı veritabanından siler", tags = "File API")
    @ApiResponse(responseCode = "200", description = "Dosya başarıyla silindi")
    @ApiResponse(responseCode = "500", description = "Dosya silinirken hata oluştu")
    public ResponseEntity<?> deleteFile(@PathVariable Long id) {
        try {
            // Dosya servisi ile dosyayı sil
            fileService.deleteFile(id);

            // Başarılı cevap dön
            return ResponseEntity.ok("Dosya başarıyla silindi.");

        } catch (Exception e) {
            // Hata cevabı dön
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Dosya silinirken hata oluştu: " + e.getMessage());
        }
    }
}