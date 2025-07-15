package com.example.evcs.admin.garage.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.evcs.admin.garage.model.dto.GarageDTO;
import com.example.evcs.admin.garage.model.service.GarageService;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/admin-garages")
public class GarageController {
	private final GarageService garageService;
	
	@PostMapping
	public ResponseEntity<?> insertGarage(GarageDTO garage) {
		
		log.info("GarageController insertGarage : GarageDTO 값 확인 {} " , garage);
		
		garageService.insertGarage(garage);
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@GetMapping
	public ResponseEntity<Map<String, Object>> selectGarageAll(
			@RequestParam (name ="regionSido", required = false) String regionSido,
			@RequestParam (name ="regionSigungu", required = false) String regionSigungu,
			@RequestParam (name ="regionDong", required = false) String regionDong,
			@RequestParam (name ="searchKeyword", required = false) String searchKeyword,
			@RequestParam (name ="status", required = false) String status,
			@RequestParam (name ="searchCategory", required = false) String searchCategory
			){
		
		log.info("GarageController selctGarageAll : regionSido : {} // regionSigungu : {} // regionDong : {} // searchKeyword : {} // status : {}",
				regionSido, regionSigungu, regionDong, searchKeyword, status,searchCategory);
		
		Map <String, String> map = new HashMap<String, String>();
		map.put("regionSido", regionSido);
		map.put("regionSigungu", regionSigungu);
		map.put("regionDong", regionDong);
		map.put("searchKeyword", searchKeyword);
		map.put("status", status);
		map.put("searchCategory", searchCategory);
		
		Map<String, Object> returnMap = garageService.selectGarageAll(map);
		
		log.info("returnMap : {}" , returnMap);
		return ResponseEntity.ok(returnMap);
	}
	
	@GetMapping("/{garageNo}")
	public ResponseEntity<GarageDTO> selctByGarageNo(@PathVariable(name="garageNo")@Min(value=1, message="값이 너무 작습니다.")Long garageNo){
		
		return ResponseEntity.ok(garageService.selectByGarageNo(garageNo));
	}
	
	@PutMapping("/{garageNo}")
	public ResponseEntity<GarageDTO> updateGarage(GarageDTO garage,@PathVariable("garageNo") Long garageNo){
		garage.setGarageNo(garageNo);
		 
		log.info("GarageController updateGarage : garage : {} ", garage);
		garageService.updateGarage(garage);
		
	    return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/{garageNo}")
	public ResponseEntity<?> deleteByGarageNo(@PathVariable(name = "garageNo") Long garageNo){
		
		garageService.deleteByGarageNo(garageNo);
		return ResponseEntity.noContent().build();
	}
	
}
