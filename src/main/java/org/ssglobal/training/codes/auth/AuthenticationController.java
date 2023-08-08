package org.ssglobal.training.codes.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lms/auth")
public class AuthenticationController {
	private final AuthenticationService authService;
	
	@PostMapping("/login")
	public ResponseEntity<AuthenticationResponse> login(@RequestBody  AuthenticationRequest authRequest) {
		return ResponseEntity.ok(authService.login(authRequest));
	}
}
