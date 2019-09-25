package com.github.ricardocomar.springbootetl.etlconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import com.github.ricardocomar.springbootetl.etlconsumer.config.AppProperties;

@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigurationProperties(AppProperties.class)
public class SDFConsumerApplication {

	public static void main(final String[] args) {
		SpringApplication.run(SDFConsumerApplication.class);
	}

}
