package org.ssglobal.training.codes.controller;

import java.util.List;

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
import org.ssglobal.training.codes.dto.ScheduleDTO;
import org.ssglobal.training.codes.request.SchedRequest;
import org.ssglobal.training.codes.request.ScheduleRequest;
import org.ssglobal.training.codes.response.Response;
import org.ssglobal.training.codes.service.ScheduleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lms")
public class ScheduleController {
private final ScheduleService scheduleService;

	
	@GetMapping("/schedule/{id}")
	public ResponseEntity<List<ScheduleDTO>> getScheduleById(@PathVariable("id") Integer scheduleId) {
		return ResponseEntity.ok(scheduleService.getScheduleById(scheduleId));
	}
	
	@GetMapping("/schedule/student/{id}")
	public ResponseEntity<List<ScheduleDTO>> getScheduleByStudentId(@PathVariable("id") Integer studentId) {
		return ResponseEntity.ok(scheduleService.getSchedByStudentId(studentId));
	}
	
	@PostMapping("/schedule")
	public ResponseEntity<Response> addSchedule(@RequestBody ScheduleRequest schedule) {
		return ResponseEntity.status(HttpStatus.CREATED).body(scheduleService.addSchedule(schedule));
	}
	
	@PutMapping("/schedule")
	public ResponseEntity<Response> updateSchedule(@RequestBody SchedRequest schedule) {
		return ResponseEntity.ok(scheduleService.updateSchedule(schedule));
	}
	
	@PutMapping("/schedule/{id}")
	public ResponseEntity<Response> deleteSchedule(@PathVariable("id") Integer[] scheduleId, @RequestBody SchedRequest schedule) {
		return ResponseEntity.ok(scheduleService.deleteSchedule(scheduleId, schedule));
	}
}