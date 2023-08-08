package org.ssglobal.training.codes.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ssglobal.training.codes.dto.SectionDTO;
import org.ssglobal.training.codes.request.SectionRequest;
import org.ssglobal.training.codes.response.Response;
import org.ssglobal.training.codes.service.SectionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lms")
public class SectionController {
	private final SectionService sectionService;
	
	@GetMapping("/sections")
	public ResponseEntity<List<SectionDTO>> getAllSections() {
		return ResponseEntity.ok(sectionService.getAllSections());
	}
	
	@GetMapping("/section/{id}")
	public ResponseEntity<List<String>> getSectionByProgram(@PathVariable("id") Integer studentId) {
		return ResponseEntity.ok(sectionService.getSectionByProgram(studentId));
	}
	
	@PostMapping("/section")
	public ResponseEntity<Response> addSection(@RequestBody SectionRequest section) {
		return ResponseEntity.status(HttpStatus.CREATED).body(sectionService.addSection(section));
	}
	
	@PutMapping("/section/{id}")
	public ResponseEntity<Response> updateSection(@PathVariable("id") Integer sectionId, @RequestBody SectionRequest section) {
		return ResponseEntity.ok(sectionService.updateSection(sectionId, section));
	}
}
