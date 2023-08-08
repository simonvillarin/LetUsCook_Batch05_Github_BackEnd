package org.ssglobal.training.codes.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.ssglobal.training.codes.response.Response;
import org.ssglobal.training.codes.service.ParentService;
import org.ssglobal.training.codes.tables.pojos.Parent;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lms")
public class ParentController {
	private final ParentService parentService;
	
	@GetMapping("/parents")
	public ResponseEntity<List<Parent>> getAllParents() {
		return ResponseEntity.ok(parentService.getAllParents());
	}
	
	@GetMapping("/parent/{id}")
	public ResponseEntity<Parent> getParentById(@PathVariable("id") Integer parentId) {
		return ResponseEntity.ok(parentService.getParentById(parentId));
	}
	
	@PutMapping(value = "/parent/image/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Response> updateParentWithImage(@PathVariable("id") Integer parentId, @RequestPart Parent parent, @RequestPart MultipartFile file) {
		return ResponseEntity.ok(parentService.updateParentWithImage(parentId, parent, file));
	}

	@PutMapping("/parent/{id}")
	public ResponseEntity<Response> updateParent(@PathVariable("id") Integer parentId, @RequestBody Parent parent) {
		return ResponseEntity.ok(parentService.updateParent(parentId, parent));
	}
	
	@PutMapping(value = "/parent/img/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Response> updateImage(@PathVariable("id") Integer parentId, @RequestPart MultipartFile image) {
		return ResponseEntity.ok(parentService.updateParentImage(parentId, image));
	}
	
	@PutMapping(value = "/parent/banner/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Response> updateBanner(@PathVariable("id") Integer parentId, @RequestPart MultipartFile banner) {
		return ResponseEntity.ok(parentService.updateParentBanner(parentId, banner));
	}
	
	@DeleteMapping("/parent/{id}")
	public ResponseEntity<Response> deleteParent(@PathVariable("id") Integer parentId){
		return ResponseEntity.ok(parentService.deleteParent(parentId));
	}
}
