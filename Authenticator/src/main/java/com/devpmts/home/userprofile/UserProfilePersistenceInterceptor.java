package com.devpmts.home.userprofile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public class UserProfilePersistenceInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	UserProfilePersistence userProfilePersistence;

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		userProfilePersistence.persistGoogleProfile();
	}

}
