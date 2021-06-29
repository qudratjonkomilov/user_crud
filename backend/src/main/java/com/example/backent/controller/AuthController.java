package com.example.backent.controller;

import com.example.backent.entity.User;
import com.example.backent.payload.ApiResponseModel;
import com.example.backent.payload.ReqSignIn;
import com.example.backent.payload.ReqSignUp;
import com.example.backent.security.AuthService;
import com.example.backent.security.CurrentUser;
import com.example.backent.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired JwtTokenProvider jwtTokenProvider;

  @Autowired AuthenticationManager authenticate;

  @Autowired AuthService authService;

  // **************** GET  YOURSELF  ****************//
  @GetMapping("/me")
  public ApiResponseModel getUser(@CurrentUser User user) {
    return new ApiResponseModel(
        user != null ? 200 : 204, user != null ? "user info" : "Error", user);
  }

  // **************** REGISTER USER ****************//
  @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
  @PostMapping("/register")
  public ApiResponseModel register(@Valid @RequestBody ReqSignUp reqSignUp) {
    return authService.register(reqSignUp);
  }

  // **************** LOGIN USER ****************//
  @PermitAll
  @PostMapping("/login")
  public ApiResponseModel login(@Valid @RequestBody ReqSignIn reqSignIn) {
    try {
      return new ApiResponseModel(
          HttpStatus.OK.value(),
          "login",
          authService.getApiToken(reqSignIn.getEmail(), reqSignIn.getPassword()).getBody());
    } catch (Exception e) {
      return new ApiResponseModel(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }
  }

  @ExceptionHandler
  @ResponseStatus(code = HttpStatus.BAD_REQUEST)
  public ApiResponseModel handleException(Exception e) {
    return new ApiResponseModel(HttpStatus.BAD_REQUEST.value(), e.getMessage(), e.hashCode());
  }
}
