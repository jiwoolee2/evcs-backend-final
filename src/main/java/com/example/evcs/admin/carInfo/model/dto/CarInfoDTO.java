package com.example.evcs.admin.carInfo.model.dto;


import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CarInfoDTO {
	
	private int carNo;
	private String carName;
	private Long colorNo;
	private String colorName;
	private String carType;
	private String carTypeName;
	private String companyName;  
	private int carYear;
	private String carCompany;
	private int carBattery;
	private Date enrollDate;
	private Long companyNo;
	private Long carTypeNo;
	private String returnEnrollDate;
	private String fileLoad;
	
}
