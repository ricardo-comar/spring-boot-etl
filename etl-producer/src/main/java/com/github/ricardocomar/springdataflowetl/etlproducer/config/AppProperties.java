package com.github.ricardocomar.springdataflowetl.etlproducer.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "etl-producer")
public class AppProperties {

	public static final String HEADER_REQUEST_ID = "X-Request-id";
	public static final String HEADER_CORRELATION_ID = "X-Correlation-id";
	public static final String PROP_CORRELATION_ID = "correlationId";

	private String instanceId;
	
	private ConcurrentProcessor concurrentProcessor;

	@Data
	@NoArgsConstructor
	public static class ConcurrentProcessor {

		private Integer waitTimeout = 10000;
	}

	private Consumer consumer;

	@Data
	@NoArgsConstructor
	public static class Consumer {

		private ContainerFactory containerFactory;

		@Data
		@NoArgsConstructor
		public static class ContainerFactory {

			private Integer concurrency = 25;

			private Properties properties;

			@Data
			@NoArgsConstructor
			public static class Properties {

				private Integer poolTimeout = 30000;
			}

		}
	}

	private RestTemplate restTemplate;

	@Data
	@NoArgsConstructor
	public static class RestTemplate {

		private Redirect redirect;

		@Data
		@NoArgsConstructor
		public static class Redirect {

			private Integer readTimeout = 1000;
			
			private Integer connectTimeout = 500;
		}
	}

}
