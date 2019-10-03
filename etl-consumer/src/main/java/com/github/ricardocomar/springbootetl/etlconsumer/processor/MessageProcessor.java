package com.github.ricardocomar.springbootetl.etlconsumer.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.ricardocomar.springbootetl.etlconsumer.model.Team;
import com.github.ricardocomar.springbootetl.etlconsumer.producer.ReturnProducer;
import com.github.ricardocomar.springbootetl.etlconsumer.transformer.AvroTransformer;
import com.github.ricardocomar.springbootetl.etlconsumer.transformer.TrancodeTransformer;
import com.github.ricardocomar.springbootetl.etlconsumer.validation.ValidatorTeam;

import br.com.fluentvalidator.context.ValidationResult;

@Component
public class MessageProcessor {

	@Autowired
	private TrancodeTransformer trancodeTransformer;

	@Autowired
	private AvroTransformer avroTransformer;

	@Autowired
	private ValidatorTeam validatorTeam;

	@Autowired
	private ReturnProducer producer;

	private static final Logger LOGGER = LoggerFactory.getLogger(MessageProcessor.class);

	public void process(final String message, final String requestId) {
		LOGGER.info("Processing message: {}", message);
		
		final Team teamTrancode = trancodeTransformer.from(message);
		LOGGER.info("Message transformed into bean: {}", teamTrancode);
		
		final ValidationResult validationResult = validatorTeam.validate(teamTrancode);
		LOGGER.info("Validation result: {}", validationResult);
		
//		final TeamAvro teamAvro = avroTransformer.from(teamTrancode);
//		LOGGER.info("Bean transformed into response: {}", teamAvro);
//
//		LOGGER.info("Sending bean to response topic");
//		producer.sendMessage(teamAvro, requestId);
	}

}
