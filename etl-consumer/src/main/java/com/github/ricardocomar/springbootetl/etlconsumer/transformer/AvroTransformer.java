package com.github.ricardocomar.springbootetl.etlconsumer.transformer;

import org.apache.avro.specific.SpecificRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

import com.github.ricardocomar.springbootetl.etlconsumer.mapper.ConsumerAvroMapper;
import com.github.ricardocomar.springbootetl.etlconsumer.mapper.ConsumerModelMapper;
import com.github.ricardocomar.springbootetl.etlconsumer.model.ConsumerModel;

@Component
public class AvroTransformer {

	private static final Logger LOGGER = LoggerFactory.getLogger(AvroTransformer.class);

	@Autowired
	private ApplicationContext ctx;

	@SuppressWarnings("unchecked")
	public SpecificRecord from(final ConsumerModel input) {
		LOGGER.debug("Transforming trancode into bean: {}", input);
		
		final ConsumerModelMapper<ConsumerModel> mapper = resolveMapper(input.getClass(), ConsumerModelMapper.class);

		final SpecificRecord output = mapper.fromModel(input);
		LOGGER.debug("Resulted bean: {}", output);

		return output;
	}

	@SuppressWarnings("unchecked")
	public ConsumerModel to(final SpecificRecord input) {
		LOGGER.debug("Transforming avro into bean: {}", input);

		final ConsumerAvroMapper<SpecificRecord> mapper = resolveMapper(input.getClass(), ConsumerAvroMapper.class);

		final ConsumerModel output = mapper.fromAvro(input);
		LOGGER.debug("Resulted bean: {}", output);

		return output;
	}

	@SuppressWarnings("unchecked")
	private <T> T resolveMapper(final Class<?> inputClass, final Class<T> mapperClass) {

		final String[] beanNamesForType = ctx
				.getBeanNamesForType(ResolvableType.forClassWithGenerics(mapperClass, inputClass));

		final T mapper = (T) ctx
				.getBean(beanNamesForType[0]);

		return mapper;
	}
}
