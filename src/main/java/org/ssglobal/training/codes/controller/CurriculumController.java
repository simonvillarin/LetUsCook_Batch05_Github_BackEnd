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
import org.ssglobal.training.codes.dto.CurriculumDTO;
import org.ssglobal.training.codes.request.CurriculumRequest;
import org.ssglobal.training.codes.response.Response;
import org.ssglobal.training.codes.service.CurriculumService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lms")
public class CurriculumController {
	private final CurriculumService curriculumService;
	
	@GetMapping("/curriculums")
	public ResponseEntity<List<CurriculumDTO>> getAllCurriculums() {
		return ResponseEntity.ok(curriculumService.getAllCurriculum());
	}
	
	@GetMapping("/curriculum/{id}")
	public ResponseEntity<CurriculumDTO> getCurriculumById(@PathVariable("id") Integer curriculumId) {
		return ResponseEntity.ok(curriculumService.getCurriculumById(curriculumId));
	}
	
	@PostMapping("/curriculums")
	public ResponseEntity<Response> addCurriculums(@RequestBody List<CurriculumRequest> curriculums) {
		return ResponseEntity.ok(curriculumService.addCurriculums(curriculums));
	}
	
	@PostMapping("/curriculum")
	public ResponseEntity<Response> addCurriculum(@RequestBody CurriculumRequest curriculum) {
		return ResponseEntity.status(HttpStatus.CREATED).body(curriculumService.addCurriculum(curriculum));
	}
	
	@PutMapping("/curriculum/{id}")
	public ResponseEntity<Response> updateCurriculum(@PathVariable("id") Integer curriculumId, @RequestBody CurriculumRequest curriculum) {
		return ResponseEntity.ok(curriculumService.updateCurriculum(curriculumId, curriculum));
	}
}