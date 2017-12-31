package com.company.surv.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.company.surv.dao.UserProfileDao;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserProfileDao userProfileDao;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		return userProfileDao.findByEmail(username);
	}

}
