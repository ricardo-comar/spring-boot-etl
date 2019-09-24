package com.github.ricardocomar.springdataflowetl.etlconsumer.config;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.MDC;

import com.github.ricardocomar.springdataflowetl.model.ResponseMessage;

public class KafkaMessageInterceptor implements ProducerInterceptor<String, ResponseMessage> {

	@Override
	public void configure(final Map<String, ?> configs) {
	}

	@Override
	public ProducerRecord<String, ResponseMessage> onSend(final ProducerRecord<String, ResponseMessage> record) {

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
