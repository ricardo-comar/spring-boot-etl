package com.github.ricardocomar.springbootetl.etlconsumer.config;

import org.apache.avro.specific.SpecificRecord;
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
	public KafkaTemplate<String, SpecificRecord> kafkaTemplate(
			@Autowired final ProducerFactory<String, SpecificRecord> producerFactory) {
		return new KafkaTemplate<>(producerFactory);
	}
}