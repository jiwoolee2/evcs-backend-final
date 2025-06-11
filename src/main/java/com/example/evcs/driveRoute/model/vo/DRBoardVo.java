package com.example.evcs.driveRoute.model.vo;

import java.sql.Date;

import lombok.Builder;
import lombok.ToString;
import lombok.Value;

@Value
@Builder
@ToString
public class DRBoardVo {

	private Long boardNo;
	private Long boardWriter;
	private String memberNickName;
	private String boardContent;
	private Date createDate;
	private String status;
	private String driveRouteImage;
	private String boardImage;
}
