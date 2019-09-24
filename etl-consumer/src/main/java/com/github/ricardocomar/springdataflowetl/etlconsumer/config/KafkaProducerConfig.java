package com.github.ricardocomar.springdataflowetl.etlconsumer.config;

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

import com.github.ricardocomar.springdataflowetl.model.ResponseMessage;

@Configuration
public class KafkaProducerConfig {
	@Bean @Lazy
	public ProducerFactory<String, ResponseMessage> producerFactory(
			@Autowired final KafkaProperties kafkaProps) {
		final DefaultKafkaProducerFactory<String, ResponseMessage> producerFactory = new DefaultKafkaProducerFactory<>(
				kafkaProps.buildProducerProperties(), new StringSerializer(), new JsonSerializer<ResponseMessage>());
		return producerFactory;
	}

	@Bean @Lazy
	public KafkaTemplate<String, ResponseMessage> kafkaTemplate(
			@Autowired final ProducerFactory<String, ResponseMessage> producerFactory) {
		return new KafkaTemplate<>(producerFactory);
	}
}