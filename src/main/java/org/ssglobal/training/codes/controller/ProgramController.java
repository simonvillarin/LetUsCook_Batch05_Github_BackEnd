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
import org.ssglobal.training.codes.dto.ProgramDTO;
import org.ssglobal.training.codes.request.ProgramRequest;
import org.ssglobal.training.codes.response.Response;
import org.ssglobal.training.codes.service.ProgramService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lms")
public class ProgramController {
	private final ProgramService programService;
	
	@GetMapping("/programs")
	public ResponseEntity<List<ProgramDTO>> getAllPrograms() {
		return ResponseEntity.ok(programService.getAllProgram());
	}
	
	@GetMapping("/program/{id}")
	public ResponseEntity<ProgramDTO> getProgramById(@PathVariable("id") Integer programId) {
		return ResponseEntity.ok(programService.getProgramById(programId));
	}
	
	@PostMapping("/program")
	public ResponseEntity<Response> addProgram(@RequestBody ProgramRequest program) {
		return ResponseEntity.status(HttpStatus.CREATED).body(programService.addProgram(program));
	}
	
	@PutMapping("/program/{id}")
	public ResponseEntity<Response> updateProgram(@PathVariable("id") Integer programId, @RequestBody ProgramRequest program) {
		return ResponseEntity.ok(programService.updateProgram(programId, program));
	}
}
