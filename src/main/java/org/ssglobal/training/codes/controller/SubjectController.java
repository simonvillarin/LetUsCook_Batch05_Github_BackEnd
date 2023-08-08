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
import org.ssglobal.training.codes.dto.SubjectDTO;
import org.ssglobal.training.codes.request.SubjectRequest;
import org.ssglobal.training.codes.response.Response;
import org.ssglobal.training.codes.service.SubjectService;
import org.ssglobal.training.codes.tables.pojos.Subject;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lms")
public class SubjectController {
	private final SubjectService subjectService;
	
	@GetMapping("/subjects")
	public ResponseEntity<List<SubjectDTO>> getAllSubjects() {
		return ResponseEntity.ok(subjectService.getAllSubjects());
	}
	
	@GetMapping("/subject/{id}")
	public ResponseEntity<SubjectDTO> getSubjectById(@PathVariable("id") Integer subjectId) {
		return ResponseEntity.ok(subjectService.getSubjectById(subjectId));
	}
	
	@GetMapping("/majors")
	public ResponseEntity<List<Subject>> getMajors() {
		return ResponseEntity.ok(subjectService.getAllMajors());
	}
	
	@GetMapping("/minors")
	public ResponseEntity<List<Subject>> getMinors() {
		return ResponseEntity.ok(subjectService.getAllMinors());
	}
	
	@GetMapping("/electives")
	public ResponseEntity<List<Subject>> getElectives() {
		return ResponseEntity.ok(subjectService.getAllElectives());
	}
	
	@PostMapping("/subjects")
	public ResponseEntity<Response> addSubjects(@RequestBody List<SubjectRequest> subjects) {
		return ResponseEntity.ok(subjectService.addSubjects(subjects));
	}
	
	@PostMapping("/subject")
	public ResponseEntity<Response> addSubject(@RequestBody SubjectRequest subject) {
		return ResponseEntity.status(HttpStatus.CREATED).body(subjectService.addSubject(subject));
	}
	
	@PutMapping("/subject/{id}")
	public ResponseEntity<Response> updateSubject(@PathVariable("id") Integer subjectId, @RequestBody SubjectRequest subject) {
		return ResponseEntity.ok(subjectService.updateSubject(subjectId, subject));
	}
}
