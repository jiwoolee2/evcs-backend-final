package com.example.evcs.admin.hotdeal.controller;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.evcs.admin.hotdeal.model.dto.HotdealDTO;
import com.example.evcs.admin.hotdeal.model.service.HotdealService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/admin-hotdeals")
public class HodealController {
	
	private final HotdealService hotdealService; 
	
	@PostMapping
	public ResponseEntity<?> insertHotdeal(@RequestBody HotdealDTO hotdeal) {
		
		log.info("HotdealController insertHotdeal : HotdealDTO 값 확인 {} " , hotdeal);
		
		hotdealService.insertHotdeal(hotdeal);
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@GetMapping
	public ResponseEntity<Map<String, Object>> selectHotdealAll(
			@RequestParam (name="page", defaultValue="0") int page,
			@RequestParam (name ="ing", required = false) String ing,
			@RequestParam (name ="startDate", required = false) String startDate,
			@RequestParam (name ="endDate", required = false) String endDate,
			@RequestParam (name ="searchKeyword", required = false) String searchKeyword
			){
		
		log.info("HotdealController selctHotdealAll : page : {} // ing : {} // startDate : {} // endDate : {} // searchKeyword : {} ", page , ing, startDate, endDate, searchKeyword);
		
		Map <String, String> map = new HashMap<String, String>();
		map.put("page", String.valueOf(page-1));
		map.put("ing", ing);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("searchKeyword", searchKeyword);
		
		Map<String, Object> returnMap = hotdealService.selectHotdealAll(map);
		
		log.info("returnMap : {}" , returnMap);
		return ResponseEntity.ok(returnMap);
	}
	
	@GetMapping("/cars")
	public ResponseEntity<Map<String, Object>> selectAllRentCar(
			@RequestParam (name ="useStatus", required = false) String useStatus,
			@RequestParam (name ="carCategory", required = false) String carCategory,
			@RequestParam (name ="searchCategory", required = false) String searchCategory,
			@RequestParam (name ="searchKeyword", required = false) String searchKeyword,
			@RequestParam (name ="hotdealNo", required = false) String hotdealNo
			){
		log.info("HotdealController selectAllRentCar : useStatus : {} // carCategory : {} // searchCategory : {} // searchKeyword : {} // hotdealNo : {} "
				, useStatus , carCategory, searchCategory, searchKeyword, hotdealNo);
		
		Map <String, String> map = new HashMap<String, String>();
		map.put("useStatus", useStatus);
		map.put("carCategory", carCategory);
		map.put("searchCategory", searchCategory);
		map.put("searchKeyword", searchKeyword);
		map.put("hotdealNo", hotdealNo);
		
		Map<String, Object> returnMap = hotdealService.selectAllRentCar(map);
		
		log.info("returnMap : {}" , returnMap);
		
		return ResponseEntity.ok(returnMap);
	}

	@GetMapping("/hotdealCars")
	public ResponseEntity<Map<String, Object>> selectAllHotdealRentCar(
			@RequestParam (name ="useStatus", required = false) String useStatus,
			@RequestParam (name ="carCategory", required = false) String carCategory,
			@RequestParam (name ="searchCategory", required = false) String searchCategory,
			@RequestParam (name ="searchKeyword", required = false) String searchKeyword,
			@RequestParam (name ="hotdealNo", required = false) String hotdealNo
			){
		log.info("HotdealController selectAllHotdealRentCar : useStatus : {} // carCategory : {} // searchCategory : {} // searchKeyword : {} // hotdealNo : {} "
				, useStatus , carCategory, searchCategory, searchKeyword, hotdealNo);
		
		Map <String, String> map = new HashMap<String, String>();
		map.put("useStatus", useStatus);
		map.put("carCategory", carCategory);
		map.put("searchCategory", searchCategory);
		map.put("searchKeyword", searchKeyword);
		map.put("hotdealNo", hotdealNo);
		
		Map<String, Object> returnMap = hotdealService.selectAllHotdealRentCar(map);
		
		log.info("returnMap : {}" , returnMap);
		
		return ResponseEntity.ok(returnMap);
	}
	
	@PutMapping("/{hotdealNo}")
	public ResponseEntity<HotdealDTO> updateHotdeal(@RequestBody HotdealDTO hotdeal,@PathVariable("hotdealNo") Long hotdealNo){
		hotdeal.setHotdealNo(hotdealNo);
		 
		log.info("HotdealController updateHotdeal : hotdeal : {} ", hotdeal);
		hotdealService.updateHotdeal(hotdeal);
		
	    return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/{hotdealNo}")
	public ResponseEntity<?> deleteByHotdealNo(@PathVariable(name = "hotdealNo") Long hotdealNo){
		
		hotdealService.deleteByHotdealNo(hotdealNo);
		return ResponseEntity.noContent().build();
	}
	
}
