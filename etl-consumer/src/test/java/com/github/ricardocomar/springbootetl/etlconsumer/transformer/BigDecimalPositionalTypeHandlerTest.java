package com.github.ricardocomar.springbootetl.etlconsumer.transformer;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.Test;

public class BigDecimalPositionalTypeHandlerTest {

	@Test
	public void testParse() {
		
		final BigDecimalPositionalTypeHandler handler = BigDecimalPositionalTypeHandler.builder().precision(5).scale(2)
				.build();

		assertThat(new BigDecimal(123.45).setScale(2, RoundingMode.HALF_EVEN), equalTo(handler.parse("12345678")));
		assertThat(new BigDecimal(123.45).setScale(2, RoundingMode.HALF_EVEN), equalTo(handler.parse("12345670")));
		assertThat(new BigDecimal(123.45).setScale(2, RoundingMode.HALF_EVEN), equalTo(handler.parse("12345600")));
		assertThat(new BigDecimal(123.45).setScale(2, RoundingMode.HALF_EVEN), equalTo(handler.parse("12345000")));
		assertThat(new BigDecimal(123.4).setScale(2, RoundingMode.HALF_EVEN), equalTo(handler.parse("12340000")));
		assertThat(new BigDecimal(123).setScale(2, RoundingMode.HALF_EVEN), equalTo(handler.parse("12300000")));
		assertThat(new BigDecimal(120).setScale(2, RoundingMode.HALF_EVEN), equalTo(handler.parse("12000000")));
		
	}

	@Test
	public void testFormat() {

		final BigDecimalPositionalTypeHandler handler = BigDecimalPositionalTypeHandler.builder().precision(5).scale(2)
				.build();

		assertThat("12345", equalTo(handler.format(new BigDecimal(123456.78).setScale(2, RoundingMode.HALF_EVEN))));
		assertThat("12345", equalTo(handler.format(new BigDecimal(123.45).setScale(2, RoundingMode.HALF_EVEN))));
		assertThat("12340", equalTo(handler.format(new BigDecimal(123.4).setScale(2, RoundingMode.HALF_EVEN))));
		assertThat("12300", equalTo(handler.format(new BigDecimal(123).setScale(2, RoundingMode.HALF_EVEN))));

	}
}
