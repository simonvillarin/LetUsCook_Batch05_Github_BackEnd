package org.ssglobal.training.codes.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ssglobal.training.codes.response.Response;
import org.ssglobal.training.codes.service.SchoolCalendarService;
import org.ssglobal.training.codes.tables.pojos.SchoolCalendar;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lms")
public class SchoolCalendarController {
	private final SchoolCalendarService scc;
	
	@GetMapping("/calendar")
	public ResponseEntity<List<SchoolCalendar>> getCalendars() {
		return ResponseEntity.ok(scc.getSchoolCalendar());
	}
	
	@PostMapping("/calendar")
	public ResponseEntity<Response> addSchoolCalendar(@RequestBody SchoolCalendar calendar) {
		return ResponseEntity.ok(scc.addSchoolCalendar(calendar));
	}
}
