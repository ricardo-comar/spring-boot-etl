package com.github.ricardocomar.springbootetl.etlproducer.service;

import static org.hamcrest.MatcherAssert.assertThat;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.assertj.core.util.Arrays;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.ricardocomar.springbootetl.etlproducer.config.AppProperties;
import com.github.ricardocomar.springbootetl.etlproducer.entrypoint.model.ProcessRequest;
import com.github.ricardocomar.springbootetl.etlproducer.entrypoint.model.ProcessResponse.Team;
import com.github.ricardocomar.springbootetl.etlproducer.exception.UnavailableResponseException;
import com.github.ricardocomar.springbootetl.etlproducer.service.model.MessageEvent;
import com.github.ricardocomar.springbootetl.model.EmployeeAvro;
import com.github.ricardocomar.springbootetl.model.EmployeeAvroStatus;
import com.github.ricardocomar.springbootetl.model.TeamAvro;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@EnableConfigurationProperties(AppProperties.class)
@TestPropertySource(properties = { "kafka-producer.concurrent-processor.wait-timeout=200", })
@ContextConfiguration(classes = {
		ConcurrentProcessor.class }, initializers = ConfigFileApplicationContextInitializer.class)
public class ConcurrentProcessorTest {

	private static final EmployeeAvro EMPLOYEE_AVRO = EmployeeAvro.newBuilder().setFirstName("John").setLastName("Snow")
			.setHireDate("20100520").setTitle("Developer").setSalary(new BigDecimal(1000.0))
			.setStatus(EmployeeAvroStatus.ACTIVE).build();

	@MockBean
	private JmsTemplate template;

	@Autowired
	private ApplicationContext appContext;

	@Autowired
	private ConcurrentProcessor processor;

	private String requestId;

	@Before
	public void before() {
		requestId = null;
		Mockito.doAnswer((Answer<?>) invocation -> {
			final MessagePostProcessor processor = invocation.getArgument(2);

			final ActiveMQTextMessage message = new ActiveMQTextMessage();
			processor.postProcessMessage(message);
			requestId = message.getStringProperty(AppProperties.HEADER_REQUEST_ID);
			return null;
		}).when(template).convertAndSend(Mockito.anyString(), Mockito.anyString(),
				Mockito.any(MessagePostProcessor.class));
	}

	@After
	public void after() {
		Mockito.verify(template, Mockito.atLeastOnce()).convertAndSend(Mockito.anyString(), Mockito.anyString(),
				Mockito.any(MessagePostProcessor.class));
	}

	@Test
	public void testRelease() {
		final TeamAvro response = buildTeamAvro("Team NotUpdated");
		final TeamAvro expected = buildTeamAvro("Team OK");

		System.err.println("release - requestId=" + requestId);
		new Thread(runnable(ProcessRequest.builder().payload("testRelease").build(), response)).start();
		sleep(50);

		System.err.println("release - requestId=" + requestId);
		appContext.publishEvent(new MessageEvent(requestId, expected));
		sleep(50);
		System.err.println("release - requestId=" + requestId);

		assertThat(response, Matchers.equalTo(expected));
	}

	@Test
	public void testTimeout() {
		final TeamAvro response = buildTeamAvro("Team XYZ");

		System.err.println("timeout - requestId=" + requestId);
		new Thread(runnable(ProcessRequest.builder().payload("testTimeout").build(), response)).start();
		sleep(350);
		System.err.println("timeout - requestId=" + requestId);

		assertThat(response.getTeamName(), Matchers.equalTo("Team XYZ"));
	}

	@Test
	public void testConcurrent() {
		final TeamAvro responseSuccess = buildTeamAvro("Team NotUpdated");
		final TeamAvro expectedSuccess = buildTeamAvro("Team OK");

		final TeamAvro responseTimeout = buildTeamAvro("Team TimeOut");
		final TeamAvro expectedTimeout = buildTeamAvro("Team TimeOut");

		System.err.println("concurrent - requestId=" + requestId);
		new Thread(runnable(ProcessRequest.builder().payload("testConcurrent-AAA").build(), responseSuccess)).start();
		System.err.println("concurrent - requestId=" + requestId);
		sleep(50);
		appContext.publishEvent(new MessageEvent(requestId, expectedSuccess));
		System.err.println("concurrent - requestId=" + requestId);

		new Thread(runnable(ProcessRequest.builder().payload("testConcurrent-XXX").build(), responseTimeout)).start();
		sleep(250);
		System.err.println("concurrent - requestId=" + requestId);

		assertThat(responseSuccess, Matchers.equalTo(expectedSuccess));
		assertThat(responseTimeout, Matchers.equalTo(expectedTimeout));
	}

	public Runnable runnable(final ProcessRequest request, final TeamAvro response) {

		final Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					final Team resp = processor.handle(request);
					response.setTeamName(resp.getTeamName());
				} catch (final UnavailableResponseException e) {
					e.printStackTrace();
				}
			}
		};
		return runnable;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private TeamAvro buildTeamAvro(final String teamName) {
		return TeamAvro.newBuilder().setTeamName(teamName)
				.setEmployees(new ArrayList(Arrays.asList(new EmployeeAvro[] { EMPLOYEE_AVRO }))).build();
	}

	private void sleep(final long duration) {
		try {
			Thread.sleep(duration);
		} catch (final InterruptedException e) {
		}
	}

}
