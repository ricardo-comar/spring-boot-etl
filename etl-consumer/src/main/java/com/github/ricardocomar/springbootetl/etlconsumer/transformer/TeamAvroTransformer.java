package com.github.ricardocomar.springbootetl.etlconsumer.transformer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.ricardocomar.springbootetl.etlconsumer.consumer.model.TeamTrancode;
import com.github.ricardocomar.springbootetl.etlconsumer.mapper.TeamMapper;
import com.github.ricardocomar.springbootetl.model.TeamAvro;

@Component
public class TeamAvroTransformer {

	private static final Logger LOGGER = LoggerFactory.getLogger(TeamAvroTransformer.class);

	@Autowired
	private TeamMapper mapper;

	public TeamAvro from(final TeamTrancode input) {
		LOGGER.debug("Transforming trancode into bean: {}", input);

		final TeamAvro output = mapper.trancodeToAvro(input);
		LOGGER.debug("Resulted bean: {}", output);

		return output;
	}

	public TeamTrancode to(final TeamAvro input) {
		LOGGER.debug("Transforming trancode into bean: {}", input);

		final TeamTrancode output = mapper.avroToTrancode(input);
		LOGGER.debug("Resulted bean: {}", output);

		return output;
	}
}
