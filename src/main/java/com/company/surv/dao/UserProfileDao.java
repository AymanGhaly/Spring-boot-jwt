package com.company.surv.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.company.surv.model.UserProfile;

public interface UserProfileDao extends JpaRepository<UserProfile, Long> {

	@Query("from UserProfile up where up.fristName =:email And up.passWord = :passWord")
	public UserProfile findByEmailAndPassword(@Param("email") String email, @Param("passWord") String passWord);


	
	
	@Query("from UserProfile up where up.email =:email")
	public UserProfile findByEmail(@Param("email") String email);

}