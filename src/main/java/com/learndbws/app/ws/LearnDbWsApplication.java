package com.learndbws.app.ws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.learndbws.app.ws.security.AppProperties;

@SpringBootApplication
public class LearnDbWsApplication extends SpringBootServletInitializer {

	//for WAR files
	/*@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application){
		return application.sources(LearnDbWsApplication.class);
	}*/

	public static void main(String[] args) {
		SpringApplication.run(LearnDbWsApplication.class, args);
	}
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public SpringApplicationContext springApplicationContext(){
		return new SpringApplicationContext();
	}
	
	@Bean(name="AppProperties")
	public AppProperties getAppProperties() {
		return new AppProperties();
	}
}
