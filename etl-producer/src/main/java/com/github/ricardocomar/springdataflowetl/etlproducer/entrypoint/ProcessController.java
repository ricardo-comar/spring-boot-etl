package com.github.ricardocomar.springdataflowetl.etlproducer.entrypoint;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.ricardocomar.springdataflowetl.etlproducer.config.AppProperties;
import com.github.ricardocomar.springdataflowetl.etlproducer.entrypoint.model.ProcessRequest;
import com.github.ricardocomar.springdataflowetl.etlproducer.entrypoint.model.ProcessResponse;
import com.github.ricardocomar.springdataflowetl.etlproducer.exception.UnavailableResponseException;
import com.github.ricardocomar.springdataflowetl.etlproducer.service.ConcurrentProcessor;
import com.github.ricardocomar.springdataflowetl.model.RequestMessage;
import com.github.ricardocomar.springdataflowetl.model.ResponseMessage;

@RestController
public class ProcessController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessController.class);

	@Autowired
	private ConcurrentProcessor processor;

	@Autowired
	private AppProperties appProps;

	@PostMapping(value = "/process")
	public ResponseEntity<ProcessResponse> process(@RequestBody final ProcessRequest request) {

		final long start = System.currentTimeMillis();

		try {
			final RequestMessage requestMessage = RequestMessage.builder().id(request.getId())
					.payload(request.getPayload()).build();

			final ResponseMessage response = processor.handle(requestMessage);

			return (!StringUtils.isEmpty(response.getPayload()) ? ResponseEntity.ok()
					: ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR))
							.body(ProcessResponse.builder().id(response.getId()).payload(response.getPayload())
									.duration(System.currentTimeMillis() - start).build());

		} catch (final UnavailableResponseException e) {
			LOGGER.error("Response Unavailable");
		}

		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
	}
}
