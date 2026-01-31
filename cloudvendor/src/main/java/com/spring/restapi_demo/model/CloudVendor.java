package com.spring.restapi_demo.model;

import jakarta.persistence.*;
//this class for defining database model
@Entity
@Table(name="cloud_vendor_info")
public class CloudVendor {
	@Id
	private String vendorId;
	private String vendorName;
	private String vendorAddress;
	private String vendorPhoneNumber;
	
	public CloudVendor() {}
	
	public CloudVendor(String vendorId,String vendorName,String vendorAddress,
			String vendorPhoneNumber) {
		this.vendorId=vendorId;
		this.vendorName=vendorName;
		this.vendorAddress=vendorAddress;
		this.vendorPhoneNumber=vendorPhoneNumber;

	}
	public String getVendorId() {
		return vendorId;
	}
	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	public String getVendorAddress() {
		return vendorAddress;
	}
	public void setVendorAddress(String vendorAddress) {
		this.vendorAddress = vendorAddress;
	}
	public String getVendorPhoneNumber() {
		return vendorPhoneNumber;
	}
	public void setVendorPhoneNumber(String vendorPhoneNumber) {
		this.vendorPhoneNumber = vendorPhoneNumber;
	}

	@Override
	public String toString() {
		return "CloudVendor [vendorId=" + vendorId + ", vendorName=" + vendorName + ", vendorAddress=" + vendorAddress
				+ ", vendorPhoneNumber=" + vendorPhoneNumber + "]";
	}
	
	
}
