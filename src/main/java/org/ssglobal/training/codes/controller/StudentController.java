package org.ssglobal.training.codes.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.ssglobal.training.codes.dto.StudentDTO;
import org.ssglobal.training.codes.request.StudentRequest;
import org.ssglobal.training.codes.response.Response;
import org.ssglobal.training.codes.service.StudentService;
import org.ssglobal.training.codes.tables.pojos.Student;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lms")
public class StudentController {
	private final StudentService studentService;
	
	@GetMapping("/students")
	public ResponseEntity<List<StudentDTO>> getAllStudents() {
		return ResponseEntity.ok(studentService.getAllStudents());
	}
	
	@GetMapping("/student/{id}")
	public ResponseEntity<StudentDTO> getStudentById(@PathVariable("id") Integer studentId) {
		return ResponseEntity.ok(studentService.getStudentById(studentId));
	}
	
	@GetMapping("/student/parent/{parentId}")
	public ResponseEntity<List<StudentDTO>> getStudentByParentId(@PathVariable("parentId") Integer parentId) {
		return ResponseEntity.ok(studentService.getStudentByParentId(parentId));
	}
	
	@GetMapping("/studentNo/{studentNo}")
	public ResponseEntity<StudentDTO> getStudentByStudentNo(@PathVariable("studentNo") String studentNo) {
		return ResponseEntity.ok(studentService.getStudentByStudentNo(studentNo));
	}
	
	@PostMapping("/students")
	public ResponseEntity<Response> addStudents(@RequestBody List<StudentRequest> students) {
		return ResponseEntity.status(HttpStatus.CREATED).body(studentService.addStudents(students));
	}
	
	@PostMapping("/student")
	public ResponseEntity<Response> addStudent(@RequestBody StudentRequest student) {
		return ResponseEntity.status(HttpStatus.CREATED).body(studentService.addStudent(student));
	}
	
	@DeleteMapping("/student/{id}")
	public ResponseEntity<Response> deleteStudent(@PathVariable("id") Integer studentId){
		return ResponseEntity.ok(studentService.deleteStudent(studentId));
	}
	
	@PostMapping(value = "/student/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Response> addStudentWithImage(@RequestPart StudentRequest student, @RequestPart MultipartFile image) {
		return ResponseEntity.status(HttpStatus.CREATED).body(studentService.addStudentWithImage(student, image));
	}
	
	@PutMapping(value = "/student/img/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Response> updateImage(@PathVariable("id") Integer studentId, @RequestPart MultipartFile image) {
		return ResponseEntity.ok(studentService.updateImage(studentId, image));
	}
	
	@PutMapping(value = "/student/banner/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Response> updateBanner(@PathVariable("id") Integer studentId, @RequestPart MultipartFile banner) {
		return ResponseEntity.ok(studentService.updateBanner(studentId, banner));
	}
	
	@PutMapping(value = "/student/image/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Response> updateStudentWithImage(@PathVariable("id") Integer studentId, @RequestPart StudentRequest student, @RequestPart MultipartFile image) {
		return ResponseEntity.ok(studentService.updateStudentWithImage(studentId, student, image));
	}
	
	@PutMapping("/student/{id}")
	public ResponseEntity<Response> updateStudent(@PathVariable("id") Integer studentId, @RequestBody StudentRequest student) {
		return ResponseEntity.ok(studentService.updateStudent(studentId, student));
	}
}