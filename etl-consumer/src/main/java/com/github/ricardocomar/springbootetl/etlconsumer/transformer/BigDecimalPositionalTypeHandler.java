package com.github.ricardocomar.springbootetl.etlconsumer.transformer;

import java.math.BigDecimal;
import java.util.Properties;

import org.beanio.types.ConfigurableTypeHandler;
import org.beanio.types.TypeHandler;

import lombok.Builder;

@Builder
public class BigDecimalPositionalTypeHandler implements TypeHandler, ConfigurableTypeHandler {

	private static final String PARAM_SEP = ",";
	private static final String DECIMAL_REGEX = "([0-9]+),([0-9]+)";
	private final Integer precision;
	private final Integer scale;

	@Override
	public Object parse(final String text) {
		if (text.length() > precision) {
			throw new IllegalArgumentException(
					text + " length (" + text.length() + ") is greater than configured precision " + precision);
		}
		return new java.math.BigDecimal(text.substring(0, Math.min(precision, text.length()))).movePointLeft(scale).setScale(scale);
	}

	@Override
	public String format(final Object input) {
		final BigDecimal value = (BigDecimal) input;
		if (value.precision() > precision) {
			throw new IllegalArgumentException(
					value + " precision (" + value.precision() + ") is greater than configured precision " + precision);
		}

		final String tmp = String.format("%01.0f", value.movePointRight(scale));
		return tmp.substring(0, Math.min(precision, tmp.length()));
	}

	@Override
	public Class<?> getType() {
		return BigDecimal.class;
	}

	@Override
	public TypeHandler newInstance(final Properties properties) throws IllegalArgumentException {
		if (!properties.containsKey("format")) {
			throw new IllegalArgumentException("property \"format\" is mandatory");
		}

		final String format = properties.getProperty("format");
		if (format.equals(precision + PARAM_SEP + scale)) {
			return this;
		}

		if (!format.matches(DECIMAL_REGEX)) {
			throw new IllegalArgumentException(
					"property \"format\" is invalid, must be like this: \"" + DECIMAL_REGEX + "\"");
		}
		final String[] params = format.split(PARAM_SEP);
		return BigDecimalPositionalTypeHandler.builder().precision(new Integer(params[0])).scale(new Integer(params[1])).build();
	}

}
