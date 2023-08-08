package org.ssglobal.training.codes.controller;

import java.util.HashSet;
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
import org.ssglobal.training.codes.dto.GradesDTO;
import org.ssglobal.training.codes.response.Response;
import org.ssglobal.training.codes.service.GradesService;
import org.ssglobal.training.codes.tables.pojos.Grades;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lms")
public class GradesController {
	private final GradesService gradesService;
	
	@GetMapping("/grades")
	public ResponseEntity<List<GradesDTO>> getAllGrades() {
		return ResponseEntity.ok(gradesService.getAllGrades());
	}
	
	@GetMapping("/grade/{id}")
	public ResponseEntity<List<GradesDTO>> getGradeById(@PathVariable("id") Integer studentId) {
		return ResponseEntity.ok(gradesService.getGradeById(studentId));
	}
	
	@GetMapping("/grades/{sectionId}/{subId}")
	public ResponseEntity<HashSet<GradesDTO>> getGradesBySection(@PathVariable("sectionId") Integer sectionId, @PathVariable("subId") Integer subjectId) {
		return ResponseEntity.ok(gradesService.getGradesBySection(sectionId, subjectId));
	}
	
	@GetMapping("/grades/{sectionId}")
	public ResponseEntity<HashSet<GradesDTO>> getGradesBySection(@PathVariable("sectionId") Integer sectionId) {
		return ResponseEntity.ok(gradesService.getGradesBySection(sectionId));
	}
	
	@PostMapping("/grade")
	public ResponseEntity<Response> addGrade(@RequestBody Grades grade) {
		return ResponseEntity.status(HttpStatus.CREATED).body(gradesService.addGrade(grade));
	}
	
	@PutMapping("/grade/{id}")
	public ResponseEntity<Response> updateGrade(@PathVariable("id") Integer gradeId, @RequestBody Grades grade) {
		return ResponseEntity.ok(gradesService.updateGrade(gradeId, grade));
	}
}
