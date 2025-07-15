package com.example.evcs.auth.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.evcs.auth.service.AuthService;
import com.example.evcs.member.model.dto.MemberDTO;
import com.example.evcs.token.model.service.TokenService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	
	private final AuthService authService;
	private final TokenService tokenService;
	
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody MemberDTO member){
		Map<String, Object> loginResponse = authService.login(member);
		return ResponseEntity.ok(loginResponse);
	}
	
	
	@PostMapping("/refresh")
	public ResponseEntity<?> refresh(@RequestBody Map<String, String> token){
		String refreshToken = token.get("refreshToken");
		Map<String, Object> newToken = tokenService.refreshToken(refreshToken);
		return ResponseEntity.status(HttpStatus.CREATED).body(newToken);
	}

}
