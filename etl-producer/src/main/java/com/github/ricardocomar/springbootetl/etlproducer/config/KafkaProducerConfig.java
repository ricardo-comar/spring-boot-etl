package com.github.ricardocomar.springbootetl.etlproducer.config;

import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.github.ricardocomar.springbootetl.model.RequestMessage;

@Configuration
public class KafkaProducerConfig {

	@Bean
	public ProducerFactory<String, RequestMessage> producerFactory(
			@Autowired KafkaProperties kafkaProps) {
		return new DefaultKafkaProducerFactory<String, RequestMessage>(kafkaProps.buildProducerProperties(),
				new StringSerializer(), new JsonSerializer<RequestMessage>());
	}

	@Bean
	public KafkaTemplate<String, RequestMessage> kafkaTemplate(
			@Autowired ProducerFactory<String, RequestMessage> producerFactory) {
		return new KafkaTemplate<>(producerFactory);
	}
}
