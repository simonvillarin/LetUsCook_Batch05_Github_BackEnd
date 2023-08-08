package org.ssglobal.training.codes.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.ssglobal.training.codes.request.AdminRequest;
import org.ssglobal.training.codes.response.Response;
import org.ssglobal.training.codes.service.AdminService;
import org.ssglobal.training.codes.tables.pojos.Admin;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lms")
public class AdminController {
	private final AdminService adminService;
	
	@GetMapping("/admin")
	public ResponseEntity<List<Admin>> getAllAdmin() {
		return ResponseEntity.ok(adminService.getAllAdmin());
	}
	
	@GetMapping("/admin/{id}")
	public ResponseEntity<Admin> getAdminById(@PathVariable("id") Integer adminId) {
		return ResponseEntity.ok(adminService.getAdminById(adminId));
	}
	
	@PostMapping( value = "/auth/admin", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Response> addAdmin(@RequestPart AdminRequest admin, @RequestPart MultipartFile image) {
		return ResponseEntity.status(HttpStatus.CREATED).body(adminService.addAdmin(admin, image));
	}
	
	@PutMapping(value = "/admin/image/{id}",  consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Response> updateImage(@PathVariable("id") Integer adminId, @RequestPart MultipartFile image) {
		return ResponseEntity.ok(adminService.updateImage(adminId, image));
	}
	
	@PutMapping(value = "/admin/banner/{id}",  consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Response> updateBanner(@PathVariable("id") Integer adminId, @RequestPart MultipartFile banner) {
		return ResponseEntity.ok(adminService.updateBanner(adminId, banner));
	}
	
	
	@PutMapping("/admin/{id}")
	public ResponseEntity<Response> updateAdmin(@PathVariable("id") Integer adminId, @RequestBody AdminRequest admin) {
		return ResponseEntity.ok(adminService.updateAdmin(adminId, admin));
	}
}
