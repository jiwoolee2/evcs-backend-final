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
	
	private final DRBoardService drBoardService;
	
	// 게시글 등록
	@PostMapping("/insert")
	public ResponseEntity<?> insertBoard(@ModelAttribute @Valid DRBoardDTO drBoard,
									     @RequestParam("boardFiles") MultipartFile[] boardFiles,
									     @RequestParam("drFile") MultipartFile drFile) {
		drBoardService.insertBoard(drBoard,boardFiles,drFile);
		
		return ResponseEntity.status(HttpStatus.CREATED).body("게시글이 등록되었습니다");
	}
	
	// 게시글 조회
	@GetMapping("/{currentPage}")
	public ResponseEntity<?> selectBoard(@PathVariable(name="currentPage") int currentPage) {
		
		log.info("currentPage : {}",currentPage);
		Map<String,Object> map = drBoardService.selectBoard(currentPage);
		log.info("map :{}",map);
		 return ResponseEntity.ok(map); 
	}
	
	// 게시글 수정
	@PostMapping("/update")
	public ResponseEntity<?> updateBoard(@ModelAttribute DRBoardDTO drBoard,
										 @RequestPart(value = "boardFiles", required = false) MultipartFile[] boardFiles,
									     @RequestParam("drFile") MultipartFile drFile) {
		log.info("drBoard : {},{} ,boardFiles : {}, drFile : {}",drBoard.getBoardContent(),drBoard.getBoardWriter(),boardFiles,drFile);
		drBoardService.updateBoard(drBoard,boardFiles,drFile);
		
		return ResponseEntity.status(HttpStatus.CREATED).body("게시글이 등록되었습니다");
	}

	// 게시글 삭제
	@DeleteMapping("/delete/{boardNo}")
	public ResponseEntity<?> deleteBoard(@PathVariable(name="boardNo") Long boardNo) {
		
		log.info("boardNo : {}",boardNo);
		drBoardService.deleteBoard(boardNo);
		 return ResponseEntity.ok("게시글이 삭제되었습니다."); 
	}

	
	
	@GetMapping("/likes/{boardNo}")
	public ResponseEntity<?> boardLikes(@PathVariable(name="boardNo") Long boardNo) {
	
		log.info("boardNo : {}",boardNo);
		drBoardService.boardLikes(boardNo);
		 return ResponseEntity.ok("좋아요 눌림"); 
	}
	
	@DeleteMapping("/likesCancel/{boardNo}")
	public ResponseEntity<?> boardLikesCancel(@PathVariable(name="boardNo") Long boardNo) {
		
		log.info("boardNo : {}",boardNo);
		drBoardService.boardLikesCancel(boardNo);
		 return ResponseEntity.ok("좋아요 취소"); 
	}
	
	@GetMapping("/selectLikes")
	public ResponseEntity<?> selectBoardLikes() {
		log.info("잘 들어오나요?");
		List<DRBoardDTO> boardLikesInfo = drBoardService.selectBoardLikes();
		return ResponseEntity.status(HttpStatus.CREATED).body(boardLikesInfo);
	}
	

}
