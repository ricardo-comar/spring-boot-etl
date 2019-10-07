package com.github.ricardocomar.springbootetl.etlconsumer.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

import com.github.ricardocomar.springbootetl.etlconsumer.model.ConsumerModel;

import br.com.fluentvalidator.AbstractValidator;
import br.com.fluentvalidator.context.ValidationResult;

@Component
public class ModelValidator {

	private static final Logger LOGGER = LoggerFactory.getLogger(ModelValidator.class);

	@Autowired
	private ApplicationContext ctx;

	@SuppressWarnings("unchecked")
	public ValidationResult validate(final ConsumerModel input) {
		LOGGER.debug("Validating bean: {}", input);
		
		final AbstractValidator<ConsumerModel> validator = (AbstractValidator<ConsumerModel>) ctx
				.getBeanProvider(ResolvableType.forClassWithGenerics(AbstractValidator.class, input.getClass()))
				.getIfUnique();

		return validator.validate(input);

	}

}
