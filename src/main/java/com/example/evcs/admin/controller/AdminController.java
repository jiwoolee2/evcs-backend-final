package com.example.evcs.admin.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.evcs.admin.model.service.AdminService;
import com.example.evcs.auth.model.vo.CustomUserDetails;
import com.example.evcs.auth.service.AuthService;
import com.example.evcs.exception.CustomAuthenticationException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("api/admin")
@RestController
@RequiredArgsConstructor
public class AdminController {

	private final AdminService adminService;
	private final AuthService authService;

	@PostMapping
	public ResponseEntity<?> checkAdminRole() {
		CustomUserDetails user = authService.getUserDetails();
		if (!user.isAdmin()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("관리자만 접근할 수 있습니다.");
		}
		return ResponseEntity.status(200).build();
	}

	@GetMapping("/user/info")
	public ResponseEntity<?> getUserInfo() {
		try {
			CustomUserDetails user = authService.getUserDetails();
			Map<String, Object> userInfo = new HashMap<>();
			userInfo.put("username", user.getUsername());
			userInfo.put("isAdmin", user.isAdmin());

			return ResponseEntity.ok(userInfo);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 정보가 없습니다.");
		}
	}

	@PostMapping("/{memberNo}/ban")
	public ResponseEntity<?> banMember(@PathVariable("memberNo") Long memberNo) {
		adminService.banMember(memberNo);
		return ResponseEntity.ok().body("회원이 정지되었습니다.");
	}

	// 회원 탈퇴
	@PostMapping("/delete")
	public ResponseEntity<String> deleteAccount() {
		try {
			authService.deleteAccount();
			return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
		} catch (RuntimeException e) {
			throw new CustomAuthenticationException("회원 탈퇴에 실패했습니다.");
		}
	}


}
