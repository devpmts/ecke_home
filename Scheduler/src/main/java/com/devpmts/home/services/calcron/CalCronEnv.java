package com.devpmts.home.services.calcron;

import org.springframework.stereotype.Component;

import com.devpmts.home.HomeEnv;

@Component
public interface CalCronEnv {

	String CALCRON_TRIGGER_URL = HomeEnv.HOME_APP_BASE_URL_LOCAL + ":3000/calcron_trigger";

}
