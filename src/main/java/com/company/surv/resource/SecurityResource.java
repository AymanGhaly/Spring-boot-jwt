package com.company.surv.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.company.surv.dao.UserProfileDao;
import com.company.surv.model.UserProfile;
import com.company.surv.security.JwtTokenUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * all end-points are protected by tokens
 */
@RestController
@RequestMapping(value = "surv/sucurity")
@Api(value = "SecurityResource", description = "contains all security endpoints")
public class SecurityResource {

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserProfileDao userProfileDao;

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	@ApiOperation(value = "test")
	public ResponseEntity<String> testApi() {
		return ResponseEntity.ok("success");
	}

	@RequestMapping(value = "/signIn", method = RequestMethod.POST)
	@ApiOperation(value = "signIn")
	public ResponseEntity<String> signIn(@RequestBody SignInRequest request) throws Throwable {
		// a proper sign in logic goes her before generating a token
		return ResponseEntity.ok(jwtTokenUtil.generateToken(request));
	}

	@RequestMapping(value = "/signUp", method = RequestMethod.POST)
	@ApiOperation(value = "signUp")
	public ResponseEntity<String> signUp(@RequestBody SignInRequest request) {

		UserProfile userProfile = new UserProfile();
		userProfile.setFristName(request.getEmail());
		userProfile.setLastName(request.getEmail());
		userProfile.setEmail(request.getEmail());
		userProfile.setPassWord(request.getPassWord());
		userProfileDao.save(userProfile);

		return ResponseEntity.ok("success");
	}
}
