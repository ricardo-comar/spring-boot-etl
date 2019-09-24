package com.github.ricardocomar.springdataflowetl.etlconsumer.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.github.ricardocomar.apachecameletl.camelconsumer.producer.ReturnProducer;
import com.github.ricardocomar.apachecameletl.camelconsumer.service.MessageProcessor;
import com.github.ricardocomar.apachecameletl.model.ResponseMessage;

@Component
public class MessageConsumer {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageConsumer.class);

	@Autowired
	private MessageProcessor processor;
	
	@Autowired
	private ReturnProducer producer;

	@JmsListener(destination = "queue.sample")
	public void handle(final String message) {

		LOGGER.info("Received Message ({})", message);
		
		final ResponseMessage response = processor.process(message);
		LOGGER.info("Message Processed: ({})", response);
		
		producer.sendMessage(response);
	}
}
