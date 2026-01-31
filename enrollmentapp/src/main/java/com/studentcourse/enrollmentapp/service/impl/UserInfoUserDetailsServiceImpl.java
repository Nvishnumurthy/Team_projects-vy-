package com.studentcourse.enrollmentapp.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.studentcourse.enrollmentapp.mapper.UserInfoUserDetailsMapper;
import com.studentcourse.enrollmentapp.model.UserInfo;
import com.studentcourse.enrollmentapp.repository.UserInfoRepository;

@Service
public class UserInfoUserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	private UserInfoRepository userInfoRepository;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<UserInfo> userInfo = userInfoRepository.findByUserName(username);
		return userInfo.map(UserInfoUserDetailsMapper::new)
				.orElseThrow(()-> new UsernameNotFoundException("User "+
		 username +" not found"));
	}
	

}
