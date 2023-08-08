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
import org.ssglobal.training.codes.dto.AttendanceStudentDTO;
import org.ssglobal.training.codes.response.Response;
import org.ssglobal.training.codes.service.AttendanceStudentService;
import org.ssglobal.training.codes.tables.pojos.AttendanceStudent;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lms")
public class AttendanceStudentController {

	private final AttendanceStudentService attendanceService;
	
	@GetMapping("/attendance/student/all")
	public ResponseEntity<List<AttendanceStudentDTO>> getAllAttendance(){
		return ResponseEntity.ok(attendanceService.getAllAttendance());
	}
	
	@GetMapping("/attendance/student/{id}")
	public ResponseEntity<List<AttendanceStudentDTO>> getAttendanceByStudentId(@PathVariable("id") Integer studentId){
		return ResponseEntity.ok(attendanceService.getAttendanceByStudentId(studentId));
	}
	
	@GetMapping("/attendance/student/{id}/{subjectId}")
	public ResponseEntity<List<AttendanceStudentDTO>> getAttendanceById(@PathVariable("id") Integer studentId, @PathVariable("subjectId") Integer subjectId){
		return ResponseEntity.ok(attendanceService.getAttendanceById(studentId, subjectId));
	}
	
	@GetMapping("/attendance/section/{sectionId}/{subId}")
	public ResponseEntity<HashSet<AttendanceStudentDTO>> getAttendanceBySection(@PathVariable("sectionId") Integer sectionId, @PathVariable("subId") Integer subjectId) {
		return ResponseEntity.ok(attendanceService.getAttendanceBySection(sectionId, subjectId));
	}
	
	@GetMapping("/attendance/section/{sectionId}")
	public ResponseEntity<HashSet<AttendanceStudentDTO>> getAttendanceBySection(@PathVariable("sectionId") Integer sectionId) {
		return ResponseEntity.ok(attendanceService.getAttendanceBySection(sectionId));
	}
	
	@GetMapping("/attendance/students/{sectionId}/{subjectId}")
	public ResponseEntity<Response> addAttendance(@PathVariable("sectionId") Integer sectionId, @PathVariable("subjectId") Integer subjectId){
		return ResponseEntity.status(HttpStatus.CREATED).body(attendanceService.addAttendance(sectionId, subjectId));
	}
	
	@PostMapping("/attendance/student")
	public ResponseEntity<Response> addAttendance(@RequestBody AttendanceStudent attendance){
		return ResponseEntity.status(HttpStatus.CREATED).body(attendanceService.addAttendance(attendance));
	}
	
	@PutMapping("/attendance/student/{id}")
	public ResponseEntity<Response> updateAttendance(@PathVariable("id") Integer attendanceId, @RequestBody AttendanceStudent attendance){
		return ResponseEntity.ok(attendanceService.updateSubject(attendanceId, attendance));
	}
}