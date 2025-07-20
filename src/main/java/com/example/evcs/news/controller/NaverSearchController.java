package com.example.evcs.news.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.evcs.news.model.dto.NewsMainImageDto;
import com.example.evcs.news.model.dto.NewsMainResponseDto;
import com.example.evcs.news.model.service.NaverSearchService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class NaverSearchController {

    private final NaverSearchService naverSearchService;

    public NaverSearchController(NaverSearchService naverSearchService) {
        this.naverSearchService = naverSearchService;
    }

    @GetMapping("/naver-news")
    public ResponseEntity<?> searchNews(@RequestParam("query") String query) {
        try {
            NewsMainResponseDto response = naverSearchService.searchNews(query);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("서버 내부 오류");
        }
    }
    

    @GetMapping("/naver-news-list")
    public ResponseEntity<NewsMainResponseDto> searchNewsList(
            @RequestParam(name = "query") String query,
            @RequestParam(name = "sort", defaultValue = "sim") String sort,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        try {
            NewsMainResponseDto dto = naverSearchService.searchNewsList(query, sort, page, size);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    
    
    @GetMapping("/naver-image")  // 🆕 API 엔드포인트 변경
    public ResponseEntity<NewsMainImageDto> searchImage(@RequestParam("query") String query) {
        try {
        	NewsMainImageDto response = naverSearchService.searchImage(query);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
        	e.printStackTrace(); // 실제 콘솔 로그로 에러 내용 확인
            return ResponseEntity.internalServerError().body(null);
        }
    }
}
