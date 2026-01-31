package com.spring.restapi_demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.spring.restapi_demo.model.CloudVendor;
//this class for extending spring boot jpa for db operations
@Repository
public interface CloudVendorRepository 
				extends JpaRepository<CloudVendor,String>{
	
		List<CloudVendor> findByVendorName(String name);
		List<CloudVendor> findByVendorPhoneNumber(String PhoneNumber);
		List<CloudVendor> findByVendorAddress(String Address);
		
}
