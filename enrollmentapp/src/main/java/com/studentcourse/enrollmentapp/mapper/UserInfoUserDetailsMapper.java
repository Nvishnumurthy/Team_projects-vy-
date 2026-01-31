package com.studentcourse.enrollmentapp.mapper;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.studentcourse.enrollmentapp.model.UserInfo;

@SuppressWarnings("serial")
public class UserInfoUserDetailsMapper implements UserDetails{
		private String userName;
		private  String password;
		private List<GrantedAuthority> grantedAuthorities;
		
		public UserInfoUserDetailsMapper(UserInfo userInfo) {
			userName=userInfo.getUserName();
			password=userInfo.getPassword();
			grantedAuthorities = Arrays
					.stream(userInfo.getRoles().split(","))
					.map(SimpleGrantedAuthority::new)
					.collect(Collectors.toList());
		}
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
				return grantedAuthorities;
	}
	
	@Override
	public String getPassword() {
				return password;
	}

	@Override
	public String getUsername() {
		return userName;
	}

}
