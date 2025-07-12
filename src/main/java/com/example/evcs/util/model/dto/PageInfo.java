package com.example.evcs.util.model.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PageInfo {

	
	private int currentPage;    // 현재 페이지 번호
	private int totalPages;     // 전체 페이지 수
	private int pageSize;       // 한 화면에 보여질 페이지 수 (추가로 필요할 경우)
	private int startPage;      // 시작 페이지 번호
	private int endPage;        // 끝 페이지 번호
	private int boardNoPerPage;   // 한 페이지에 보여질 게시글 수
	private int totalBoardNo;     // 전체 게시글 수
}
