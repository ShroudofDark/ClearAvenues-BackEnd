package edu.odu.clearavenues.prototype;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// This is the root of the application

@SpringBootApplication
public class ClearAvenuesApplication {

	private static final Logger log = LoggerFactory.getLogger(ClearAvenuesApplication.class);

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				// Should change origin later after hosting on site
				registry.addMapping("/**").allowedOrigins("*")
						.allowedMethods("GET","POST","PUT","DELETE")
						.allowedHeaders("*");
			}
		};
	}
	public static void main(String[] args) {

		SpringApplication.run(ClearAvenuesApplication.class, args);


		// All unneeded now. Just leaving this commented for future reference

		//UserDatabaseActions dbConnection = new UserDatabaseActions();
		/* ArrayList<String> emails = dbConnection.getEmails();
		System.out.println("Emails:");
		for(String email : emails) {
			System.out.println("\t" + email);
		}*/
		
		//boolean authorized = dbConnection.isAuthorized("fm");
		//System.out.println(authorized);
		
//		dbConnection.createNewUser("mwils031@odu.edu_duplicate1", "Matthew Wilson", "password1", "maintainer");


		//dbConnection.resetPassword("mwils031@odu.edu", "password1", "test1");

	}
}