package org.ssglobal.training.codes.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ssglobal.training.codes.dto.AppDTO;
import org.ssglobal.training.codes.request.AppRequest;
import org.ssglobal.training.codes.response.Response;
import org.ssglobal.training.codes.service.ApplicationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lms")
public class ApplicationController {
	private final ApplicationService appService;
	
	@GetMapping("/applications")
	public ResponseEntity<List<AppDTO>> getAllApplications() {
		return ResponseEntity.ok(appService.getAllApplications());
	}
	
	@GetMapping("/application/{id}")
	public ResponseEntity<AppDTO> getApplicationById(@PathVariable("id") Integer appId) {
		return ResponseEntity.ok(appService.getApplicationById(appId));
	}
	
	@PostMapping("/application")
	public ResponseEntity<Response> addApplication(@RequestBody AppRequest app) {
		return ResponseEntity.status(HttpStatus.CREATED).body(appService.addApplication(app));
	}
	
	@PutMapping("/application/{id}")
	public ResponseEntity<Response> updateApplication(@PathVariable("id") Integer appId, @RequestBody AppRequest app) {
		return ResponseEntity.ok(appService.updateApplication(appId, app));
	}
	
	@DeleteMapping("/application/{id}")
	public ResponseEntity<Response> deleteApplication(@PathVariable("id") Integer appId) {
		return ResponseEntity.ok(appService.deleteApplication(appId));
	}
} 
 