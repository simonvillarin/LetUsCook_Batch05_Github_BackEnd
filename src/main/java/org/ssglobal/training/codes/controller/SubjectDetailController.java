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
import org.ssglobal.training.codes.dto.SubjectDetailDTO;
import org.ssglobal.training.codes.response.Response;
import org.ssglobal.training.codes.service.SubjectDetailService;
import org.ssglobal.training.codes.tables.pojos.SubjectDetailHistory;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lms")
public class SubjectDetailController {
	private final SubjectDetailService subjService;
	
	@GetMapping("/subject/history/all")
	public ResponseEntity<List<SubjectDetailDTO>> getAllSubject(){
		return ResponseEntity.ok(subjService.getAllSubjectHistory());
	}
	
	@GetMapping("/subject/history/{id}")
	public ResponseEntity<SubjectDetailDTO> getSubjById(@PathVariable("id") Integer historyId){
		return ResponseEntity.ok(subjService.getSubjectHistoryById(historyId));
	}
	
	@PostMapping("/subject/history")
	public ResponseEntity<Response> addSubjectHistory(@RequestBody SubjectDetailHistory subj){
		return ResponseEntity.status(HttpStatus.CREATED).body(subjService.addSubject(subj));
	}
	@PutMapping("/subject/history/{id}")
	public ResponseEntity<Response> updateSubjectHistory(@PathVariable("id") Integer historyId, @RequestBody SubjectDetailHistory subj){
		return ResponseEntity.ok(subjService.updateSubject(historyId, subj));
	}
}