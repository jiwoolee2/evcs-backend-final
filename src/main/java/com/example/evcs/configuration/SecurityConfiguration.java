package com.example.evcs.configuration;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.evcs.configuration.filter.JwtFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {
	
	private final JwtFilter filter;
	
	public static final String[] ALLOW_URLS = {
		    "/swagger-ui/**",
		    "/swagger-resources/**",
		    "/v3/api-docs/**",
		    "/auth/login",
		    "/auth/login/kakao/**",
		    "/auth/kakao/callback",
		    "/members",
		    "/members/**",
		    "/api/**"
		};

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable)
            .cors(Customizer.withDefaults())

            // 1) news 관련 API 권한 설정
            .authorizeHttpRequests(auth -> auth
                // — 비회원 접근 허용
                .requestMatchers(HttpMethod.GET,
                    "/api/naver-news",
                    "/api/naver-image",
                    "/api/naver-news-list",
                    "/api/news/categories",
                    "/api/admin/news/list",
                    "/api/admin/news/category/all",
                    "/api/news/comment/list",
                    "/api/news/like",
                    "/api/news/hate"
                ).permitAll()
                .requestMatchers(HttpMethod.POST,
                    "/api/news/detail"
                ).permitAll()

                // — 회원 전용
                .requestMatchers(HttpMethod.POST,
                    "/api/news/like",
                    "/api/news/hate",
                    "/api/news/bookmark",
                    "/api/news/comment",
                    "/api/news/comment/like",
                    "/api/news/comment/hate",
                    "/api/report/comment",
                    "/api/usReports/**",
                    "/api/usReportsCom/**",
                    "/api/integrated-reports"
                ).authenticated()
                .requestMatchers(HttpMethod.PUT,
                    "/api/news/comment"
                ).authenticated()
                .requestMatchers(HttpMethod.PATCH,
                    "/api/usReportsCom/**",
                    "/api/usReports/**"
                ).authenticated()
                .requestMatchers(HttpMethod.DELETE,
                    "/api/news/comment/**"
                ).authenticated()
                .requestMatchers(HttpMethod.GET,
                    "/api/news/bookmark/status",
                    "/api/news/like/status",
                    "/api/news/hate/status",
                    "/api/news/mypage/**",
                    "/api/usReports/**",
                    "/api/usReportsCom/**"
                ).authenticated()

                // — 관리자 전용
                .requestMatchers("/api/admin/**", "/api/reports/**", "/api/admin/management/**").hasRole("ADMIN")
                .requestMatchers("/api/admin/**", "/api/reports/**", "/api/amReportsCom/**").hasRole("ADMIN")

                // 소셜 관련 
                .requestMatchers(ALLOW_URLS).permitAll()

                // 2) 그 외 모든 요청(News 외 서비스)은 모두 허용
                .anyRequest().permitAll()
            )

            // stateless JWT
            .sessionManagement(m -> m.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowedOrigins(List.of("*"));
        cfg.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
        cfg.setAllowedHeaders(List.of("*"));
  
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
