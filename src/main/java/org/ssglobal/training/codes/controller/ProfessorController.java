package org.ssglobal.training.codes.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.ssglobal.training.codes.response.Response;
import org.ssglobal.training.codes.service.ProfessorService;
import org.ssglobal.training.codes.tables.pojos.Professor;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lms")
public class ProfessorController {
	private final ProfessorService professorService;

	@GetMapping("/professors")
	public ResponseEntity<List<Professor>> getAllProfessors() {
		return ResponseEntity.ok(professorService.getAllProfessors());
	}
	
	@GetMapping("/professor/{id}")
	public ResponseEntity<Professor> getStudentById(@PathVariable("id") Integer professorId) {
		return ResponseEntity.ok(professorService.getProfessorById(professorId));
	}
	
	@PostMapping("/professor")
	public ResponseEntity<Response> addProfessor(@RequestBody Professor professor) {
		return ResponseEntity.status(HttpStatus.CREATED).body(professorService.addProfessor(professor));
	}
	
	@PostMapping(value = "/professor/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Response> addProfessorWithImage(@RequestPart Professor professor, @RequestPart MultipartFile image) {
		return ResponseEntity.status(HttpStatus.CREATED).body(professorService.addProfessorWithImage(professor, image));
	}
	
	@PutMapping(value = "/professor/img/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Response> updateImage(@PathVariable("id") Integer professorId, @RequestPart MultipartFile image) {
		return ResponseEntity.ok(professorService.updateImage(professorId, image));
	}
	
	@PutMapping(value = "/professor/banner/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Response> updateBanner(@PathVariable("id") Integer professorId, @RequestPart MultipartFile banner) {
		return ResponseEntity.ok(professorService.updateBanner(professorId, banner));
	}
	
	@PutMapping(value = "/professor/image/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Response> updateProfessorWithImage(@PathVariable("id") Integer professorId, @RequestPart Professor professor, @RequestPart MultipartFile image) {
		return ResponseEntity.ok(professorService.updateProfessorWithImage(professorId, professor, image));
	}
	
	@PutMapping("/professor/{id}")
	public ResponseEntity<Response> updateProfessor(@PathVariable("id") Integer professorId, @RequestBody Professor professor) {
		return ResponseEntity.ok(professorService.updateProfessor(professorId, professor));
	}
}
