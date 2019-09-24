package com.github.ricardocomar.springdataflowetl.etlproducer.config;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.RecordInterceptor;

import com.github.ricardocomar.springdataflowetl.model.ResponseMessage;

@Configuration
public class KafkaConsumerConfig {
	
	@Autowired
	private AppProperties appProps;

	@Bean
	public ConsumerFactory<String, String> consumerFactory(
			@Autowired final KafkaProperties kafkaProps) {
//TODO: reativar final JsonDeserializer<Object> jsonDeserializer = new JsonDeserializer<>();
//		jsonDeserializer.addTrustedPackages("*");
		return new DefaultKafkaConsumerFactory<>(
				kafkaProps.buildConsumerProperties(), new StringDeserializer(),
//TODO: reativar				jsonDeserializer);
				new StringDeserializer());
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
			final ConsumerFactory<String, String> consumerFactory) {
		final ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory);
//TODO: reativar		factory.setRecordInterceptor(recordInterceptor());
		factory.setConcurrency(appProps.getConsumer().getContainerFactory().getConcurrency());
		factory.getContainerProperties()
				.setPollTimeout(appProps.getConsumer().getContainerFactory().getProperties().getPoolTimeout());
//TODO: reativar		factory.setRecordFilterStrategy(
//				record -> !appProps.getInstanceId().equals(record.value().getOrigin()));
		return factory;
	}

	private static RecordInterceptor<String, ResponseMessage> recordInterceptor() {

		return new RecordInterceptor<String, ResponseMessage>() {

			@Override
			public ConsumerRecord<String, ResponseMessage> intercept(
					final ConsumerRecord<String, ResponseMessage> record) {

				record.headers().headers(AppProperties.HEADER_CORRELATION_ID).forEach((h) -> {
					MDC.put(AppProperties.PROP_CORRELATION_ID, new String(h.value()));
				});
				return record;
			}
		};
	}
}
