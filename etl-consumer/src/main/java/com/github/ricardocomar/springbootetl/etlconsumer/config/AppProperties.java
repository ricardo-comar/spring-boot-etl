package com.github.ricardocomar.springbootetl.etlconsumer.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
@ConfigurationProperties(prefix = "etl-consumer")
public class AppProperties {
	
	public static final String HEADER_REQUEST_ID = "X-Request-id";
	public static final String HEADER_CORRELATION_ID = "X-Correlation-id";
	public static final String PROP_CORRELATION_ID = "correlationId";

	private String instanceId;

	private Consumer consumer;
	
	@Data @NoArgsConstructor
	public static class Consumer {
		
		private ContainerFactory containerFactory;
		
		@Data @NoArgsConstructor
		public static class ContainerFactory {
			
			Integer concurrency;
			private Properties properties;
			
			@Data @NoArgsConstructor
			public static class Properties {
				
				private Long poolTimeout;
			}
		}
}
}
