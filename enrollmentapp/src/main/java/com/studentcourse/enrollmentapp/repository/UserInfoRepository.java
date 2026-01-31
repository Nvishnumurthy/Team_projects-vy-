package com.studentcourse.enrollmentapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.studentcourse.enrollmentapp.model.UserInfo;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, String>
	{
	Optional<UserInfo> findByUserName(String userName);
}
