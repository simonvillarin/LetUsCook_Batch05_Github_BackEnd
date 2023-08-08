package org.ssglobal.training.codes.service;

import java.time.LocalDateTime;
import java.util.List;

import org.jooq.DSLContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.ssglobal.training.codes.response.Response;
import org.ssglobal.training.codes.tables.pojos.Account;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {
	private final DSLContext dslContext;
	private final PasswordGenerator passGenerator;
	private final PasswordEncoder passEncoder;
	private final org.ssglobal.training.codes.tables.Account ACCOUNT = org.ssglobal.training.codes.tables.Account.ACCOUNT;
	
	public List<Account> getAllAccounts(){
		return dslContext.selectFrom(ACCOUNT)
				.fetchInto(Account.class);
	}
	
	public Account getAccountById(Integer accountId) {
		Account account = dslContext.selectFrom(ACCOUNT)
				.where(ACCOUNT.ACCOUNT_ID.eq(accountId))
				.fetchOneInto(Account.class);
		if(account != null) {
			return account;
		} else {
			throw new RuntimeException("Account not found");
		}
	}
	
	public Account getAccountByUserId(Integer userId) {
		Account account = dslContext.selectFrom(ACCOUNT)
				.where(ACCOUNT.USER_ID.eq(userId))
				.fetchOneInto(Account.class);
		if(account != null) {
			return account;
		} else {
			throw new RuntimeException("Account not found");
		}
	}
	
	public Response addAccount(Account account) {
		Account username = dslContext.selectFrom(ACCOUNT)
				.where(ACCOUNT.USERNAME.eq(account.getUsername()))
				.fetchOneInto(Account.class);
		if(username != null) {
			return Response.builder()
					.status(409)
					.message("Username already exist")
					.timestamp(LocalDateTime.now())
					.build();
		}else {
			String pass = passGenerator.generatePassword();
			
			dslContext.insertInto(ACCOUNT)
			.set(ACCOUNT.USER_ID, account.getUserId())
			.set(ACCOUNT.USERNAME, account.getUsername())
			.set(ACCOUNT.PASSWORD, passEncoder.encode(pass))
			.set(ACCOUNT.PASS, pass)
			.set(ACCOUNT.TYPE, account.getType())
			.set(ACCOUNT.ACTIVE_DEACTIVE, account.getActiveDeactive())
			.execute();
			return Response.builder()
					.status(201)
					.message("Account successfully created")
					.timestamp(LocalDateTime.now())
					.build();
		}
	}
	
	public Response updateAccount(Integer accountId, Account account) {
		Account email = dslContext.selectFrom(ACCOUNT)
				.where(ACCOUNT.USERNAME.eq(account.getUsername()))
				.fetchOneInto(Account.class);
		Account acc = dslContext.selectFrom(ACCOUNT)
				.where(ACCOUNT.USER_ID.eq(accountId))
				.fetchOneInto(Account.class);
		if(email != null) {
			return Response.builder()
					.status(409)
					.message("Username already exist")
					.timestamp(LocalDateTime.now())
					.build();
		}else {
			if(account.getUsername() != null) {
				dslContext.update(ACCOUNT)
				.set(ACCOUNT.USERNAME, account.getUsername())
				.where(ACCOUNT.ACCOUNT_ID.eq(acc.getAccountId()))
				.execute();
			}
			if(account.getPassword() != null) {
				dslContext.update(ACCOUNT)
				.set(ACCOUNT.PASSWORD, passEncoder.encode(account.getPassword()))
				.set(ACCOUNT.PASS, account.getPassword())
				.where(ACCOUNT.ACCOUNT_ID.eq(acc.getAccountId()))
				.execute();
			}
			if (account.getActiveDeactive() != null) {
				dslContext.update(ACCOUNT)
				.set(ACCOUNT.ACTIVE_DEACTIVE, account.getActiveDeactive())
				.where(ACCOUNT.ACCOUNT_ID.eq(acc.getAccountId()))
				.execute();
			}
				
			return Response.builder()
					.status(201)
					.message("Account successfully updated")
					.timestamp(LocalDateTime.now())
					.build();
		}
	}
	
	public Response deleteAccount(Integer userId) {
		Account account = dslContext.selectFrom(ACCOUNT)
				.where(ACCOUNT.USER_ID.eq(userId))
				.fetchOneInto(Account.class);
		if(account != null) {
			dslContext.delete(ACCOUNT)
			.where(ACCOUNT.ACCOUNT_ID.eq(account.getAccountId()))
			.execute();
			
			return Response.builder()
					.status(201)
					.message("Account successfully deleted")
					.timestamp(LocalDateTime.now())
					.build();
		}else {
			return Response.builder()
					.status(404)
					.message("Student not found")
					.timestamp(LocalDateTime.now())
					.build();
		}
	}
}