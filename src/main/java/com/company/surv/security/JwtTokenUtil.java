package com.company.surv.security;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.company.surv.dao.UserProfileDao;
import com.company.surv.model.UserProfile;
import com.company.surv.resource.SignInRequest;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil {

	private static String SECRET = "surv";

	@Autowired
	private UserProfileDao userProfileDao;

	/**
	 * validate the request credentials and generate a valid token
	 * 
	 * @param signInRequest
	 * @return valid token
	 * @throws Throwable
	 */
	public String generateToken(SignInRequest signInRequest) throws Throwable {

		UserProfile foundUser = userProfileDao.findByEmailAndPassword(signInRequest.getEmail(),
				signInRequest.getPassWord());

		if (foundUser != null) {
			Calendar calendar = Calendar.getInstance();

			Date createdTime = calendar.getTime();
			System.out.println("created Time : " + createdTime);

			calendar.add(Calendar.DATE, 1);
			Date expiretime = calendar.getTime();
			System.out.println("expiry Time : " + expiretime);

			Map<String, Object> claims = new HashMap<>();
			claims.put("test", "test");
			claims.put("test1", "test1");
			
			
			return Jwts.builder()//
					.setClaims(claims)//
					.setSubject(foundUser.getEmail())//
					.setIssuedAt(createdTime)//
					.setExpiration(expiretime)//
					.signWith(SignatureAlgorithm.HS256, SECRET)//
					.compact();

		} else {
			throw new Exception("userprofile notfound");
		}
	}

	public boolean validateToken(String token) {

		String email = getUserEmailFromToken(token);
		UserProfile foundUser = userProfileDao.findByEmail(email);
		boolean userFound = false;
		if (foundUser != null)  {
			userFound = true;
		}

		return userFound && !isTokenExpired(token);

	}

	private boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}


	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
	}

	public String getUserEmailFromToken(String authToken) {
		return getClaimFromToken(authToken, Claims::getSubject);
	}
}
