package com.github.ricardocomar.springbootetl.etlconsumer.transformer.handler;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.Test;

public class BigDecimalPositionalTypeHandlerTest {

	final BigDecimalPositionalTypeHandler handler = BigDecimalPositionalTypeHandler.builder().precision(5).scale(2)
			.build();

	@Test
	public void testParse() {
		
		assertThat(new BigDecimal(123.45).setScale(2, RoundingMode.HALF_EVEN), equalTo(handler.parse("12345")));
		assertThat(new BigDecimal(123.4).setScale(2, RoundingMode.HALF_EVEN), equalTo(handler.parse("12340")));
		assertThat(new BigDecimal(123).setScale(2, RoundingMode.HALF_EVEN), equalTo(handler.parse("12300")));
		assertThat(new BigDecimal(120).setScale(2, RoundingMode.HALF_EVEN), equalTo(handler.parse("12000")));

	}

	@Test(expected = IllegalArgumentException.class)
	public void testParseInvalidPrecision() {

		handler.parse("12345678");
	}

	@Test
	public void testFormat() {

		assertThat("12345", equalTo(handler.format(new BigDecimal(123.45).setScale(2, RoundingMode.HALF_EVEN))));
		assertThat("12340", equalTo(handler.format(new BigDecimal(123.4).setScale(2, RoundingMode.HALF_EVEN))));
		assertThat("12300", equalTo(handler.format(new BigDecimal(123).setScale(2, RoundingMode.HALF_EVEN))));

	}

	@Test(expected = IllegalArgumentException.class)
	public void testFormatAbovePrecision() {

		handler.format(new BigDecimal(123456.78).setScale(2, RoundingMode.HALF_EVEN));
	}
}
