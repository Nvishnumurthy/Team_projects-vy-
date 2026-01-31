package com.spring.restapi_demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.restapi_demo.exception.CloudVendorNotFoundException;
import com.spring.restapi_demo.model.CloudVendor;
import com.spring.restapi_demo.repository.CloudVendorRepository;
import com.spring.restapi_demo.service.CloudVendorService;

// in this class Service logic is written in these defined methods
//@Service - marks this class as service layer bean
@Service
public class CloudVendorServiceImpl	implements CloudVendorService {
	
	@Autowired
	CloudVendorRepository cloudVendorRepository;
	
	public CloudVendorServiceImpl(CloudVendorRepository cloudVendorRepository) {
		this.cloudVendorRepository = cloudVendorRepository;
	}

	@Override
	public String createCloudVendor(CloudVendor cloudVendor) {
		cloudVendorRepository.save(cloudVendor);
		return "Success";
	}

	@Override
	public String updateCloudVendor(CloudVendor cloudVendor) {
		cloudVendorRepository.save(cloudVendor);
		return "Updated Successfully";
	}

	@Override
	public String deleteCloudVendor(String cloudVendorId) {
		cloudVendorRepository.deleteById(cloudVendorId);
		return "Deleted Successfully";
	}

	@Override
	public CloudVendor getCloudVendor(String cloudVendorId) {
		if(cloudVendorRepository.findById(cloudVendorId).isEmpty())
			throw new CloudVendorNotFoundException("Requested Cloud Vendor"
					+ " does not exist");
		return cloudVendorRepository.findById(cloudVendorId).get();
		
	}

	@Override
	public List<CloudVendor> getAllCloudVendors() {
		return cloudVendorRepository.findAll();		
	}
		
}
