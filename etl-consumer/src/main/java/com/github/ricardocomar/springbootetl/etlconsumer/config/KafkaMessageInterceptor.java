package com.github.ricardocomar.springbootetl.etlconsumer.config;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.MDC;

public class KafkaMessageInterceptor implements ProducerInterceptor<String, Serializable> {

	@Override
	public void configure(final Map<String, ?> configs) {
	}

	@Override
	public ProducerRecord<String, Serializable> onSend(final ProducerRecord<String, Serializable> record) {

		Optional.ofNullable(MDC.get(AppProperties.PROP_CORRELATION_ID))
				.ifPresent(
						(c) -> record.headers().add(AppProperties.HEADER_CORRELATION_ID,
								c.getBytes(StandardCharsets.UTF_8)));
		return record;
	}

	@Override
	public void onAcknowledgement(final RecordMetadata metadata, final Exception exception) {
	}

	@Override
	public void close() {
	}

}
