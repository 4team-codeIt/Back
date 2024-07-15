package com.brick.demo.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.brick.demo.auth.controller.AuthController;
import com.brick.demo.auth.dto.DuplicateEmailRequestDto;
import com.brick.demo.auth.dto.DuplicateEmailResponseDto;
import com.brick.demo.auth.dto.SignUpRequestDto;
import com.brick.demo.auth.dto.SigninRequestDto;
import com.brick.demo.auth.dto.SigninResponseDto;
import com.brick.demo.auth.entity.Account;
import com.brick.demo.auth.repository.AccountManager;
import com.brick.demo.auth.service.AuthService;
import com.brick.demo.common.CustomException;
import com.brick.demo.common.ErrorDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthController.class)
@WithMockUser // 시큐리티 기본 설정 때문에 추가
public class AuthControllerBootTests {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AuthService authService;

  @MockBean
  private AccountManager accountManager;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  public void accountDetails() throws Exception {
    Account mockAccount = Account.builder()
        .name("홍길동")
        .email("honghong@gmail.com")
        .password("0000")
        .build();

    Cookie authCookie = new Cookie("email", "honghong@gmail.com");
    given(authService.findAccountByEmail(any(HttpServletRequest.class))).willReturn(mockAccount);

    mockMvc.perform(get("/auth/user")
            .cookie(authCookie))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("name").value("홍길동"))
        .andExpect(jsonPath("email").value("honghong@gmail.com"))
        .andExpect(jsonPath("password").value("0000"));
    verify(authService).findAccountByEmail(any(HttpServletRequest.class));
  }

  @Test
  public void accountDetailsFail() throws Exception {
    given(authService.findAccountByEmail(any(HttpServletRequest.class)))
        .willThrow(new CustomException(ErrorDetails.E001));

    Cookie authCookie = new Cookie("email", "nonexistent@example.com");

    mockMvc.perform(get("/auth/user")
            .cookie(authCookie))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("code").value(ErrorDetails.E001.getCode()))
        .andExpect(jsonPath("message").value(ErrorDetails.E001.getMessage()));
    verify(authService).findAccountByEmail(any(HttpServletRequest.class));
  }

  @Test
  public void createAccount() throws Exception {
    // Given
    SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
        .email("test@example.com")
        .password("password")
        .name("Test User")
        .build();

    mockMvc.perform(post("/auth/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(signUpRequestDto))
            .with(SecurityMockMvcRequestPostProcessors.csrf())) // CSRF 토큰 포함
        .andExpect(status().isCreated());
    verify(authService).createAccount(any(SignUpRequestDto.class));
  }

  @Test
  public void emailDuplicate() throws Exception {
    DuplicateEmailRequestDto duplicateEmailRequestDto = new DuplicateEmailRequestDto(
        "hoho@email.com");
    DuplicateEmailResponseDto duplicateEmailResponseDto = new DuplicateEmailResponseDto(true);

    // 이메일이 중복된 경우
    given(authService.isDuplicatedEmail(any(DuplicateEmailRequestDto.class))).willReturn(
        duplicateEmailResponseDto);

    mockMvc.perform(post("/auth/users/duplicate-email")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(duplicateEmailRequestDto))
            .with(SecurityMockMvcRequestPostProcessors.csrf())) // CSRF 토큰 포함
        .andExpect(status().isOk())
        .andExpect(jsonPath("duplicateEmail").value(true));
    verify(authService).isDuplicatedEmail(any(DuplicateEmailRequestDto.class));
  }

  @Test
  public void emailNotDuplicate() throws Exception {
    DuplicateEmailRequestDto duplicateEmailRequestDto = new DuplicateEmailRequestDto(
        "hoho@email.com");
    DuplicateEmailResponseDto duplicateEmailResponseDto = new DuplicateEmailResponseDto(false);

    // 이메일이 중복되지 않은 경우
    given(authService.isDuplicatedEmail(any(DuplicateEmailRequestDto.class))).willReturn(
        duplicateEmailResponseDto);

    mockMvc.perform(post("/auth/users/duplicate-email")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(duplicateEmailRequestDto))
            .with(SecurityMockMvcRequestPostProcessors.csrf())) // CSRF 토큰 포함
        .andExpect(status().isOk())
        .andExpect(jsonPath("duplicateEmail").value(false));
    verify(authService).isDuplicatedEmail(any(DuplicateEmailRequestDto.class));
  }

  @Test
  public void signinAndGetCookie() throws Exception {
    SigninRequestDto signinRequestDto = new SigninRequestDto("honghong@gmail.com", "password");

    given(authService.signin(any(SigninRequestDto.class), any(HttpServletResponse.class)))
        .willAnswer(invocation -> {
          HttpServletResponse response = invocation.getArgument(1);
          Cookie cookie = new Cookie("email", signinRequestDto.getEmail());
          cookie.setHttpOnly(true);
          cookie.setSecure(true);
          cookie.setPath("/");
          cookie.setMaxAge(10 * 60 * 60);
          response.addCookie(cookie);
          return new SigninResponseDto(signinRequestDto.getEmail());
        });

    mockMvc.perform(post("/auth/signin")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON) // Accept 헤더 추가
            .content(objectMapper.writeValueAsString(signinRequestDto))
            .with(SecurityMockMvcRequestPostProcessors.csrf())) // CSRF 토큰 포함
        .andExpect(status().isOk())
        .andExpect(cookie().exists("email"))
        .andExpect(cookie().value("email", signinRequestDto.getEmail()));
  }

  private PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
