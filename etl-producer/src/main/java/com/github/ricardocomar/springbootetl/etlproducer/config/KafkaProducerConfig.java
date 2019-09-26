package com.github.ricardocomar.springbootetl.etlproducer.config;

import java.io.Serializable;

import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
public class KafkaProducerConfig {

	@Bean
	public ProducerFactory<String, Serializable> producerFactory(
			@Autowired final KafkaProperties kafkaProps) {
		return new DefaultKafkaProducerFactory<String, Serializable>(kafkaProps.buildProducerProperties(),
				new StringSerializer(), new JsonSerializer<Serializable>());
	}

	@Bean
	public KafkaTemplate<String, Serializable> kafkaTemplate(
			@Autowired final ProducerFactory<String, Serializable> producerFactory) {
		return new KafkaTemplate<>(producerFactory);
	}
}
