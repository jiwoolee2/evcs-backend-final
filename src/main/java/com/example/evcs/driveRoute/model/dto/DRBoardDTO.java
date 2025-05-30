package com.example.evcs.driveRoute.model.dto;

import java.sql.Date;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Valid
public class DRBoardDTO {

	    private Long boardNo;
	    private Long boardWriter;
	    @NotBlank(message = "내용은 비워둘 수 없습니다.")
	    @Size(max = 200, min = 5, message = "내용은 5자 이상, 200자 이하로 작성해주세요.")
	    private String boardContent;
	    private Date createDate;
	    private String status;
	    private List<DRBoardImageDTO> drBoardImage;
	    private DriveRouteImageDTO driveRouteImage;
	    
	    private String memberNickName;
	    private int likeCount;
}
