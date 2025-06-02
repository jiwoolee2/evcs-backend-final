package com.example.evcs.driveRoute.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.example.evcs.driveRoute.model.dto.DRBoardDTO;
import com.example.evcs.driveRoute.model.vo.DRBoardVo;

@Mapper
public interface DRBoardMapper {

	// 1. 게시글 추가하기
	int insertBoard(DRBoardVo drBoardData);
	
	Long getBoardNo();
	void insertBoardFile(DRBoardVo boardFileData);
	void insertDriveRouteFile(DRBoardVo driveRouteFileData);
	List<DRBoardDTO> getAllBoard(RowBounds rowBounds);
	int countBoardByBoardNo(Long boardNo);

	// 게시글 수정
	int updateBoard(DRBoardVo drBoardData);
	
	void updateBoardFile(DRBoardVo boardFileData);
	void updateDriveRouteFile(DRBoardVo driveRouteFileData);
	
	// 게시글 삭제 상태바꾸기
	void deleteBoard(Long boardNo);
	
	List<String> getBoardImageUrl(Long boardNo);
	String getDriveRouteImageUrl(Long boardNo);
	
	// 게시글 좋아요 insert
	void boardLikes(DRBoardVo boardLikesData);
	// 게시글 좋아요 삭제
	void boardLikesCancel(DRBoardVo boardLikesCancelData);
	// 좋아요 조회
	List<DRBoardDTO> selectBoardLikes(Long boardWriter);







}
