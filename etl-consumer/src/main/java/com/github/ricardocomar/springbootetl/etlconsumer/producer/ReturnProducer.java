package com.github.ricardocomar.springbootetl.etlconsumer.producer;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.owasp.security.logging.SecurityMarkers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.github.ricardocomar.springbootetl.etlconsumer.config.AppProperties;

@Service
public class ReturnProducer {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReturnProducer.class);

	@Autowired
	private KafkaTemplate<String, SpecificRecord> kafkaTemplate;
	
	@Value("topicOutbound")
	private String topicName;
	
	public void sendMessage(final SpecificRecord message, final String requestId) {
		LOGGER.info(SecurityMarkers.CONFIDENTIAL, "Sending message to topic {}", topicName);

		final List<Header> headers = new ArrayList<>();
		headers.add(new RecordHeader(AppProperties.HEADER_REQUEST_ID, requestId.getBytes(StandardCharsets.UTF_8)));

		final ProducerRecord<String, SpecificRecord> producerRecord = new ProducerRecord<>(topicName, 0, "111", message,
				headers);

		kafkaTemplate.send(producerRecord);
	}
	
	 
}
