package com.org.huy.woakbart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * Main class that starts the server.
 * 
 * @author hnguyen10
 *
 */
@SpringBootApplication
public class WoakBartApplication {

	public static void main(String[] args) {
		SpringApplication.run(WoakBartApplication.class, args);
	}
	   
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
