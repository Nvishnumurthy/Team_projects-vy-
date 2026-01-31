package com.spring.restapi_demo;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//import com.spring.restapi_demo.model.CloudVendor;
//import com.spring.restapi_demo.repository.CloudVendorRepository;

//import lombok.val;
/*
 * @SpringBootApplication 
 * above annotation is used for combination of
 *  @Configuration-marks the class as source
 *  @EnableAutoConfiguration-enable spring boot auto configuration
 *  @ComponentScan-performs the component for the package and its 
 *  - sub-packages for components like @RestController,@Service,etc.
 */
@SpringBootApplication
public class RestapiDemoApplication {
	//added at the above class
	//implements CommandLineRunner
	/*
	 * CommandLineRunner- lets you run code after the application starts
	 */
/*
 * @Autowired- is used to access the database
 */
//	@Autowired
//	CloudVendorRepository cloudVendorRepository;
	
	public static void main(String[] args) {
		//below line runs the spring boot application by using run()
		SpringApplication.run(RestapiDemoApplication.class, args);
	}
//	
//	@Override
//	public void run(String... args) {
//		
//		CloudVendor cv1 = new CloudVendor
//				("Ram","Ameerpet","9898452306");
//		
//		CloudVendor cv2 = new CloudVendor
//				("Anil","Balanagr","9999966666");
//		
//		CloudVendor cv3 = new CloudVendor
//				("Murali","Saroor nagar","7878994523");
//		
//		CloudVendor cv4 = new CloudVendor
//				("Vijay","Kphb","4567811223");
//		
//		cloudVendorRepository.save(cv1);
//		cloudVendorRepository.save(cv2);
//		cloudVendorRepository.save(cv3);
//		cloudVendorRepository.save(cv4);
//
//		cloudVendorRepository.findByVendorName("Ram")
//		      .forEach(val -> System.out.println(val));
//		cloudVendorRepository.findByVendorAddress("Kphb")
//				.forEach(val -> System.out.println(val));
//		cloudVendorRepository.findByVendorPhoneNumber("9999966666")
//				.forEach(val -> System.out.println(val));
//		cloudVendorRepository.findById(2)
//				.ifPresent(val -> System.out.println(val));
//		
//		System.out.println(cloudVendorRepository.count());
//		
//		
//	}
}
