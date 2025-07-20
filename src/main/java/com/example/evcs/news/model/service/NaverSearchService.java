package com.example.evcs.news.model.service;


import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.evcs.news.model.dto.NewsMainImageDto;
import com.example.evcs.news.model.dto.NewsMainResponseDto;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NaverSearchService {

    private final RestTemplate restTemplate;

    @Value("${naver.client.id}")
    private String clientId;

    @Value("${naver.client.secret}")
    private String clientSecret;

    @Value("${naver.api.url}")
    private String apiUrl;

    public NaverSearchService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public NewsMainResponseDto searchNews(String query) {
        if (query == null || query.trim().isEmpty()) {
            throw new IllegalArgumentException("검색어는 비어 있을 수 없습니다.");
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Naver-Client-Id", clientId);
            headers.set("X-Naver-Client-Secret", clientSecret);
            headers.setContentType(MediaType.APPLICATION_JSON);

            URI uri = UriComponentsBuilder
                    .fromUriString(apiUrl)
                    .path("/v1/search/news.json")
                    .queryParam("query", query)
                    .queryParam("display", 100)
                    .queryParam("sort", "sim")
                    .build()
                    .encode()
                    .toUri();

            log.info("Naver 요청 URI: {}", uri);

            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<NewsMainResponseDto> response = restTemplate.exchange(
                    uri, HttpMethod.GET, entity, NewsMainResponseDto.class
            );

            return response.getBody();

        } catch (HttpClientErrorException e) {
            log.error("Naver API 오류 응답: {}", e.getResponseBodyAsString());
            throw new IllegalArgumentException("Naver API 오류: " + e.getResponseBodyAsString());
        } catch (RestClientException e) {
            log.error("Naver API 네트워크 오류: {}", e.getMessage());
            throw new RuntimeException("Naver API 호출 실패");
        }
    }

    
    public NewsMainResponseDto searchNewsList(String query, String sort, int page, int size) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientSecret);
        headers.setContentType(MediaType.APPLICATION_JSON);

        int start = (page - 1) * size + 1;

        URI uri = UriComponentsBuilder
                .fromUriString(apiUrl)
                .path("/v1/search/news.json")
                .queryParam("query", query)
                .queryParam("display", size)
                .queryParam("start", start)
                .queryParam("sort", sort)
                .build()
                .encode()
                .toUri();

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<NewsMainResponseDto> response = restTemplate.exchange(
                uri, HttpMethod.GET, entity, NewsMainResponseDto.class);
        return response.getBody();
    }
    
    public NewsMainImageDto searchImage(String title) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Naver-Client-Id", clientId);
            headers.set("X-Naver-Client-Secret", clientSecret);
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);

            URI uri = UriComponentsBuilder
                    .fromUriString(apiUrl)
                    .path("/v1/search/image.json")
                    .queryParam("query", title)
                    .queryParam("display", 100)
                    .queryParam("sort", "date")
                    .build()
                    .encode()
                    .toUri();

            log.info("요청 URI: {}", uri.toString()); // 요청 URI 로깅

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<NewsMainImageDto> response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    entity,
                    NewsMainImageDto.class // 여기서 반환 타입을 변경!
            );

            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("네이버 이미지 검색 중 오류 발생: " + e.getMessage());
        }
    }
}
