package com.binar.grab;
//import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import com.binar.grab.controller.fileupload.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties({
		FileStorageProperties.class
})
//@OpenAPIDefinition
//@EnableSwagger2
@EnableScheduling
public class GrabApplication {

	public static void main(String[] args) {
		SpringApplication.run(GrabApplication.class, args);
	}

}
