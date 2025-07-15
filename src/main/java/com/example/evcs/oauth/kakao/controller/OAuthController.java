package com.example.evcs.oauth.kakao.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.evcs.oauth.kakao.model.dto.LoginMemberDTO;
import com.example.evcs.oauth.kakao.model.service.OAuthService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OAuthController {

	private final OAuthService oauthService;

	@Value("${kakao.auth.client}")
	private String clientId; // 카카오 REST API 키

	@Value("${kakao.auth.redirect}")
	private String redirectUri; // 카카오 인증 후 리디렉션 URI

	// 카카오 로그인 페이지로 리디렉션하는 엔드포인트
	@GetMapping("/auth/login/kakao")
	public void kakaoLogin(HttpServletResponse response) throws IOException {
		String kakaoLoginUrl = "https://kauth.kakao.com/oauth/authorize?"
				+ "client_id=" + clientId 
				+ "&redirect_uri=" + URLEncoder.encode(redirectUri, "UTF-8") 
				+ "&response_type=code"
				+ "&scope=account_email,profile_nickname,profile_image"
				+ "&prompt=login";

		// 카카오 로그인 페이지로 리디렉션
		response.sendRedirect(kakaoLoginUrl);
	}
	
	

	@GetMapping("/auth/kakao/callback")
	public void kakaoCallback(@RequestParam("code") String accessCode, HttpServletResponse response) {
	    try {
	        LoginMemberDTO loginMember = oauthService.oAuthLogin(accessCode, response); // 토큰은 여기서 받아옴

	        // 1. 토큰을 HttpOnly 쿠키로 설정
	        Cookie accessTokenCookie = new Cookie("access_token", loginMember.getAccessToken());
	        accessTokenCookie.setHttpOnly(true);
	        accessTokenCookie.setSecure(true); // 개발 시엔 false, 배포 시엔 true
	        accessTokenCookie.setPath("/");
	        accessTokenCookie.setMaxAge(60 * 60); // 1시간
	        accessTokenCookie.setAttribute("SameSite", "None"); // <-- 추가!

	        Cookie refreshTokenCookie = new Cookie("refresh_token", loginMember.getRefreshToken());
	        refreshTokenCookie.setHttpOnly(true);
	        refreshTokenCookie.setSecure(true);
	        refreshTokenCookie.setPath("/");
	        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60);
	        refreshTokenCookie.setAttribute("SameSite", "None");

	        response.addCookie(accessTokenCookie);
	        response.addCookie(refreshTokenCookie);

	        // 2. 로그인 후 프론트로 리다이렉트 (필요한 정보만 포함)
	        String redirectUrl = UriComponentsBuilder
	                .fromUriString("http://localhost:5173/auth/kakao/callback")
	                .queryParam("nickname", URLEncoder.encode(loginMember.getNickname(), StandardCharsets.UTF_8))
	                .queryParam("email", loginMember.getEmail())
	                .queryParam("memberNo", loginMember.getMemberNo())
	                .queryParam("accessToken", loginMember.getAccessToken())
	                .queryParam("refreshToken", loginMember.getRefreshToken())
	                .build()
	                .toUriString();

	        response.sendRedirect(redirectUrl);
	    } catch (Exception e) {
	        log.error("카카오 로그인 실패", e);
	        try {
	            response.sendRedirect("http://localhost:5173/login?error=1");
	        } catch (IOException ioException) {
	            log.error("리다이렉트 실패", ioException);
	        }
	    }
	}



}
