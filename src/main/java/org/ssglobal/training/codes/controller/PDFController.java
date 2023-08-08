package org.ssglobal.training.codes.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ssglobal.training.codes.service.PDFService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lms")
public class PDFController {
	private final PDFService pdfService;
	
	@GetMapping("/generate/{id}")
	public void generatePDF(@PathVariable("id") Integer studentId) {
		pdfService.generatePDF(studentId);
	}
}
