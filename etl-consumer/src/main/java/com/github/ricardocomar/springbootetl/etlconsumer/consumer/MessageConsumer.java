package com.github.ricardocomar.springbootetl.etlconsumer.consumer;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import com.github.ricardocomar.springbootetl.etlconsumer.config.AppProperties;
import com.github.ricardocomar.springbootetl.etlconsumer.processor.MessageProcessor;

@Component
public class MessageConsumer {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageConsumer.class);

	private static final Random RANDOM = new Random();

	@Autowired
	private MessageProcessor processor;

	@JmsListener(destination = "queue.sample")
	public void handle(final String message,
			@Header(AppProperties.HEADER_REQUEST_ID) final String requestId) {

		try {
			LOGGER.info("Received Message, will be processed ({})", message);
			try {
				Thread.sleep(50 + RANDOM.nextInt(50));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			processor.process(message, requestId);
		} catch (final Exception e) {
			LOGGER.error("Error processing message", e);
		}
	}
}
