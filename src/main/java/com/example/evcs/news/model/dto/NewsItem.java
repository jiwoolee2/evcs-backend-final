package com.example.evcs.news.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewsItem {
	
	    private String title;
	    private String originallink;
	    private String link; // 네이버 뉴스 링크
	    private String description;
	    private String pubDate;  // 🆕 뉴스 기사 발행 날짜 추가 
}
