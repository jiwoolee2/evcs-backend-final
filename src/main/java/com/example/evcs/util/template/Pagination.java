package com.example.evcs.util.template;

import com.example.evcs.util.model.dto.PageInfo;

public class Pagination {
	
	public static PageInfo getPageInfo(int currentPage,int pageSize,int boardNoPerPage ,int totalBoardNo) {
		// 현재 페이지, 하단에 보여질 페이지 개수, 한 페이지에 보여질 게시글 개수, 전체 게시글 개수
		
		int totalPages = (totalBoardNo+boardNoPerPage-1)/boardNoPerPage; // 전체 페이지 수
		int startPage = (currentPage/pageSize) *pageSize +1;	   // 하단 시작 페이지
		int endPage = startPage+pageSize-1;						   // 하단 마지막 페이지
		
		
		
		return PageInfo.builder()
						.currentPage(currentPage)
						.pageSize(pageSize)
						.boardNoPerPage(boardNoPerPage)
						.totalBoardNo(totalBoardNo)
						.totalPages(totalPages)
						.startPage(startPage)
						.endPage(endPage)
						.build();
	}
}
