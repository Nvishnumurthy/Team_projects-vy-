package com.studentcourse.enrollmentapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
		
//	@Bean
//	public UserDetailsService userDetailsService() {
//		UserDetails userDetailsOne = User.withUsername("vishnu1")
//					.password(passwordEncoder().encode("1234")).
//					roles("USER").build();
//		UserDetails admin =User.withUsername("admin")
//					.password(passwordEncoder().encode("mokila1234#"))
//					.roles("ADMIN").build();
//		
//		return new InMemoryUserDetailsManager(userDetailsOne,admin);
//		
//	}
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf(csrfCustomizer -> csrfCustomizer.disable());
		httpSecurity.authorizeHttpRequests(request ->
						request.requestMatchers("/students/home","/user-info/register").permitAll()
						.anyRequest().authenticated()
				);
		httpSecurity.httpBasic(org.springframework.security.config.Customizer.withDefaults());
		httpSecurity.sessionManagement(Session ->
		   			Session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				);
		return httpSecurity.build(); 
	}
	@SuppressWarnings("deprecation")
	@Bean
	public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
	    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
	    provider.setUserDetailsService(userDetailsService);
	    provider.setPasswordEncoder(passwordEncoder());
	    return provider;
	}

}
