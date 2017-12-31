package com.company.surv.security.filtes;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.company.surv.security.JwtTokenUtil;

import io.jsonwebtoken.ExpiredJwtException;

public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

	private static final String HEADER_STRING = "Authorization";

	private JwtTokenUtil jwtTokenUtil;

	private UserDetailsService userDetailsService;

	public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, UserDetailsService userDetailsService) {
		super(authenticationManager);
		this.jwtTokenUtil = jwtTokenUtil;
		this.userDetailsService=userDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		String email = null;
		String authToken = null;
		String header = req.getHeader(HEADER_STRING);
		if (header != null && header.startsWith("Bearer ")) {
			authToken = header.substring(7);
			try {
				email = jwtTokenUtil.getUserEmailFromToken(authToken);
			} catch (IllegalArgumentException e) {
				System.out.println("an error occured during getting username from token");
			} catch (ExpiredJwtException e) {
				System.out.println("the token is expired and not valid anymore");
			}
		}
		if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);
			if (jwtTokenUtil.validateToken(authToken)) {
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}
		chain.doFilter(req, res);
	}

}
