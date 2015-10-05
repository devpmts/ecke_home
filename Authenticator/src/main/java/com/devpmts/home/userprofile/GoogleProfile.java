package com.devpmts.home.userprofile;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
@Entity
@Table(name = "google_profile")
public class GoogleProfile {

	// public static GoogleProfile DUMMY_PROFILE = new GoogleProfile();
	//
	// static {
	// DUMMY_PROFILE.accessToken = "nope";
	// DUMMY_PROFILE.refreshToken = "nope";
	// }

	@Id
	private String id;

	private String email;

	@JsonProperty("verified_email")
	private Boolean verifiedEmail;

	private String name;

	@JsonProperty("given_name")
	private String givenName;

	@JsonProperty("family_name")
	private String familyName;

	private String link;

	private String picture;

	private String gender;

	private Long expiration;

	private String locale;

	private String hd;

	@JsonProperty("access_token")
	private String accessToken;

	@JsonProperty("refresh_token")
	private String refreshToken;

}
