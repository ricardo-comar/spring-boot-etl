package com.github.ricardocomar.springbootetl.etlconsumer.transformer;

import org.apache.avro.generic.GenericRecord;
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
	public GenericRecord from(final ConsumerModel input) {
		LOGGER.debug("Transforming trancode into bean: {}", input);
		
		final ConsumerModelMapper<ConsumerModel> mapper = (ConsumerModelMapper<ConsumerModel>) ctx
				.getBeanProvider(ResolvableType.forClassWithGenerics(ConsumerModelMapper.class, input.getClass()))
				.getIfUnique();

		final GenericRecord output = mapper.fromModel(input);
		LOGGER.debug("Resulted bean: {}", output);

		return output;
	}

	@SuppressWarnings("unchecked")
	public ConsumerModel to(final GenericRecord input) {
		LOGGER.debug("Transforming avro into bean: {}", input);

		final ConsumerAvroMapper<GenericRecord> mapper = (ConsumerAvroMapper<GenericRecord>) ctx
				.getBeanProvider(ResolvableType.forClassWithGenerics(ConsumerAvroMapper.class, input.getClass()))
				.getIfUnique();

		final ConsumerModel output = mapper.fromAvro(input);
		LOGGER.debug("Resulted bean: {}", output);

		return output;
	}

}
