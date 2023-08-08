package org.ssglobal.training.codes.auth;

import org.jooq.DSLContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.ssglobal.training.codes.security.JwtService;
import org.ssglobal.training.codes.tables.pojos.Account;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
	private final DSLContext dslContext;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
	private final org.ssglobal.training.codes.tables.Account ACCOUNT = org.ssglobal.training.codes.tables.Account.ACCOUNT;
	
	public AuthenticationResponse login(AuthenticationRequest authRequest) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
		
		Account account = dslContext.selectFrom(ACCOUNT)
				.where(ACCOUNT.USERNAME.eq(authRequest.getUsername()).and(ACCOUNT.ACTIVE_DEACTIVE.eq(true)))
				.fetchOneInto(Account.class);
		
		if (account != null) {
			UserDetails userDetails;
			if (account.getType() == "ADMIN") {
				userDetails = User.builder()
						.username(account.getUsername())
						.password(account.getPassword())
						.roles("ADMIN")
						.build();
			} else {
				userDetails = User.builder()
						.username(account.getUsername())
						.password(account.getPassword())
						.roles("USER")
						.build();
			}
			String token = jwtService.generateToken(userDetails);
			return AuthenticationResponse.builder()
					.token(token)
					.id(account.getUserId())
					.type(account.getType())
					.build();
		} else {
			throw new RuntimeException("User not found");
		}
	}
}
