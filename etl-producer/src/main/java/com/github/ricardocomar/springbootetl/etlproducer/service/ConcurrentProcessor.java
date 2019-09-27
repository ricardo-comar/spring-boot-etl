package com.github.ricardocomar.springbootetl.etlproducer.service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.jms.JMSException;
import javax.jms.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.stereotype.Service;

import com.github.ricardocomar.springbootetl.etlproducer.config.AppProperties;
import com.github.ricardocomar.springbootetl.etlproducer.entrypoint.model.ProcessRequest;
import com.github.ricardocomar.springbootetl.etlproducer.entrypoint.model.ProcessResponse.Team;
import com.github.ricardocomar.springbootetl.etlproducer.entrypoint.model.ProcessResponse.Team.Employee.EmployeeStatus;
import com.github.ricardocomar.springbootetl.etlproducer.exception.UnavailableResponseException;
import com.github.ricardocomar.springbootetl.etlproducer.service.model.MessageEvent;
import com.github.ricardocomar.springbootetl.model.TeamAvro;

@Service
public class ConcurrentProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConcurrentProcessor.class);

	@Autowired
	private JmsTemplate jmsTemplate;

	@Autowired
	private AppProperties appProps;

	private final Map<String, ProcessRequest> lockMap = new ConcurrentHashMap<>();
	private final Map<String, TeamAvro> responseMap = new ConcurrentHashMap<>();

	public Team handle(final ProcessRequest request) throws UnavailableResponseException {

		LOGGER.debug("Message to be processed: {}", request);
		final String requestId = UUID.randomUUID().toString();

		lockMap.remove(requestId);
		responseMap.remove(requestId);

		LOGGER.debug("Will wait {}ms", appProps.getConcurrentProcessor().getWaitTimeout());

		lockMap.put(requestId, request);


		synchronized (request) {
			try {
				jmsTemplate.convertAndSend("queue.sample", request.getPayload(), new MessagePostProcessor() {
					@Override
					public Message postProcessMessage(final Message message) throws JMSException {
						message.setStringProperty(AppProperties.HEADER_REQUEST_ID, requestId);
						return message;
					}
				});

			} catch (final JmsException e) {
				LOGGER.error("Error sending message", e);
				return null;
			}

			try {
				request.wait(appProps.getConcurrentProcessor().getWaitTimeout());
				LOGGER.debug("Lock released for message {}", requestId);
			} catch (final InterruptedException e) {
				LOGGER.debug("Wait timeout for message {}", requestId);
			} finally {
				lockMap.remove(requestId);
			}
		}

		final TeamAvro responseAvro = responseMap.remove(requestId);
		if (responseAvro == null) {
			throw new UnavailableResponseException("No response for id " + requestId);
		}
		
		final Team response = Team.builder().teamName(responseAvro.getTeamName())
				.employees(responseAvro.getEmployees().stream().filter(emp -> emp != null)
				.map(emp -> Team.Employee.builder().firstName(emp.getFirstName()).lastName(emp.getLastName())
						.title(emp.getTitle()).hireDate(emp.getHireDate()).salary(emp.getSalary())
						.status(EmployeeStatus.valueOf(emp.getStatus().name())).build())
						.collect(Collectors.toList())
				).build();
		
		LOGGER.debug("Returning response for id ({}) = {}", requestId, response);
		return response;

	}

	@EventListener
	public void notifyResponse(final MessageEvent event) {
		final String requestId = event.getRequestId();
		if (!lockMap.containsKey(requestId)) {
			LOGGER.warn("Locked request not found for response id {}", requestId);
			return;
		}

		final ProcessRequest request = lockMap.remove(requestId);
		synchronized (request) {
			final TeamAvro response = event.getResponse();
			LOGGER.debug("Response is being saved for id {}, lock will be released for payload {}", requestId, request);
			responseMap.put(requestId, response);

			request.notify();
		}

	}

}
