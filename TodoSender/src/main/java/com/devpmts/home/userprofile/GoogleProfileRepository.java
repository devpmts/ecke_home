package com.devpmts.home.userprofile;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.data.repository.Repository;

import com.devpmts.home.services.todosender.TodoEnv;

public interface GoogleProfileRepository extends Repository<GoogleProfile, String> {

	GoogleProfile save(GoogleProfile profile);

	List<GoogleProfile> findAll();

	Optional<GoogleProfile> findOne(String id);

	Optional<GoogleProfile> findByEmail(String email);

	default Optional<GoogleProfile> emailSenderProfile() {
		Optional<GoogleProfile> profile = findByEmail(TodoEnv.SMTP_OWNER);
		if (!profile.isPresent()) {
			Logger.getLogger(this.getClass().getName())
					.warning("no suitable profile found, please connect application and start again. returning dummy");
		}
		return profile;
	}

}
