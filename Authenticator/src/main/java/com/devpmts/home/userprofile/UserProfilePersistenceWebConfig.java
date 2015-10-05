package com.devpmts.home.userprofile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class UserProfilePersistenceWebConfig extends WebMvcConfigurerAdapter {

	@Autowired
	UserProfilePersistenceInterceptor userProfilePersistenceInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(userProfilePersistenceInterceptor).addPathPatterns("/**");
	}

}
