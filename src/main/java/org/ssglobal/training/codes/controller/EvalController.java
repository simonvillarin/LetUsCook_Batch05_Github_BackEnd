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
import org.ssglobal.training.codes.request.EvalRequest;
import org.ssglobal.training.codes.response.Response;
import org.ssglobal.training.codes.service.EvalService;
import org.ssglobal.training.codes.tables.pojos.Eval;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lms")
public class EvalController {
		private final EvalService evalService;
		
		@GetMapping("/eval/{subjectId}/{sectionId}")
		public ResponseEntity<Eval> getEval(@PathVariable("subjectId") Integer subjectId, @PathVariable("sectionId") Integer sectionId){
			return ResponseEntity.ok(evalService.getEval(subjectId, sectionId));
		}
		
		@GetMapping("/eval/{id}")
		public ResponseEntity<List<Eval>> getEvalBySubjectId(@PathVariable("id") Integer subjectId){
			return ResponseEntity.ok(evalService.getEvaluationBySubjectId(subjectId));
		}
		
		@PostMapping("/eval")
		public ResponseEntity<Response> addEvaluation(@RequestBody EvalRequest eval){
			return ResponseEntity.status(HttpStatus.CREATED).body(evalService.addEvaluation(eval));
		}
		
		@PutMapping("/eval/{evalId}")
		public ResponseEntity<Response> udpateEval(@PathVariable("evalId") Integer evalId,@RequestBody EvalRequest eval){
			return ResponseEntity.ok(evalService.updateEvaluation(evalId, eval));
		}
}
