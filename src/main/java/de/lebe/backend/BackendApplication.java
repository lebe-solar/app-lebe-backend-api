package de.lebe.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import com.azure.spring.data.cosmos.repository.config.EnableCosmosRepositories;

@SpringBootApplication
@EnableFeignClients
@EnableAutoConfiguration
@EnableCosmosRepositories
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}
