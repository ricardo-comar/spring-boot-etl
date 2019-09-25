package com.github.ricardocomar.springbootetl.etlconsumer.producer;

import java.io.Serializable;

import org.owasp.security.logging.SecurityMarkers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ReturnProducer {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReturnProducer.class);

	@Autowired
	private KafkaTemplate<String, Serializable> kafkaTemplate;
	
	@Value("topicOutbound")
	private String topicName;
	
	public void sendMessage(final Serializable message) {
		LOGGER.info(SecurityMarkers.CONFIDENTIAL, "Sending message to topic {}", topicName);
		kafkaTemplate.send(topicName, message);
	}
	
	 
}
