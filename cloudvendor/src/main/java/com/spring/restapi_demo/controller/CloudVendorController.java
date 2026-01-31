package com.spring.restapi_demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.spring.restapi_demo.model.CloudVendor;
import com.spring.restapi_demo.response.ResponseHandler;
import com.spring.restapi_demo.service.CloudVendorService;

//this class for exposing rest end points
@RestController
@RequestMapping("/cloudvendor")
public class CloudVendorController {
	
	@Autowired
	CloudVendorService cloudVendorService;

	public CloudVendorController(CloudVendorService cloudVendorService) {
		this.cloudVendorService = cloudVendorService;
	}
		//read specific cloud vendor details
	@GetMapping("{vendorId}")
	public ResponseEntity<Object> getCloudVendorDetails(@PathVariable("vendorId") String vendorId){
		
		return ResponseHandler.responseBuilder("Requested vendor details given here",
				HttpStatus.OK, 
				cloudVendorService.getCloudVendor(vendorId));
		
	}
	
	//read all cloud vendor details
	@GetMapping()
	public List<CloudVendor> getAllCloudVendorDetails() {
		return cloudVendorService.getAllCloudVendors();
	}
		
	@PostMapping
	public String createCloudVendorDetails(@RequestBody CloudVendor cloudVendor) {
		 cloudVendorService.createCloudVendor(cloudVendor);
		 return "Cloud Vendor Created Successfully";
	}
	
	@PutMapping
	public String updateCloudVendorDetails(@RequestBody CloudVendor cloudVendor) {
		cloudVendorService.updateCloudVendor(cloudVendor);
		return "Cloud Vendor Updated Successfully";
	}
	
	@DeleteMapping("{vendorId}")
	public String deleteCloudVendorDetails(@PathVariable("vendorId") String vendorId) {
		cloudVendorService.deleteCloudVendor(vendorId);
		return "Cloud Vendor Deleted Successfully";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
		
}
