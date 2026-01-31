package com.studentcourse.enrollmentapp.mapper;

import com.studentcourse.enrollmentapp.dto.UserInfoDto;
import com.studentcourse.enrollmentapp.model.UserInfo;

public class UserInfoMapper {

	public static UserInfoDto toDto(UserInfo userInfo) {
		return new UserInfoDto(userInfo.getUserName(),
				userInfo.getPassword(),
				userInfo.getRoles());
	}
	
	public static UserInfo toEntity(UserInfoDto userInfoDto) {
		return new UserInfo(userInfoDto.userName(),
				userInfoDto.password(),
				userInfoDto.roles()); 		 
	}
}
