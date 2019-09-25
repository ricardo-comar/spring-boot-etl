package com.github.ricardocomar.springdataflowetl.etlconsumer.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.github.ricardocomar.springdataflowetl.etlconsumer.processor.MessageProcessor;

@Component
public class MessageConsumer {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageConsumer.class);

	@Autowired
	private MessageProcessor processor;

	@JmsListener(destination = "queue.sample")
	public void handle(final String message) {

		try {
			LOGGER.info("Received Message, will be processed ({})", message);
			processor.process(message);
		} catch (final Exception e) {
			LOGGER.error("Error processing message", e);
		}
	}
}
