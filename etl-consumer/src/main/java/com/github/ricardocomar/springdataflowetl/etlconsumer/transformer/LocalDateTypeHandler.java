package com.github.ricardocomar.springdataflowetl.etlconsumer.transformer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import org.beanio.types.ConfigurableTypeHandler;
import org.beanio.types.TypeConversionException;
import org.beanio.types.TypeHandler;

import lombok.Builder;

@Builder
public class LocalDateTypeHandler implements TypeHandler, ConfigurableTypeHandler {

	private final String format;

	@Override
	public Object parse(final String text) throws TypeConversionException {
		return LocalDate.parse(text, DateTimeFormatter.ofPattern(format));
	}

	@Override
	public String format(final Object value) {
		return ((LocalDate) value).format(DateTimeFormatter.ofPattern(format));
	}

	@Override
	public Class<?> getType() {
		return LocalDate.class;
	}

	@Override
	public TypeHandler newInstance(final Properties properties) throws IllegalArgumentException {
		if (properties.containsKey("format") && properties.getProperty("format").equals(format)) {
			return this;
		}
		return LocalDateTypeHandler.builder().format(properties.getProperty("format")).build();
	}

}
