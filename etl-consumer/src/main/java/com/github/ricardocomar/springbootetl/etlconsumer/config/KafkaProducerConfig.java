package com.github.ricardocomar.springbootetl.etlconsumer.config;

import org.apache.avro.generic.GenericRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaProducerConfig {
	@Bean
	public ProducerFactory<?, ?> producerFactory(
			@Autowired final KafkaProperties kafkaProps) {
		return new DefaultKafkaProducerFactory<>(
				kafkaProps.buildProducerProperties());
	}

	@Bean
	public KafkaTemplate<String, GenericRecord> kafkaTemplate(
			@Autowired final ProducerFactory<String, GenericRecord> producerFactory) {
		return new KafkaTemplate<>(producerFactory);
	}
}