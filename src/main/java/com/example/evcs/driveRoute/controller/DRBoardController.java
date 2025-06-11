package com.example.evcs.driveRoute.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.evcs.common.model.dto.ResponseData;
import com.example.evcs.common.response.ResponseUtil;
import com.example.evcs.driveRoute.model.dto.DRBoardDTO;
import com.example.evcs.driveRoute.model.service.DRBoardService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/driveRouteBoard")
@RequiredArgsConstructor
public class DRBoardController {
	
	private final ResponseUtil responseUtil;
	private final DRBoardService drBoardService;
	
	// 게시글 등록
	@PostMapping("/insert")
	public ResponseEntity<?> insertBoard(@ModelAttribute @Valid DRBoardDTO drBoard,
									     @RequestParam("boardFiles") MultipartFile[] boardFiles,
									     @RequestParam("drFile") MultipartFile drFile) {
		drBoardService.insertBoard(drBoard,boardFiles,drFile);
		ResponseData data = responseUtil.getResponseData("게시물이 등록되었습니다.","200");
		return ResponseEntity.ok(data);
	}
	
	// 게시글 조회
	@GetMapping("/{currentPage}")
	public ResponseEntity<?> selectBoard(@PathVariable(name="currentPage") int currentPage) {
		
		log.info("currentPage : {}",currentPage);
		Map<String,Object> map = drBoardService.selectBoard(currentPage);
		ResponseData data = responseUtil.getResponseData(map,"게시물이 조회되었습니다.","200");
				
		return ResponseEntity.ok(data);
	}
	
	// 게시글 수정
	@PostMapping("/update")
	public ResponseEntity<?> updateBoard(@ModelAttribute DRBoardDTO drBoard,
										 @RequestPart(value = "boardFiles", required = false) MultipartFile[] boardFiles,
									     @RequestParam("drFile") MultipartFile drFile) {
		log.info("drBoard : {}",drBoard);
		drBoardService.updateBoard(drBoard,boardFiles,drFile);
		  ResponseData data = responseUtil.getResponseData("게시물이 수정되었습니다.", "200");
		    return ResponseEntity.ok(data); 
	}
	

	// 게시글 삭제
	@DeleteMapping("/delete/{boardNo}")
	public ResponseEntity<?> deleteBoard(@PathVariable(name="boardNo") Long boardNo) {
		
		log.info("boardNo : {}",boardNo);
		drBoardService.deleteBoard(boardNo);
		ResponseData data = responseUtil.getResponseData("게시물이 삭제되었습니다.","200");
		return ResponseEntity.ok(data);
	}

	@GetMapping("/likes/{boardNo}")
	public ResponseEntity<?> boardLikes(@PathVariable(name="boardNo") Long boardNo) {
	
		log.info("boardNo : {}",boardNo);
		drBoardService.boardLikes(boardNo);
		ResponseData data = responseUtil.getResponseData("좋아요 누름","200");
		 return ResponseEntity.ok(data); 
	}
	
	@DeleteMapping("/likesCancel/{boardNo}")
	public ResponseEntity<?> boardLikesCancel(@PathVariable(name="boardNo") Long boardNo) {
		
		log.info("boardNo : {}",boardNo);
		drBoardService.boardLikesCancel(boardNo);
		ResponseData data = responseUtil.getResponseData("좋아요 취소","200");
		 return ResponseEntity.ok(data); 
	}
	
	@GetMapping("/selectLikes")
	public ResponseEntity<?> selectBoardLikes() {
		List<DRBoardDTO> boardLikesInfo = drBoardService.selectBoardLikes();
		ResponseData data = responseUtil.getResponseData(boardLikesInfo,"게시물 좋아요 조회","201");
		return ResponseEntity.ok(data);
	}
	

	

}
