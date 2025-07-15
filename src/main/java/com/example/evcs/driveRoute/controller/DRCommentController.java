package com.example.evcs.driveRoute.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.evcs.driveRoute.model.dto.DRCommentDTO;
import com.example.evcs.driveRoute.model.service.DRCommentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/driveRouteComment")
@RequiredArgsConstructor
public class DRCommentController {

	private final DRCommentService drCommentService;
	
	@PostMapping("/insert")
	public ResponseEntity<?> insertComment(@RequestBody DRCommentDTO drComment) {
		
		log.info("DRComment : {}",drComment);
		drCommentService.insertComment(drComment);
		return ResponseEntity.status(HttpStatus.CREATED).body("조회성공");
	}
	
	@GetMapping("{boardNo}/{currentCommentPage}")
	public ResponseEntity<?> selectComment(@PathVariable(name="currentCommentPage") 
													int currentCommentPage,
										   @PathVariable(name="boardNo") Long boardNo) {
		log.info("현재 페이지 번호 : {} ",currentCommentPage);
		Map<String, Object> map =  drCommentService.selectComment(boardNo,currentCommentPage);
		return ResponseEntity.status(HttpStatus.CREATED).body(map);
	}
	
	@DeleteMapping("/delete/{commentNo}")
	public ResponseEntity<?> deleteComment(@PathVariable(name="commentNo") Long commentNo) {
		
		log.info("boardNo : {}",commentNo);
		drCommentService.deleteComment(commentNo);
		return ResponseEntity.ok("댓글이 삭제되었습니다."); 
	}
	
	@PutMapping("/update")
	public ResponseEntity<?> updateComment(@RequestBody DRCommentDTO drComment) {
		
		log.info("drComment : {}", drComment);
		drCommentService.updateComment(drComment);
		return ResponseEntity.ok("댓글이 수정되었습니다."); 
	}

	
}
