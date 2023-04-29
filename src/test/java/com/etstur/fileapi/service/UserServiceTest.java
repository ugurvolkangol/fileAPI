package com.etstur.fileapi.service;

import com.etstur.fileapi.model.UserEntity;
import com.etstur.fileapi.repository.UserRepository;
import com.etstur.fileapi.security.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

// Test sınıfı
public class UserServiceTest {

  // Test edilecek servis
  private UserService userService;

  // Mock edilecek bağımlılıklar
  private UserRepository userRepository;
  private PasswordEncoder passwordEncoder;
  private JwtProvider jwtProvider;
  private AuthenticationManager authenticationManager;

  // Her testten önce yapılacak işlemler
  @BeforeEach
  public void setUp() throws Exception {
    // Mock bağımlılıklar oluştur
    userRepository = mock(UserRepository.class);
    passwordEncoder = mock(PasswordEncoder.class);
    jwtProvider = mock(JwtProvider.class);
    authenticationManager = mock(AuthenticationManager.class);

    // Servis nesnesi oluştur ve mock bağımlılıklar bağla
    userService = new UserService(userRepository,passwordEncoder,jwtProvider,authenticationManager);
  }

  // Kullanıcıyı yüklemek için loadUserByUsername metodunun başarılı olduğunu test eden metod
  @Test
  public void testLoadUserByUsernameSuccess() throws Exception {
    // Mock kullanıcı adı oluştur
    String mockUsername = "test";

    // Mock kullanıcı entity oluştur
    UserEntity mockUserEntity = new UserEntity(1L, "test", "test", "USER");

    // Mock user details oluştur
    UserDetails mockUserDetails =
        new User(mockUsername, "test", Collections.singleton(new SimpleGrantedAuthority("USER")));

    // Mock repository'nin findByUsername metodunu taklit et
    when(userRepository.findByUsername(mockUsername)).thenReturn(Optional.of(mockUserEntity));

    // Servisin loadUserByUsername metodunu çağır ve sonucu al
    UserDetails result = userService.loadUserByUsername(mockUsername);

    // Sonucun beklendiği gibi olduğunu kontrol et
    assertEquals(mockUserDetails.getUsername(), result.getUsername());
    assertEquals(mockUserDetails.getPassword(), result.getPassword());
    assertEquals(mockUserDetails.getAuthorities(), result.getAuthorities());
  }

  // Kullanıcıyı yüklemek için loadUserByUsername metodunun kullanıcı bulunamadığında istisna
  // fırlattığını test eden metod
  @Test
  public void testLoadUserByUsernameNotFound() throws Exception {
    // Mock kullanıcı adı oluştur
    String mockUsername = "test";

    // Mock repository'nin findByUsername metodunu taklit et (boş optional dön)
    when(userRepository.findByUsername(mockUsername)).thenReturn(Optional.empty());

    // Servisin loadUserByUsername metodunu çağır ve beklenen istisnayı yakala
    Exception exception =
        assertThrows(
            UsernameNotFoundException.class, () -> userService.loadUserByUsername(mockUsername));

    // İstisna mesajının beklendiği gibi olduğunu kontrol et
    assertEquals("Kullanıcı bulunamadı", exception.getMessage());
  }
  // Kullanıcıyı kaydetmek için saveUser metodunun başarılı olduğunu test eden metod
  @Test
  public void testSaveUserSuccess() throws Exception {
    // Mock kullanıcı entity oluştur
    UserEntity mockUserEntity = new UserEntity(1L, "test", "test", "USER");

    // Mock şifreli parola oluştur
    String mockEncodedPassword = "encoded";

    // Mock repository'nin save metodunu taklit et
    when(userRepository.save(any(UserEntity.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    // Mock password encoder'nin encode metodunu taklit et
    when(passwordEncoder.encode(anyString())).thenReturn(mockEncodedPassword);

    // Servisin saveUser metodunu çağır ve sonucu al
    UserEntity result = userService.saveUser(mockUserEntity);

    // Sonucun beklendiği gibi olduğunu kontrol et
    assertEquals(mockUserEntity.getId(), result.getId());
    assertEquals(mockUserEntity.getUsername(), result.getUsername());
    assertEquals(mockEncodedPassword, result.getPassword()); // parolanın şifrelendiğini kontrol et
    assertEquals(mockUserEntity.getRole(), result.getRole());
  }
  // Kullanıcının başarılı bir şekilde giriş yaptığını ve JWT tokeni aldığını test eden metod
  @Test
  public void testLoginSuccess() throws Exception {
    // Mock kullanıcı entity oluştur
    UserEntity mockUserEntity = new UserEntity(1L, "test", "test", "USER");

    // Mock user details oluştur
    UserDetails mockUserDetails =
        new User(
            mockUserEntity.getUsername(),
            mockUserEntity.getPassword(),
            Collections.singleton(new SimpleGrantedAuthority(mockUserEntity.getRole())));

    // Mock authentication nesnesi oluştur
    Authentication mockAuthentication =
        new UsernamePasswordAuthenticationToken(
            mockUserDetails, null, mockUserDetails.getAuthorities());

    // Mock JWT tokeni oluştur
    String mockToken = "token";

    // Mock authentication manager'nin authenticate metodunu taklit et
    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenReturn(mockAuthentication);

    // Mock jwt provider'nin generateToken metodunu taklit et
    when(jwtProvider.generateToken(any(User.class))).thenReturn(mockToken);

    // Servisin login metodunu çağır ve sonucu al
    String result = userService.login(mockUserEntity);

    // Sonucun beklendiği gibi olduğunu kontrol et
    assertEquals(mockToken, result);
  }

  // Kullanıcının giriş yaparken hatalı bilgi girdiğinde istisna fırlattığını test eden metod
  @Test
  public void testLoginFail() throws Exception {
    // Mock kullanıcı entity oluştur
    UserEntity mockUserEntity = new UserEntity(1L, "test", "test", "USER");

    // Mock authentication manager'nin authenticate metodunu taklit et (BadCredentialsException
    // fırlat)
    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenThrow(new BadCredentialsException(""));

    // Servisin login metodunu çağır ve beklenen istisnayı yakala
    Exception exception = assertThrows(Exception.class, () -> userService.login(mockUserEntity));

    // İstisna mesajının beklendiği gibi olduğunu kontrol et
    assertEquals("Kullanıcı adı veya şifre hatalı", exception.getMessage());
  }
}
