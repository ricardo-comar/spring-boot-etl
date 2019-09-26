package com.github.ricardocomar.springbootetl.model;

import java.util.List;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ErrorMessage {
	private final String id;
	private final String payload;
	private final List<ErrorItem> errors;
	
	@Value
	@Builder
	public static class ErrorItem {
		private final String message;
		private final String field;
		private final Object attemptedValue;
		private final String code;
	}
}
