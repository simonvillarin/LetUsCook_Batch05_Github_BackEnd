package org.ssglobal.training.codes.controller;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ssglobal.training.codes.service.ImageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lms/image/")
public class ImageController {
	private  final ImageService imageService;
	
	@GetMapping("/{filename}")
	public ResponseEntity<Resource> retrieve(@PathVariable String filename) {
		var image = imageService.getImage(filename);
		var body = new ByteArrayResource(image.getData());
		
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_TYPE, image.getMimeType())
				.body(body);
	}
}
