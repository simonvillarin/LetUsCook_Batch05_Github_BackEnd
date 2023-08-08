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
import org.ssglobal.training.codes.response.Response;
import org.ssglobal.training.codes.service.AccountService;
import org.ssglobal.training.codes.tables.pojos.Account;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lms")
public class AccountController {
	private final AccountService accService;
	
	@GetMapping("/accounts")
	public ResponseEntity<List<Account>> getAllAccounts(){
		return ResponseEntity.ok(accService.getAllAccounts());
	}
	
	@GetMapping("/account/{id}")
	public ResponseEntity<Account> getAccountById(@PathVariable("id") Integer accountId){
		return ResponseEntity.ok(accService.getAccountById(accountId));
	}
	
	@GetMapping("/account/userid/{id}")
	public ResponseEntity<Account> getAccountByUserId(@PathVariable("id") Integer userId) {
		return ResponseEntity.ok(accService.getAccountByUserId(userId));
	}
	
	@PostMapping("/account")
	public ResponseEntity<Response> addAccount(@RequestBody Account account){
		return ResponseEntity.status(HttpStatus.CREATED).body(accService.addAccount(account));
	}
	
	@PutMapping("/account/{id}")
	public ResponseEntity<Response> updateAccount(@PathVariable("id") Integer accountId, @RequestBody Account account){
		return ResponseEntity.ok(accService.updateAccount(accountId, account));
	}
	
	@DeleteMapping("/account/{id}")
	public ResponseEntity<Response> deleteAccount(@PathVariable("id") Integer accId){
		return ResponseEntity.ok(accService.deleteAccount(accId));
	}
}