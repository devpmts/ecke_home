package com.devpmts.database;

import java.sql.SQLException;

import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class H2ConsoleWebServlet {

	@Autowired
	Server h2WebServer;

	@Bean
	public Server h2WebServer() throws SQLException {
		return Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();
	}

	// @Bean
	// ServletRegistrationBean h2servletRegistration() {
	// ServletRegistrationBean registrationBean = new
	// ServletRegistrationBean(new WebServlet());
	// registrationBean.addUrlMappings("/console/*");
	// return registrationBean;
	// }
}
