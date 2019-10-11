package com.github.ricardocomar.springbootetl.etlproducer.entrypoint;

import java.util.HashMap;

import org.apache.avro.generic.GenericRecord;
import org.apache.avro.specific.SpecificData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ricardocomar.springbootetl.etlproducer.entrypoint.model.ProcessRequest;
import com.github.ricardocomar.springbootetl.etlproducer.entrypoint.model.ProcessResponse;
import com.github.ricardocomar.springbootetl.etlproducer.service.ConcurrentProcessor;

@RestController
public class ProcessController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessController.class);

	@Autowired
	private ConcurrentProcessor processor;

	@PostMapping(value = "/process")
	@ResponseBody
	public ResponseEntity<ProcessResponse> process(@RequestBody final ProcessRequest request) {

		final long start = System.currentTimeMillis();

		try {
			final GenericRecord response = processor.handle(request);
			return (response == null || response.get(0) == null
					? ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
					: ResponseEntity.ok())
							.body(ProcessResponse.builder().id(request.getId())
									.response(new ObjectMapper().readValue(SpecificData.get().toString(response),
											HashMap.class))
									.duration(System.currentTimeMillis() - start).build());

		} catch (final Exception e) {
			LOGGER.error("Response Unavailable", e);
		}

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

}
