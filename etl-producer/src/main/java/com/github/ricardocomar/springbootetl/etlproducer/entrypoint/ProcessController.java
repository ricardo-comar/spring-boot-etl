package com.github.ricardocomar.springbootetl.etlproducer.entrypoint;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.ricardocomar.springbootetl.etlproducer.config.AppProperties;
import com.github.ricardocomar.springbootetl.etlproducer.entrypoint.model.ProcessRequest;
import com.github.ricardocomar.springbootetl.etlproducer.entrypoint.model.ProcessResponse;
import com.github.ricardocomar.springbootetl.etlproducer.entrypoint.model.ProcessResponse.Team;
import com.github.ricardocomar.springbootetl.etlproducer.exception.UnavailableResponseException;
import com.github.ricardocomar.springbootetl.etlproducer.service.ConcurrentProcessor;

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
			final Team response = processor.handle(request);

			return (response == null || StringUtils.isEmpty(response.getTeamName())
					? ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					: ResponseEntity.ok())
							.body(ProcessResponse.builder().id(request.getId()).response(response)
									.duration(System.currentTimeMillis() - start).build());

		} catch (final UnavailableResponseException e) {
			LOGGER.error("Response Unavailable");
		}

		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
	}
}
