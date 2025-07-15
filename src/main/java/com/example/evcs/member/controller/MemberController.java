package com.example.evcs.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.evcs.member.model.dto.ChangePasswordDTO;
import com.example.evcs.member.model.dto.MemberDTO;
import com.example.evcs.member.model.service.MemberService;
import com.nimbusds.oauth2.sdk.Response;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api/members")
@RestController
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

	@PostMapping
	public ResponseEntity<?> SignUp(@RequestBody @Valid MemberDTO member) {
		memberService.signUp(member);
		return ResponseEntity.status(200).build();
	}
	
	
	@PostMapping("/changePassword")
	public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordDTO changePasswordDTO, 
											@AuthenticationPrincipal UserDetails userDetails){
		
		String email = userDetails.getUsername();
		memberService.changePassword(changePasswordDTO, email);
		
		return ResponseEntity.ok("비밀번호 변경 성공");
	}


}
