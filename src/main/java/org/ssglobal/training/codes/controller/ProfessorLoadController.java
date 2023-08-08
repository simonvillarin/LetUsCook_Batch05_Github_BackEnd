package org.ssglobal.training.codes.controller;

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
import org.ssglobal.training.codes.dto.LoadDTO;
import org.ssglobal.training.codes.response.Response;
import org.ssglobal.training.codes.service.ProfessorLoadService;
import org.ssglobal.training.codes.tables.pojos.ProfessorLoad;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lms")
public class ProfessorLoadController {
	private final ProfessorLoadService professorLoadService;
	
	@GetMapping("/loads/{id}")
	public ResponseEntity<LoadDTO> getAllProfessorLoads(@PathVariable("id") Integer professorId) {
		return ResponseEntity.ok(professorLoadService.getAllProfessorLoadsByProfessorId(professorId));
	}
	
	@PostMapping("/load")
	public ResponseEntity<Response> addProfessorLoad(@RequestBody ProfessorLoad professorLoad) {
		return ResponseEntity.status(HttpStatus.CREATED).body(professorLoadService.addProfessorLoad(professorLoad));
	}
	
	@PutMapping("/load/{id}")
	public ResponseEntity<Response> updateProfessorLoad(@PathVariable("id") Integer loadId, @RequestBody ProfessorLoad professorLoad) {
		return ResponseEntity.ok(professorLoadService.updateProfessorLoad(loadId, professorLoad));
	}
	
	@DeleteMapping("/load/{id}")
	public ResponseEntity<Response> deleteProfessorLoad(@PathVariable("id") Integer loadId) {
		return ResponseEntity.ok(professorLoadService.deleteProfessorLoad(loadId));
	}
}
