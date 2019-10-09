package com.github.ricardocomar.springbootetl.etlproducer.entrypoint.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProcessResponse {

	private String id;
	private String response;
	private Long duration;

}
