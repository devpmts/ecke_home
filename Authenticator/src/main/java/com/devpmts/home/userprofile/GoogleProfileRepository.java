package com.devpmts.home.userprofile;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.Repository;

public interface GoogleProfileRepository extends Repository<GoogleProfile, String> {

	GoogleProfile save(GoogleProfile profile);

	List<GoogleProfile> findAll();

	Optional<GoogleProfile> findOne(String id);

	Optional<GoogleProfile> findByEmail(String email);

}
