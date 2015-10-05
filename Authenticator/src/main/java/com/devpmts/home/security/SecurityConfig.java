package com.devpmts.home.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import com.devpmts.home.HomeEnv;
import com.devpmts.home.oauth2.google.DefaultUserAuthenticationConverter;
import com.devpmts.home.oauth2.google.GoogleAccessTokenConverter;
import com.devpmts.home.oauth2.google.GoogleTokenServices;

@Configuration
@EnableWebSecurity
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private SecurityProperties security;

	@Autowired
	private OAuth2ClientContextFilter oAuth2ClientContextFilter;

	@Autowired
	private OAuth2ClientAuthenticationProcessingFilter oAuth2ClientAuthenticationProcessingFilter;

	@Autowired
	LoginUrlAuthenticationEntryPoint clientAuthenticationEntryPoint;

	// @Autowired
	// DataSource dataSource;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		http.httpBasic().authenticationEntryPoint(clientAuthenticationEntryPoint)
		.and().anonymous().disable()
		.authorizeRequests().antMatchers("/**").access("isFullyAuthenticated()")
		.and().addFilterAfter(oAuth2ClientContextFilter, ExceptionTranslationFilter.class)
		.addFilterBefore(oAuth2ClientAuthenticationProcessingFilter, FilterSecurityInterceptor.class)
		.logout();
		// @formatter:on
	}

	// @Override
	// @SneakyThrows
	// public void configure(AuthenticationManagerBuilder auth) {
	// auth.jdbcAuthentication().dataSource(dataSource).withDefaultSchema().withUser("user").password("password")
	// .roles("DOMAIN_USER");
	// }

	@Bean
	LoginUrlAuthenticationEntryPoint clientAuthenticationEntryPoint() {
		return new LoginUrlAuthenticationEntryPoint("/googleLogin");
	}

	@Bean
	GoogleTokenServices tokenServices(HomeEnv env) {
		GoogleTokenServices tokenServices = new GoogleTokenServices();
		tokenServices.setCheckTokenEndpointUrl("https://www.googleapis.com/oauth2/v1/tokeninfo");
		tokenServices.setClientId(env.CLIENT_ID);
		tokenServices.setClientSecret(env.CLIENT_SECRET);
		GoogleAccessTokenConverter accessTokenConverter = new GoogleAccessTokenConverter();
		accessTokenConverter.setUserTokenConverter(new DefaultUserAuthenticationConverter());
		tokenServices.setAccessTokenConverter(accessTokenConverter);
		return tokenServices;
	}

	@Bean
	OAuth2ClientAuthenticationProcessingFilter oAuth2ClientAuthenticationProcessingFilter(
			OAuth2RestOperations restTemplate, ResourceServerTokenServices tokenServices) {
		OAuth2ClientAuthenticationProcessingFilter oAuth2ClientAuthenticationProcessingFilter = new OAuth2ClientAuthenticationProcessingFilter(
				"/googleLogin");
		oAuth2ClientAuthenticationProcessingFilter.setTokenServices(tokenServices);
		oAuth2ClientAuthenticationProcessingFilter.setRestTemplate(restTemplate);
		return oAuth2ClientAuthenticationProcessingFilter;
	}

}
