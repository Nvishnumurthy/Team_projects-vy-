package com.studentcourse.enrollmentapp.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.studentcourse.enrollmentapp.dto.UserInfoDto;
import com.studentcourse.enrollmentapp.mapper.UserInfoMapper;
import com.studentcourse.enrollmentapp.model.UserInfo;
import com.studentcourse.enrollmentapp.repository.UserInfoRepository;
import com.studentcourse.enrollmentapp.service.UserInfoService;

@Service
public class UserInfoServiceImpl implements UserInfoService{
	
	UserInfoRepository userInfoRepository;
	public PasswordEncoder passwordEncoder;
	public UserInfoServiceImpl(UserInfoRepository userInfoRepository,
			PasswordEncoder passwordEncoder) {
		this.userInfoRepository=userInfoRepository;
		this.passwordEncoder=passwordEncoder;
	}
	
	@Override
	public UserInfoDto createUser(UserInfoDto userInfoDto) {
		UserInfo userInfo = UserInfoMapper.toEntity(userInfoDto);
		userInfo.setPassword(passwordEncoder
				.encode(userInfo.getPassword()));
		userInfoRepository.save(userInfo);
		return UserInfoMapper.toDto(userInfo);
	}

}
