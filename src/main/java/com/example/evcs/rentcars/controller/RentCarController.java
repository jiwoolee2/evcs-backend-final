package com.example.evcs.rentcars.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.evcs.admin.rentCarInfo.model.dto.RentCarInfoDTO;
import com.example.evcs.rentcars.model.service.RentCarService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/user-rentcars")
public class RentCarController {
	
	private final RentCarService rentCarService; 

	@GetMapping("/category/{category}")
	public ResponseEntity<List<RentCarInfoDTO>> selectAllRentCar(@PathVariable("category") int category){
		
		List<RentCarInfoDTO> list = rentCarService.selectAllRentCar(category);
		
		log.info("returnList : {}" , list);
		return ResponseEntity.ok(list);
	}

	@GetMapping("/{rentCarNo}")
	public ResponseEntity<RentCarInfoDTO> selectRentCarByRentCarNo(@PathVariable("rentCarNo") String rentCarNo){
	
		RentCarInfoDTO rentCar = rentCarService.selectRentCarByRentCarNo(rentCarNo);
		
		log.info("returnList : {}" , rentCar);
		return ResponseEntity.ok(rentCar);
	}
	
}
