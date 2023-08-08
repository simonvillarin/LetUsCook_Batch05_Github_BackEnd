package org.ssglobal.training.codes.security;

import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.ssglobal.training.codes.tables.pojos.Account;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

	private final DSLContext dslContext;
	private final org.ssglobal.training.codes.tables.Account ACCOUNT = org.ssglobal.training.codes.tables.Account.ACCOUNT;

	@Bean
	UserDetailsService userDetailsService() {
		return new UserDetailsService() {
			@Override
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				Account account = dslContext.selectFrom(ACCOUNT)
						.where(ACCOUNT.USERNAME.eq(username))
						.fetchOneInto(Account.class);
				
				UserDetails userDetails;
				if (account != null) {
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
					return userDetails;
				} else {
					throw new UsernameNotFoundException("User not found");
				}
			}
		};
	}

	@Bean
	AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}