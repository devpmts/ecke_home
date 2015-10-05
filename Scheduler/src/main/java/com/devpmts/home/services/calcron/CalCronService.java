package com.devpmts.home.services.calcron;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.SneakyThrows;
import lombok.extern.java.Log;

@Configuration
@EnableScheduling
@Log
public class CalCronService {

	@SneakyThrows
	@Scheduled(cron = "* */15 * * * *")
	private void runCalCronNodeApp() {
		URI uri = buildCalcronTriggerUri();
		try {
			Object response = new RestTemplate().getForObject(uri, Object.class);
			log.info(response.toString());
		} catch (Exception e) {
			log.warning("Error reaching calCron target" + e.getMessage());
		}
	}

	private URI buildCalcronTriggerUri() throws URISyntaxException {
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();
		uriBuilder.uri(new URI(CalCronEnv.CALCRON_TRIGGER_URL));
		return uriBuilder.build().toUri();
	}

}
