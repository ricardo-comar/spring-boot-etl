package com.github.ricardocomar.springbootetl.etlconsumer.config;

import java.io.Serializable;

import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
public class KafkaProducerConfig {
	@Bean @Lazy
	public ProducerFactory<String, Serializable> producerFactory(
			@Autowired final KafkaProperties kafkaProps) {
		final DefaultKafkaProducerFactory<String, Serializable> producerFactory = new DefaultKafkaProducerFactory<>(
				kafkaProps.buildProducerProperties(), new StringSerializer(), new JsonSerializer<Serializable>());
		return producerFactory;
	}

	@Bean @Lazy
	public KafkaTemplate<String, Serializable> kafkaTemplate(
			@Autowired final ProducerFactory<String, Serializable> producerFactory) {
		return new KafkaTemplate<>(producerFactory);
	}
}