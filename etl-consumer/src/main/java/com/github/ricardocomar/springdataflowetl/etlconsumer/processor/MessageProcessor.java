package com.github.ricardocomar.springdataflowetl.etlconsumer.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.ricardocomar.springdataflowetl.etlconsumer.model.avro.TeamAvro;
import com.github.ricardocomar.springdataflowetl.etlconsumer.model.trancode.TeamTrancode;
import com.github.ricardocomar.springdataflowetl.etlconsumer.producer.ReturnProducer;
import com.github.ricardocomar.springdataflowetl.etlconsumer.transformer.TeamAvroTransformer;
import com.github.ricardocomar.springdataflowetl.etlconsumer.transformer.TeamTrancodeTransformer;
import com.github.ricardocomar.springdataflowetl.etlconsumer.validation.ValidatorTeam;

import br.com.fluentvalidator.context.ValidationResult;

@Component
public class MessageProcessor {

	@Autowired
	private TeamTrancodeTransformer trancodeTransformer;

	@Autowired
	private TeamAvroTransformer avroTransformer;

	@Autowired
	private ValidatorTeam validatorTeam;

	@Autowired
	private ReturnProducer producer;

	private static final Logger LOGGER = LoggerFactory.getLogger(MessageProcessor.class);

	public void process(final String message) {
		LOGGER.info("Processing message: {}", message);
		
		final TeamTrancode teamTrancode = trancodeTransformer.from(message);
		LOGGER.info("Message transformed into bean: {}", teamTrancode);
		
		final ValidationResult validationResult = validatorTeam.validate(teamTrancode);
		LOGGER.info("Validation result: {}", validationResult);
		
		final TeamAvro teamAvro = avroTransformer.from(teamTrancode);
		LOGGER.info("Bean transformed into response: {}", teamAvro);

		LOGGER.info("Sending bean to response topic");
		producer.sendMessage(teamAvro);
	}

}
