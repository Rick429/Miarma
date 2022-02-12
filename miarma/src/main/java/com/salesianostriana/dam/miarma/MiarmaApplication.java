package com.salesianostriana.dam.miarma;

import com.salesianostriana.dam.miarma.config.StorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(StorageProperties.class)
@SpringBootApplication
public class MiarmaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MiarmaApplication.class, args);
	}

}
