package org.ssglobal.training.codes.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ssglobal.training.codes.dto.StudentHistoryDTO;
import org.ssglobal.training.codes.response.Response;
import org.ssglobal.training.codes.service.StudentHistoryService;
import org.ssglobal.training.codes.tables.pojos.StudentHistory;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lms")
public class StudentHistoryController {
	private final StudentHistoryService shc;
	
	@GetMapping("/shs/{id}")
	public ResponseEntity<List<StudentHistoryDTO>> getStudentHistory(@PathVariable("id") Integer studentId) {
		return ResponseEntity.ok(shc.getSHById(studentId));
	}
	
	@GetMapping("/shs/student/{studentNo}")
	public ResponseEntity<List<StudentHistoryDTO>> getStudentHistory(@PathVariable("studentNo") String studentNo) {
		return ResponseEntity.ok(shc.getStudentByStudentNo(studentNo));
	}
	
	@PostMapping("/sh")
	public ResponseEntity<Response> add(@RequestBody StudentHistory sh) {
		return ResponseEntity.status(HttpStatus.CREATED).body(shc.addSh(sh));
	}
	
	@DeleteMapping("/sh")
	public ResponseEntity<Response> update(@PathVariable("id") Integer studentId, @RequestBody StudentHistoryDTO sh) {
		return ResponseEntity.ok(shc.updateSh(studentId, sh));
	}
}