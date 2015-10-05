package com.devpmts.home.userprofile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.stereotype.Component;

@Component
public class UserProfilePersistence {

	@Autowired
	private GoogleProfileRepository googleProfileRepo;

	@Autowired
	private OAuth2RestOperations oauth2RestTemplate;

	public void persistGoogleProfile() {
		OAuth2AccessToken accessToken = oauth2RestTemplate.getAccessToken();
		if (accessToken == null || accessToken.getRefreshToken() == null) {
			return;
		}
		// this is the first authentication, including a refreshtoken.
		OAuth2RefreshToken refreshToken = accessToken.getRefreshToken();
		GoogleProfile profile = getGoogleProfile();
		profile.setAccessToken(accessToken.getValue());
		profile.setRefreshToken(refreshToken.getValue());
		profile.setExpiration(accessToken.getExpiration().getTime());
		googleProfileRepo.save(profile);
	}

	private GoogleProfile getGoogleProfile() {
		String url = "https://www.googleapis.com/oauth2/v2/userinfo?access_token="
				+ oauth2RestTemplate.getAccessToken();
		ResponseEntity<GoogleProfile> forEntity = oauth2RestTemplate.getForEntity(url, GoogleProfile.class);
		return forEntity.getBody();
	}
}
