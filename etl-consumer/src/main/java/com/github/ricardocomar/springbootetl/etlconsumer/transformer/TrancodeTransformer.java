package com.github.ricardocomar.springbootetl.etlconsumer.transformer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.beanio.Marshaller;
import org.beanio.StreamFactory;
import org.beanio.Unmarshaller;
import org.beanio.builder.FixedLengthParserBuilder;
import org.beanio.builder.StreamBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.github.ricardocomar.springbootetl.etlconsumer.model.ConsumerModel;
import com.github.ricardocomar.springbootetl.etlconsumer.model.Purchase;
import com.github.ricardocomar.springbootetl.etlconsumer.model.Team;
import com.github.ricardocomar.springbootetl.etlconsumer.transformer.handler.BigDecimalPositionalTypeHandler;
import com.github.ricardocomar.springbootetl.etlconsumer.transformer.handler.LocalDateTypeHandler;

@Component
public class TrancodeTransformer {

	private static final Logger LOGGER = LoggerFactory.getLogger(TrancodeTransformer.class);

	private final Map<String, Unmarshaller> unmarshMap = Collections.synchronizedMap(new HashMap<>());
	private final Map<Class<? extends ConsumerModel>, Marshaller> marshMap = Collections
			.synchronizedMap(new HashMap<>());

	public TrancodeTransformer() {
		initMappers("TRANTEAM-1", Team.class);
		initMappers("TRANPURC-1", Purchase.class);
	}

	private void initMappers(final String transaction, final Class<? extends ConsumerModel> modelClass) {
		final StreamBuilder builder = new StreamBuilder(transaction).format("fixedlength")
				.addTypeHandler(LocalDate.class, LocalDateTypeHandler.builder().format("yyyyMMdd").build())
				.addTypeHandler(BigDecimal.class,
						BigDecimalPositionalTypeHandler.builder().precision(8).scale(2).build())
				.parser(new FixedLengthParserBuilder()).addRecord(modelClass);

		LOGGER.debug("Creating {} Unmarshallers and Marshallers", transaction);

		final StreamFactory factory = StreamFactory.newInstance();
		factory.define(builder);

		unmarshMap.put(transaction, factory.createUnmarshaller(transaction));
		marshMap.put(modelClass, factory.createMarshaller(transaction));
	}

	public ConsumerModel fromTrancode(final String input) {
		LOGGER.debug("Transforming trancode into bean: {}", input);
		final String transaction = input.substring(0, 10);
		if (!unmarshMap.containsKey(transaction)) {
			throw new ETLTransformerException("Unexpected transaction " + transaction);
		}

		final ConsumerModel output = (ConsumerModel) unmarshMap.get(transaction).unmarshal(input);
		LOGGER.debug("Resulted bean: {}", output);

		return output;
	}

	public String to(final ConsumerModel input) {
		LOGGER.debug("Transforming bean into trancode: {}", input);
		if (!marshMap.containsKey(input.getClass())) {
			throw new ETLTransformerException("Unexpected input class " + input.getClass().getName());
		}

		final String output = marshMap.get(input.getClass()).marshal(input).toString();
		LOGGER.debug("Resulted trancode: {}", output);

		return output;
	}
}
