package com.example.evcs.event.controller;

import java.util.HashMap;
import java.util.List;
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
import org.springframework.web.multipart.MultipartFile;

import com.example.evcs.event.model.dto.EventDTO;
import com.example.evcs.event.model.service.EventService;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/admin-events")
public class EventController {

	private final EventService eventService;
	
	@PostMapping
	public ResponseEntity<?> insertEvent(EventDTO event, @RequestParam(name="file") MultipartFile file) {
		
		log.info("EventController insertEvent : EventDTO 값 확인 {} , file 값 확인 {}" , event, file);
		
		eventService.insertEvent(event, file);
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@GetMapping
	public ResponseEntity<Map<String, Object>> selctEventAll(
			@RequestParam (name="page", defaultValue="0") int page,
			@RequestParam (name ="category", required = false) String category,
			@RequestParam (name ="ingCategory", required = false) String ingCategory,
			@RequestParam (name ="searchKeyword", required = false) String searchKeyword
			){
		
		log.info("EventController selctEventAll : page : {} // category : {} // ingCategory : {} // searchKeyword : {}", page, category, ingCategory, searchKeyword);
		
		Map <String, String> map = new HashMap<String, String>();
		map.put("page", String.valueOf(page-1));
		map.put("category", category);
		map.put("ingCategory", ingCategory);
		map.put("searchKeyword", searchKeyword);
		
		Map<String, Object> returnMap = eventService.selctEventAll(map);
		
		log.info("returnMap : {}" , returnMap);
		return ResponseEntity.ok(returnMap);
	}
	
	@GetMapping("/{eventNo}")
	public ResponseEntity<EventDTO> selctByEventNo(@PathVariable(name="eventNo")@Min(value=1, message="값이 너무 작습니다.")Long eventNo){
		
		return ResponseEntity.ok(eventService.selectByEventNo(eventNo));
	}
	
	@PutMapping("/{eventNo}")
	public ResponseEntity<EventDTO> updateEvent(EventDTO event,@PathVariable("eventNo") Long eventNo, @RequestParam(name="file", required = false) MultipartFile file){
		event.setEventNo(eventNo);
		 
		log.info("EventController updateEvent : event : {} , file : {}", event,file);
		eventService.updateEvent(event, file);
		
	    return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/{eventNo}")
	public ResponseEntity<?> deleteByEventNo(@PathVariable(name = "eventNo") Long eventNo){
		
		eventService.deleteByEventNo(eventNo);
		return ResponseEntity.noContent().build();
	}
	
}
