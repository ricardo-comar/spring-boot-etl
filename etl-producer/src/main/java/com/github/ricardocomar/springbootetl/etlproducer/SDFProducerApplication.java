package com.github.ricardocomar.springbootetl.etlproducer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.kafka.annotation.EnableKafka;

import com.github.ricardocomar.springbootetl.etlproducer.config.AppProperties;

@EnableKafka
@EnableDiscoveryClient
@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class SDFProducerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SDFProducerApplication.class, args);
	}

}
