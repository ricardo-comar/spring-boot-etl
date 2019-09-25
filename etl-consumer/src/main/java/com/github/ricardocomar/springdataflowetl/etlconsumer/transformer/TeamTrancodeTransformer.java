package com.github.ricardocomar.springdataflowetl.etlconsumer.transformer;

import java.time.LocalDate;

import org.beanio.Marshaller;
import org.beanio.StreamFactory;
import org.beanio.Unmarshaller;
import org.beanio.builder.FixedLengthParserBuilder;
import org.beanio.builder.StreamBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.github.ricardocomar.springdataflowetl.etlconsumer.model.trancode.TeamTrancode;

@Component
public class TeamTrancodeTransformer {

	private static final Logger LOGGER = LoggerFactory.getLogger(TeamTrancodeTransformer.class);

	private final Unmarshaller unmarsh;
	private final Marshaller marsh;

	public TeamTrancodeTransformer() {

		LOGGER.debug("Creating BeanIO Unmarshaller and Marshaller");

		final StreamBuilder builder = new StreamBuilder("TeamTrancode").format("fixedlength")
				.addTypeHandler(LocalDate.class, new LocalDateTypeHandler("yyyyMMdd"))
				.parser(new FixedLengthParserBuilder()).addRecord(TeamTrancode.class);

		final StreamFactory factory = StreamFactory.newInstance();
		factory.define(builder);

		unmarsh = factory.createUnmarshaller("TeamTrancode");
		marsh = factory.createMarshaller("TeamTrancode");
	}

	public TeamTrancode from(final String input) {
		LOGGER.debug("Transforming trancode into bean: {}", input);

		final TeamTrancode output = (TeamTrancode) unmarsh.unmarshal(input);
		LOGGER.debug("Resulted bean: {}", output);

		return output;
	}

	public String to(final TeamTrancode input) {
		LOGGER.debug("Transforming bean into trancode: {}", input);

		final String output = marsh.marshal(input).toString();
		LOGGER.debug("Resulted trancode: {}", output);

		return output;
	}
}
