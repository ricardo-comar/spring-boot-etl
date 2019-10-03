package com.github.ricardocomar.springbootetl.etlconsumer.transformer;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.beanio.Marshaller;
import org.beanio.StreamFactory;
import org.beanio.Unmarshaller;
import org.beanio.builder.FixedLengthParserBuilder;
import org.beanio.builder.StreamBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.github.ricardocomar.springbootetl.etlconsumer.model.Team;

@Component
public class TrancodeTransformer {

	private static final Logger LOGGER = LoggerFactory.getLogger(TrancodeTransformer.class);

	private final Unmarshaller unmarsh;
	private final Marshaller marsh;

	public TrancodeTransformer() {

		LOGGER.debug("Creating BeanIO Unmarshallers and Marshallers");

		final StreamBuilder builder = new StreamBuilder("TeamTrancode").format("fixedlength")
				.addTypeHandler(LocalDate.class, new LocalDateTypeHandler("yyyyMMdd"))
				.addTypeHandler(BigDecimal.class, new BigDecimalPositionalTypeHandler(8, 2))
				.parser(new FixedLengthParserBuilder()).addRecord(Team.class);

		final StreamFactory factory = StreamFactory.newInstance();
		factory.define(builder);

		unmarsh = factory.createUnmarshaller("TeamTrancode");
		marsh = factory.createMarshaller("TeamTrancode");
	}

	public Team from(final String input) {
		LOGGER.debug("Transforming trancode into bean: {}", input);

		final Team output = (Team) unmarsh.unmarshal(input);
		LOGGER.debug("Resulted bean: {}", output);

		return output;
	}

	public String to(final Team input) {
		LOGGER.debug("Transforming bean into trancode: {}", input);

		final String output = marsh.marshal(input).toString();
		LOGGER.debug("Resulted trancode: {}", output);

		return output;
	}
}
