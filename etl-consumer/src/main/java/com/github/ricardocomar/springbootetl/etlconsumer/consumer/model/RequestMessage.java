package com.github.ricardocomar.springbootetl.etlconsumer.consumer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestMessage {
	private String id;
	private String origin;
	private String trancode;
	
}
