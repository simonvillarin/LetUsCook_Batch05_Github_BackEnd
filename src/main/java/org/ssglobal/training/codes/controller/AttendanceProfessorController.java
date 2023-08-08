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
import org.ssglobal.training.codes.dto.AttendanceProfDTO;
import org.ssglobal.training.codes.response.Response;
import org.ssglobal.training.codes.service.AttendanceProfessorService;
import org.ssglobal.training.codes.tables.pojos.AttendanceProfessor;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lms")
public class AttendanceProfessorController {
	private final AttendanceProfessorService profAttendanceService;
	
	@GetMapping("/profAttendances")
	public ResponseEntity<List<AttendanceProfDTO>> getAllProfAttendance() {
		return ResponseEntity.ok(profAttendanceService.getAllProfAttendance());
	}
	
	@GetMapping("/profAttendance/{id}")
	public ResponseEntity<List<AttendanceProfDTO>> getprofAttendanceById(@PathVariable("id") Integer professorId) {
		return ResponseEntity.ok(profAttendanceService.getProfAttendanceById(professorId));
	}
	
	@PostMapping("/profAttendance")
	public ResponseEntity<Response> addProfAttendance(@RequestBody AttendanceProfessor attendanceProf) {
		return ResponseEntity.status(HttpStatus.CREATED).body(profAttendanceService.addProfAttendance(attendanceProf));
	}
	
	@PutMapping("/profAttendance/{id}")
	public ResponseEntity<Response> updateProfAttendance(@PathVariable("id") Integer attendanceId, @RequestBody AttendanceProfessor attendanceProf) {
		return ResponseEntity.ok(profAttendanceService.updateProfAttendance(attendanceId, attendanceProf));
	}
}