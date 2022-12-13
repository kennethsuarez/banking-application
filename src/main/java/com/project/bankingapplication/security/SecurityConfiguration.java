package com.project.bankingapplication.security;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		  
		http.authorizeHttpRequests(
				auth -> auth
					.requestMatchers("/api/accounts").hasRole("ADMIN")
					.anyRequest().authenticated()
				);
		
		http.httpBasic(withDefaults());
		
		
		// do not disable in real project
		// instead use csrf tokens
		http.csrf().disable();
		
		return http.build();
	}
}
