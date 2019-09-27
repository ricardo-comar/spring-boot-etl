package com.github.ricardocomar.springbootetl.etlproducer.config;

import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.RecordInterceptor;

@Configuration
public class KafkaConsumerConfig {
	
	@Autowired
	private AppProperties appProps;

	@Bean
	public ConsumerFactory<?, ?> consumerFactory(
			@Autowired final KafkaProperties kafkaProps) {
		return new DefaultKafkaConsumerFactory<>(
				kafkaProps.buildConsumerProperties());
	}

	@Bean
	public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, GenericRecord>> kafkaListenerContainerFactory(
			@Autowired final ConsumerFactory<String, GenericRecord> consumerFactory) {
		
		final ConcurrentKafkaListenerContainerFactory<String, GenericRecord> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory);
		factory.setRecordInterceptor(recordInterceptor());
		factory.setConcurrency(appProps.getConsumer().getContainerFactory().getConcurrency());
		factory.getContainerProperties()
				.setPollTimeout(appProps.getConsumer().getContainerFactory().getProperties().getPoolTimeout());
//TODO: reativar
//		factory.setRecordFilterStrategy(
//				record -> !appProps.getInstanceId().equals(record.value().getOrigin()));
		return factory;
	}

	private static RecordInterceptor<String, GenericRecord> recordInterceptor() {

		return new RecordInterceptor<String, GenericRecord>() {

			@Override
			public ConsumerRecord<String, GenericRecord> intercept(final ConsumerRecord<String, GenericRecord> record) {

				record.headers().headers(AppProperties.HEADER_CORRELATION_ID).forEach((h) -> {
					MDC.put(AppProperties.PROP_CORRELATION_ID, new String(h.value()));
				});
				return record;
			}
		};
	}
}
