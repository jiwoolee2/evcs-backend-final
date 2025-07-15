package com.example.evcs.admin.rentCarInfo.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.evcs.admin.rentCarInfo.model.dto.RentCarInfoDTO;
import com.example.evcs.admin.rentCarInfo.model.dto.RentCarOptionDTO;
import com.example.evcs.admin.rentCarInfo.model.service.RentCarInfoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("/api/rentCar")
@RestController
@Slf4j
@RequiredArgsConstructor
public class RentCarInfoController {

	private final RentCarInfoService rentCarInfoService;
	
	@GetMapping("/category")
	public ResponseEntity<?> getRentCarCategory(){
		
		Map<String, Object> category = rentCarInfoService.getRentCarCategory();
		return ResponseEntity.status(HttpStatus.OK).body(category);
	}
	
	@GetMapping("/carInfo")
	public ResponseEntity<?> getAllCarInfo(){
		log.info("값이 들어오나요???");
		Map<String, Object> allCarInfo = rentCarInfoService.getAllCarInfo();
		return ResponseEntity.status(HttpStatus.OK).body(allCarInfo);
	}
	
	@PostMapping("/insert")
	public ResponseEntity<?> insertRentCar(@RequestBody RentCarInfoDTO rentCarInfo) {
		
		log.info("전달된 값 : {}",rentCarInfo);
		rentCarInfoService.insertRentCar(rentCarInfo);
		return ResponseEntity.status(HttpStatus.CREATED).body("추가완료~");
	}
	
	@GetMapping("/{currentPage}")
	public ResponseEntity<?> rentCarList(@PathVariable(name="currentPage") int currentPage,
		    @RequestParam(name = "useStatus", required = false) String useStatus,
		    @RequestParam(name = "category", required = false) String category,
		    @RequestParam(name = "searchKeyword", required = false) String searchKeyword
			) {
		log.info("currentPage : {} ", currentPage);
		Map <String, String> map = new HashMap();
		map.put("currentPage", String.valueOf(currentPage));
		map.put("useStatus", useStatus);
		map.put("category", category);
		map.put("searchKeyword", searchKeyword);
		
		log.info("RentCarInfoController rentCarList : {}",map);
		Map<String, Object> rentCarInfoMap = rentCarInfoService.rentCarList(map);
		
		log.info("뭐냐이거 {}", rentCarInfoMap);
		
		return ResponseEntity.status(HttpStatus.OK).body(rentCarInfoMap);
	}
	
	@PostMapping("/update")
	public ResponseEntity<?> updateRentCar(@RequestBody RentCarInfoDTO rentCarInfo){ 
		
		log.info("rentCarInfo : {} ", rentCarInfo);
		rentCarInfoService.updateRentCar(rentCarInfo);
		
		return ResponseEntity.status(HttpStatus.OK).body("차량 수정 완료");
	}
	
	@PostMapping("/delete")
	public ResponseEntity<?> deleteRentCar(@RequestBody RentCarInfoDTO rentCarInfo) {
		
		log.info("rentCarInfo : {} ", rentCarInfo);
		rentCarInfoService.deleteRentCar(rentCarInfo);
		return ResponseEntity.status(HttpStatus.OK).body("차량 삭제 완료");
	}
	
	@GetMapping("/timeRentCarInfo")
	public ResponseEntity<?> getTimeRentCarInfo(@RequestParam(name="startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
												@RequestParam(name="endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate){
		log.info("값이 들어오나요???");
		Map<String,Object> dateMap = new HashMap<>();
		dateMap.put("startDate", startDate);
		dateMap.put("endDate", endDate);
		Map<String, Object> allCarInfo = rentCarInfoService.getTimeRentCarInfo(dateMap);
		log.info("allCarInfo : {} " ,allCarInfo);
		return ResponseEntity.status(HttpStatus.OK).body(allCarInfo);
	}
	
	@GetMapping("/options")
	public ResponseEntity<List<RentCarOptionDTO>> getOptions(){
		List<RentCarOptionDTO> optionList = rentCarInfoService.getOptions();
		log.info("여기오냐?? {}" , optionList);
		return ResponseEntity.status(HttpStatus.OK).body(optionList);
	}
	
	@GetMapping("/rentCaroptions")
	public ResponseEntity<List<RentCarOptionDTO>> getRentCarOptions(@RequestParam("rentCarNo")String rentCarNo){
		log.info("rentCarNo : 이거맞어?? {}" ,rentCarNo);
		List<RentCarOptionDTO> optionList = rentCarInfoService.getRentCarOptions(rentCarNo);
		log.info("여기오냐?? {}" , optionList);
		return ResponseEntity.status(HttpStatus.OK).body(optionList);
	}
	
	
	
	
	
	
	
	
	
	
}
