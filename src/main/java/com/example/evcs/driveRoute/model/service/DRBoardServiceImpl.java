package com.example.evcs.driveRoute.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.evcs.auth.model.vo.CustomUserDetails;
import com.example.evcs.auth.service.AuthServiceImpl;
import com.example.evcs.common.model.service.S3Service;
import com.example.evcs.driveRoute.model.dao.DRBoardMapper;
import com.example.evcs.driveRoute.model.dto.DRBoardDTO;
import com.example.evcs.driveRoute.model.vo.DRBoardVo;
import com.example.evcs.exception.NoFileException;
import com.example.evcs.exception.NonExistingException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class DRBoardServiceImpl implements DRBoardService {
	
	private final AuthServiceImpl authServiceImpl;
	private final DRBoardMapper drBoardMapper;
	private final S3Service s3Service;
	private String fileLocation;
	
	// 추가
	@Override
	public void insertBoard(DRBoardDTO drBoard, MultipartFile[] boardFiles, MultipartFile drFile) {
		
		
		handleException(drBoard,boardFiles,drFile); // 예외처리
		DRBoardVo drBoardData = getBoardVo(drBoard); // Vo객체에 담음
		int result = drBoardMapper.insertBoard(drBoardData); // 게시글 추가
		if(result == 1) {
			insertMultiFile(boardFiles,drBoardData);
			insertDriveRouteFile(drFile,drBoardData);
		}
	}
	
	private Long getMemberNo() {
		CustomUserDetails user = authServiceImpl.getUserDetails();
		return user.getMemberNo();
	}
	
	private void handleException(DRBoardDTO drBoard, MultipartFile[] boardFiles, 
															MultipartFile drFile) {
		Long memberNo = getMemberNo();
		if(memberNo == null) {
			throw new NonExistingException("존재하지 않는 회원입니다.");
		}
		for(MultipartFile file: boardFiles) {
			if(file == null || file.isEmpty()) {
				throw new NoFileException("이미지가 존재하지 않습니다.");
			}
		}
		if(drFile == null || drFile.isEmpty()) {
			throw new NoFileException("드라이브 경로를 선택해주세요.");
		}
	}
	
	private DRBoardVo getBoardVo(DRBoardDTO drBoard) {
		Long memberNo = getMemberNo();
		DRBoardVo drBoardData = DRBoardVo.builder()
				 .boardWriter(memberNo)
				 .boardContent(drBoard.getBoardContent())
				 .build();
		return drBoardData; 
	}
	
	private void insertMultiFile(MultipartFile[] boardFiles,DRBoardVo drBoardData) {
		Long boardNo = drBoardData.getBoardNo();
		fileLocation = "board-image/";
		
		for(MultipartFile file: boardFiles) {
			String boardFilePath = s3Service.uploadFile(file,fileLocation);
			DRBoardVo boardFileData = DRBoardVo.builder()
											   .boardNo(boardNo)
											   .boardImage(boardFilePath)
											   .build();
			drBoardMapper.insertBoardFile(boardFileData);
		}
	}
	
	private void insertDriveRouteFile(MultipartFile drFile, DRBoardVo drBoardData) {
		Long boardNo = drBoardData.getBoardNo();
		fileLocation = "driveroute-map-image/";
		
		String driveRouteFilePath = s3Service.uploadFile(drFile,fileLocation);
		DRBoardVo driveRouteFileData = DRBoardVo.builder()
										   .boardNo(boardNo)
										   .driveRouteImage(driveRouteFilePath)
										   .build();
		drBoardMapper.insertDriveRouteFile(driveRouteFileData);
	}
	

	// 조회
	@Override
	public Map<String, Object> selectBoard(int currentPage) {
		
		Map<String,Object> map = new HashMap();
		
		int boardPerPage = 10;
		RowBounds rowBounds = new RowBounds(0,boardPerPage*currentPage);
		List<DRBoardDTO> drBoard = drBoardMapper.getAllBoard(rowBounds);
		map.put("drBoard", drBoard);
		return map;
	}
	
	
	// 수정
	@Override
	public void updateBoard(DRBoardDTO drBoard, MultipartFile[] boardFiles, MultipartFile drFile) {
		handleException2(drBoard,drFile);
	    DRBoardVo drBoardData = getBoardVo(drBoard);
	    int result = drBoardMapper.updateBoard(drBoardData);

	    if (result == 1) {
	    	updateBoardFile(drBoard,boardFiles);
	        insertDriveRouteFile(drFile,drBoardData);
	   
	    }
	}
	
	private void updateBoardFile(DRBoardDTO drBoard,MultipartFile[] boardFiles) {
		fileLocation = "driveroute-map-image/";
		if (boardFiles != null && boardFiles.length > 0) {
            for (MultipartFile file : boardFiles) {
                if (file != null && !file.isEmpty()) {
                    String boardFilePath = s3Service.uploadFile(file,fileLocation);
                    DRBoardVo boardFileData = DRBoardVo.builder()
                            .boardNo(drBoard.getBoardNo())
                            .boardImage(boardFilePath)
                            .build();
                    drBoardMapper.updateBoardFile(boardFileData);
                }
            }
        }
	}
	
	private void handleException2(DRBoardDTO drBoards, MultipartFile drFile) {
		Long memberNo = getMemberNo();
		if(memberNo == null) {
			throw new NonExistingException("존재하지 않는 회원입니다.");
		}
		if(drFile == null || drFile.isEmpty()) {
			throw new NoFileException("드라이브 경로를 선택해주세요.");
		}
	}
	
	// 삭제
	@Override
	public void deleteBoard(Long boardNo) {
		int countBoardResult = countByBoardNo(boardNo);
		if(countBoardResult==0) {
			throw new NonExistingException("존재하지 않는 게시글입니다.");
		} else {
			drBoardMapper.deleteBoard(boardNo);
			deleteBoardImage(boardNo);
			deleteDriveRouteImage(boardNo);
		}
	}
	
	private void deleteBoardImage(Long boardNo) {
		List<String> boardImageUrl = getBoardImageUrl(boardNo);
		for(String url : boardImageUrl) {
			s3Service.deleteFile(url);
		}
	}
	
	private void deleteDriveRouteImage(Long boardNo) {
		String driveRouteImageUrl = getDriveRouteImageUrl(boardNo);
		s3Service.deleteFile(driveRouteImageUrl);
	}
	
	private int countByBoardNo(Long boardNo) {
		int countBoardResult = drBoardMapper.countBoardByBoardNo(boardNo);
		return countBoardResult;
	}

	private List<String> getBoardImageUrl(Long boardNo) {
		List<String> boardImageUrl = drBoardMapper.getBoardImageUrl(boardNo);
		return boardImageUrl;
	}
	
	private String getDriveRouteImageUrl(Long boardNo) {
		String driveRouteImageUrl = drBoardMapper.getDriveRouteImageUrl(boardNo);
		return driveRouteImageUrl;
	}
	

	
	
	
	
	@Override
	public void boardLikes(Long boardNo) {
		Long memberNo = getMemberNo();
		DRBoardVo boardLikesData = DRBoardVo.builder()
											 .boardWriter(memberNo)
											 .boardNo(boardNo)
											 .build();
		drBoardMapper.boardLikes(boardLikesData);
	}

	@Override
	public void boardLikesCancel(Long boardNo) {
		Long memberNo = getMemberNo();
		
		DRBoardVo boardLikesCancelData = DRBoardVo.builder()
											 .boardWriter(memberNo)
											 .boardNo(boardNo)
											 .build();
		drBoardMapper.boardLikesCancel(boardLikesCancelData);
	}

	@Override
	public List<DRBoardDTO> selectBoardLikes() {
		Long boardWriter = getMemberNo();
		List<DRBoardDTO> boardLikesInfo = drBoardMapper.selectBoardLikes(boardWriter);
		log.info("boardLikesInfo:{}",boardLikesInfo);
		return boardLikesInfo;
	}

	

	
}



































