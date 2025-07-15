package com.example.evcs.admin.reservation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.evcs.admin.reservation.model.dto.RevDTO;
import com.example.evcs.admin.reservation.model.service.RevService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/reservation")
@RequiredArgsConstructor
@Slf4j
public class RevController {

	private final RevService revService;
	
	@PostMapping("/insert")
	public ResponseEntity<?> insertRev(@RequestBody RevDTO reservation){
		log.info("reservation : {}",reservation);
		
		revService.insertRev(reservation);
		return null;
	}
}
