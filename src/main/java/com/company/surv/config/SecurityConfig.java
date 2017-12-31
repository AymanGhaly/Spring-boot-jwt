package com.company.surv.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.company.surv.security.JwtTokenUtil;
import com.company.surv.security.filtes.JwtAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	public static final String SIGN_IN_URL = "/surv/sucurity/signIn";
	public static final String SIGN_UP_URL = "/surv/sucurity/signUp";

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf()//
				.disable()//
				.authorizeRequests()//
				.anyRequest()//
				.authenticated().and()
				.addFilter(new JwtAuthenticationFilter(authenticationManager(), jwtTokenUtil, userDetailsService));
	}

	@Override
	public void configure(org.springframework.security.config.annotation.web.builders.WebSecurity web)
			throws Exception {
		web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources", "/configuration/security",
				"/swagger-ui.html", "/webjars/**", "/swagger-resources/configuration/ui",
				"/swagger-resources/configuration/security", SIGN_UP_URL, SIGN_IN_URL);
	}
}
