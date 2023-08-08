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
import org.ssglobal.training.codes.dto.EvaluationDTO;
import org.ssglobal.training.codes.response.Response;
import org.ssglobal.training.codes.service.EvaluationService;
import org.ssglobal.training.codes.tables.pojos.Evaluation;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lms")
public class EvaluationController {
	private final EvaluationService evaluationService;
	
	@GetMapping("/evaluation/{id}")
	public ResponseEntity<List<EvaluationDTO>> getEvaluationById(@PathVariable("id") Integer subjectId) {
		return ResponseEntity.ok(evaluationService.getEvaluationBySubjectId(subjectId));
	}
	
	@GetMapping("/evaluation/{subjectId}/{studentId}")
	public ResponseEntity<EvaluationDTO> getEvaluationById(@PathVariable("subjectId") Integer subjectId, @PathVariable("studentId") Integer studentId) {
		return ResponseEntity.ok(evaluationService.getEvaluationBySubjectId(subjectId, studentId));
	}
	
	
	@PostMapping("/evaluation")
	public ResponseEntity<Response> addEvaluation(@RequestBody Evaluation evaluation) {
		return ResponseEntity.status(HttpStatus.CREATED).body(evaluationService.addEvaluation(evaluation));
	}
	
	@PutMapping("/evaluation/{id}")
	public ResponseEntity<Response> updateEvaluation(@PathVariable("id") Integer evaluationId, @RequestBody Evaluation evalution) {
		return ResponseEntity.ok(evaluationService.updateEvaluation(evaluationId, evalution));
	}
}