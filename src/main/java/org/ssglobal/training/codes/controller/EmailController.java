package org.ssglobal.training.codes.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ssglobal.training.codes.dto.ContactForm;
import org.ssglobal.training.codes.request.EmailRequest;
import org.ssglobal.training.codes.response.Response;
import org.ssglobal.training.codes.service.EmailService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lms")
public class EmailController {
	private final EmailService emailService;

	@PostMapping("/otp")
	public Integer sendEmail(@RequestBody String sendTo) {
		return emailService.sendEmail(sendTo);
	}

	@PostMapping("/otp/expiration")
	public Boolean sendExpiration(@RequestBody String email) {
		return emailService.isOtpExpired(email);
	}

	@PostMapping("/email")
	public ResponseEntity<Response> checkEmail(@RequestBody String email) {
		return ResponseEntity.ok(emailService.checkEmail(email));
	}
	
	@PostMapping("/contact")
	public ResponseEntity<String> checkEmail(@RequestBody ContactForm form) {
		return ResponseEntity.ok(emailService.sendEmail1(form));
	}
	
	@PostMapping("/send/email")
	public void checkEmail(@RequestBody EmailRequest email) {
		emailService.sendEmail(email);
	}
}