package com.example.evcs.mail.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.evcs.mail.dto.EmailVerifyDTO;
import com.example.evcs.mail.dto.PassWordEmailVerifyDTO;
import com.example.evcs.mail.dto.PasswordUpdateDTO;
import com.example.evcs.mail.model.service.EmailService;
import com.example.evcs.member.model.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor
public class MailController {

	private final EmailService emailService;
	private final MemberService memberService;

	@PostMapping("/send")
	public ResponseEntity<String> sendVerificationCode(@RequestBody EmailVerifyDTO email) {
		emailService.sendVerificationCode(email);
		log.info("이메일 : {}", email);
		return ResponseEntity.ok("인증번호 전송 완료");
	}

	@PostMapping("/verify")
	public ResponseEntity<String> verifyCode(@RequestBody EmailVerifyDTO emailVerifyDTO) {
		emailService.verifyCode(emailVerifyDTO);
		return ResponseEntity.ok("인증 성공");
	}

	@PostMapping("/password-reset")
	public ResponseEntity<String> sendVerificationCode(@Valid @RequestBody PassWordEmailVerifyDTO passWordEmailVerifyDTO) {
		emailService.sendPassWordVerificationCode(passWordEmailVerifyDTO);
		log.info("이메일 : {}", passWordEmailVerifyDTO);
		return ResponseEntity.ok("인증번호 전송 완료");
	}

	@PostMapping("/password-verify")
	public ResponseEntity<String> verifyCode(@Valid @RequestBody PassWordEmailVerifyDTO passWordEmailVerifyDTO) {
			emailService.passwordVerifyCode(passWordEmailVerifyDTO);
			return ResponseEntity.ok("인증 성공");
			}

	@PostMapping("/password/update")
	public ResponseEntity<String> updatePassword(@Valid @RequestBody PasswordUpdateDTO passwordUpdateDTO) {
		memberService.updatePassword(passwordUpdateDTO.getEmail(), 
									 passwordUpdateDTO.getNewPassword(), 
									 passwordUpdateDTO.getConfirmPassword());
		return ResponseEntity.ok("비밀번호 변경 완료.");
	}

}
