package com.github.ricardocomar.springbootetl.etlproducer.consumer;

import org.apache.avro.generic.GenericRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ricardocomar.springbootetl.etlproducer.config.AppProperties;
import com.github.ricardocomar.springbootetl.etlproducer.service.model.MessageEvent;

@Component
public class ResponseConsumer {
	
	private static final ObjectMapper MAPPER = new ObjectMapper()
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	private static final Logger LOGGER = LoggerFactory.getLogger(ResponseConsumer.class);
	
	@Autowired
	private ApplicationContext appContext;

	@KafkaListener(topics = "topicOutbound", groupId = "producer-${random.value}")
	public void consumeResponse(@Payload final GenericRecord message,
			@Header(required = false, name = AppProperties.HEADER_REQUEST_ID) final String requestId)
			throws Exception {

		LOGGER.debug("Received Message: {}", message);
		LOGGER.debug("Received RequestId: {}", requestId);
		
		appContext.publishEvent(new MessageEvent(requestId, message));
		
	}
}
