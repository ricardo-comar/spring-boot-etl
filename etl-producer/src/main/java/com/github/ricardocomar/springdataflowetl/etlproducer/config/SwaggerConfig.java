package com.github.ricardocomar.springdataflowetl.etlproducer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.ricardocomar.springdataflowetl.etlproducer.entrypoint.ProcessController;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors
						.basePackage(ProcessController.class.getPackage().getName()))
				.paths(PathSelectors.any()).build().apiInfo(apiInfo());
	}

	private final ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Simple Spring Boot REST API")
				.description("Um exemplo de aplicação Spring Boot REST API").version("1.0.0")
				.license("Apache License Version 2.0").licenseUrl("https://www.apache.org/licenses/LICENSE-2.0")
				.license("Splunk Monitor").licenseUrl("http://localhost:8000/en-US/app/launcher/home")
				.contact(new Contact("Ricardo Comar", "https://github.com/ricardo-comar", "rhcomar@yahoo.com.br"))
				.build();

	}
}
