package com.etstur.fileapi.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.etstur.fileapi.model.FileEntity;
import com.etstur.fileapi.repository.FileRepository;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

// Test sınıfı
public class FileServiceTest {

  // Test edilecek servis
  private FileService fileService;

  // Mock edilecek repository
  private FileRepository fileRepository;

  // Test dosyalarının kaydedileceği klasör
  private String folder = "C:/files/";

  // Her testten önce yapılacak işlemler
  @BeforeEach
  public void setUp() throws Exception {
    // Mock repository oluştur
    fileRepository = mock(FileRepository.class);

    // Servis nesnesi oluştur ve mock repository bağla
    fileService = new FileService(fileRepository);

    // Test klasörünün içini temizle
    Files.walk(Path.of(folder), 1) // sadece bir seviye gez
        .map(Path::toFile) // dosya nesnesine dönüştür
        .filter(file -> !file.equals(new File(folder))) // klasörü kendisini hariç tut
        .forEach(File::delete); // sil
  }

  // Dosya kaydetme işleminin başarılı olduğunu test eden metod
  @Test
  public void testSaveFileSuccess() throws Exception {
    // Mock dosya oluştur
    MockMultipartFile mockFile =
        new MockMultipartFile("file", "test.pdf", "application/pdf", "test".getBytes());

    // Mock repository'nin kaydetme işlemini taklit et
    when(fileRepository.save(any(FileEntity.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    // Servisin kaydetme metodunu çağır ve sonucu al
    FileEntity result = fileService.saveFile(mockFile);

    // Sonucun beklendiği gibi olduğunu kontrol et
    assertEquals("test.pdf", result.getName());
    assertEquals("pdf", result.getExtension());
    assertEquals(4, result.getSize());
    assertEquals(folder + "test.pdf", result.getPath());

    // Kaydedilen dosyanın sunucuda var olduğunu kontrol et
    File savedFile = new File(folder + "test.pdf");
    assertEquals(true, savedFile.exists());
  }

  // Dosya boyutunun 5mb'dan büyük olduğunu test eden metod
  @Test
  public void testSaveFileSizeExceeded() throws Exception {
    // Mock dosya oluştur (boyutu 6mb)
    byte[] content = new byte[6 * 1024 * 1024];
    MockMultipartFile mockFile = new MockMultipartFile("file", "test.png", "image/png", content);

    // Servisin kaydetme metodunu çağır ve beklenen istisnayı yakala
    Exception exception = assertThrows(Exception.class, () -> fileService.saveFile(mockFile));

    // İstisna mesajının beklendiği gibi olduğunu kontrol et
    assertEquals("Dosya boyutu en fazla 5mb olmalı", exception.getMessage());
  }
  // Dosya uzantısının geçersiz olduğunu test eden metod
  @Test
  public void testSaveFileInvalidExtension() throws Exception {
    // Mock dosya oluştur (uzantısı txt)
    MockMultipartFile mockFile =
        new MockMultipartFile("file", "test.txt", "text/plain", "test".getBytes());

    // Servisin kaydetme metodunu çağır ve beklenen istisnayı yakala
    Exception exception = assertThrows(Exception.class, () -> fileService.saveFile(mockFile));

    // İstisna mesajının beklendiği gibi olduğunu kontrol et
    assertEquals(
        "Dosya uzantısı png, jpeg, jpg, docx, pdf veya xlsx olmalı", exception.getMessage());
  }

  // Dosyaları listeleme işleminin başarılı olduğunu test eden metod
  @Test
  public void testGetFilesSuccess() throws Exception {
    // Mock dosya entity listesi oluştur
    List<FileEntity> mockList = new ArrayList<>();
    mockList.add(new FileEntity(1L, "test1.pdf", "pdf", 4L, folder + "test1.pdf"));
    mockList.add(new FileEntity(2L, "test2.jpg", "jpg", 8L, folder + "test2.jpg"));

    // Mock repository'nin findAll() metodunu taklit et
    when(fileRepository.findAll()).thenReturn(mockList);

    // Servisin listeleme metodunu çağır ve sonucu al
    List<FileEntity> result = fileService.getFiles();

    // Sonucun beklendiği gibi olduğunu kontrol et
    assertEquals(2, result.size());
    assertEquals(mockList, result);
  }

  // Dosyanın içeriğinin başarılı bir şekilde döndürüldüğünü test eden metod
  @Test
  public void testGetFileContentSuccess() throws Exception {
    // Mock dosya id'si oluştur
    Long mockId = 1L;

    // Mock dosya entity oluştur
    FileEntity mockFileEntity = new FileEntity(mockId, "test.pdf", "pdf", 4L, folder + "test.pdf");

    // Mock dosya içeriği oluştur
    byte[] mockContent = "test".getBytes();

    // Mock repository'nin findById() metodunu taklit et
    when(fileRepository.findById(mockId)).thenReturn(Optional.of(mockFileEntity));

    // Mock dosya oluştur
    MockMultipartFile mockFile =
        new MockMultipartFile("file", "test.pdf", "application/pdf", mockContent);

    // Mock repository'nin kaydetme işlemini taklit et
    when(fileRepository.save(any(FileEntity.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    // Servisin kaydetme metodunu çağır ve sonucu al
    FileEntity fileEntity = fileService.saveFile(mockFile);

    // Servisin dosya içeriği metodunu çağır ve sonucu al
    byte[] result = fileService.getFileContent(mockId);

    // Sonucun beklendiği gibi olduğunu kontrol et
    assertArrayEquals(mockContent, result);
  }

  // Dosya bulunamadığında istisna fırlatıldığını test eden metod
  @Test
  public void testGetFileContentNotFound() throws Exception {
    // Mock dosya id'si oluştur
    Long mockId = 2L;

    // Mock repository'nin findById() metodunu taklit et (boş optional dön)
    when(fileRepository.findById(mockId)).thenReturn(Optional.empty());

    // Servisin dosya içeriği metodunu çağır ve beklenen istisnayı yakala
    Exception exception = assertThrows(Exception.class, () -> fileService.getFileContent(mockId));

    // İstisna mesajının beklendiği gibi olduğunu kontrol et
    assertEquals("Dosya bulunamadı", exception.getMessage());
  }

  // Dosyanın güncelleme işleminin başarılı olduğunu test eden metod
  @Test
  public void testUpdateFileSuccess() throws Exception {
    // Mock dosya id'si oluştur
    Long mockId = 1L;

    // Mock eski dosya entity oluştur
    FileEntity mockOldFileEntity =
        new FileEntity(mockId, "test1.pdf", "pdf", 4L, folder + "test1.pdf");

    // Mock yeni dosya oluştur
    MockMultipartFile mockNewFile =
        new MockMultipartFile("file", "test2.jpg", "image/jpg", "test".getBytes());

    // Mock yeni dosya entity oluştur
    FileEntity mockNewFileEntity =
        new FileEntity(mockId, "test2.jpg", "jpg", 4L, folder + "test2.jpg");

    // Mock repository'nin findById() metodunu taklit et
    when(fileRepository.findById(mockId)).thenReturn(Optional.of(mockOldFileEntity));

    // Mock repository'nin save() metodunu taklit et
    when(fileRepository.save(any(FileEntity.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    // Servisin güncelleme metodunu çağır ve sonucu al
    FileEntity result = fileService.updateFile(mockId, mockNewFile);

    // Sonucun beklendiği gibi olduğunu kontrol et
    assertEquals(mockNewFileEntity.getName(), result.getName());
    assertEquals(mockNewFileEntity.getExtension(), result.getExtension());
    assertEquals(mockNewFileEntity.getSize(), result.getSize());
    assertEquals(mockNewFileEntity.getPath(), result.getPath());

    // Eski dosyanın sunucudan silindiğini kontrol et
    File oldFile = new File(folder + "test1.pdf");
    assertEquals(false, oldFile.exists());

    // Yeni dosyanın sunucuya kaydedildiğini kontrol et
    File newFile = new File(folder + "test2.jpg");
    assertEquals(true, newFile.exists());
  }

  // Dosyanın silme işleminin başarılı olduğunu test eden metod
  @Test
  public void testDeleteFileSuccess() throws Exception {
    // Mock dosya id'si oluştur
    Long mockId = 1L;

    // Mock dosya entity oluştur
    FileEntity mockFileEntity = new FileEntity(mockId, "test.pdf", "pdf", 4L, folder + "test.pdf");

    // Mock repository'nin findById() metodunu taklit et
    when(fileRepository.findById(mockId)).thenReturn(Optional.of(mockFileEntity));

    // Servisin silme metodunu çağır (istisna fırlatmaması gerekir)
    assertDoesNotThrow(() -> fileService.deleteFile(mockId));

    // Dosyanın sunucudan silindiğini kontrol et
    File file = new File(folder + "test.pdf");
    assertEquals(false, file.exists());
  }
}
