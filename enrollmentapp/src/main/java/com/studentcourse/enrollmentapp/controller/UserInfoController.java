package com.studentcourse.enrollmentapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.studentcourse.enrollmentapp.dto.UserInfoDto;
import com.studentcourse.enrollmentapp.service.UserInfoService;

@RestController
@RequestMapping("/user-info")
public class UserInfoController {
	
	UserInfoService userInfoService;
	public UserInfoController(UserInfoService userInfoService) {
		this.userInfoService=userInfoService;
	}
	
	@PostMapping("/register")
	public ResponseEntity<String> 
					createUserInfo(@RequestBody UserInfoDto userInfoDto){
		UserInfoDto userInfoDto1 = userInfoService.createUser(userInfoDto);
		return new ResponseEntity<String>("User "+userInfoDto1.userName()
				+" Created",HttpStatus.CREATED
				);
	}

}
