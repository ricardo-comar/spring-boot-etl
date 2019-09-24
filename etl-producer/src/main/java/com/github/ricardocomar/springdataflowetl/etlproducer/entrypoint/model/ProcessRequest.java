package com.github.ricardocomar.springdataflowetl.etlproducer.entrypoint.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProcessRequest {

	private String id;
	private String payload;
}
