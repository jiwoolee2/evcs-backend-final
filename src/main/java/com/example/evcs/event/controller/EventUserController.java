package com.example.evcs.event.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.evcs.event.model.service.EventService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/api/user-events")
public class EventUserController {
	
	private final EventService eventService;
	
	@GetMapping
	public ResponseEntity<Map<String, Object>> selctEventAll(
			@RequestParam (name="page", defaultValue="0") int page
			){
		
		log.info("EventUserController selctEventAll : page : {} ", page);
		
		page--;
		
		Map<String, Object> returnMap = eventService.selctEventAllUser(page);
		
		log.info("returnMap : {}" , returnMap);
		return ResponseEntity.ok(returnMap);
	}
	
}
