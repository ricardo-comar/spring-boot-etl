package com.github.ricardocomar.springbootetl.etlproducer.service;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.apache.avro.generic.GenericRecord;
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
import com.github.ricardocomar.springbootetl.etlproducer.exception.UnavailableResponseException;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@EnableConfigurationProperties(AppProperties.class)
@TestPropertySource(properties = { "kafka-producer.concurrent-processor.wait-timeout=200", })
@ContextConfiguration(classes = {
		ConcurrentProcessor.class }, initializers = ConfigFileApplicationContextInitializer.class)
public class ConcurrentProcessorTest {

//	private static final EmployeeAvro EMPLOYEE_AVRO = EmployeeAvro.newBuilder().setFirstName("John").setLastName("Snow")
//			.setHireDate("20100520").setTitle("Developer").setSalary(new BigDecimal(1000.0))
//			.setStatus(EmployeeAvroStatus.ACTIVE).build();

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
//		final TeamAvro response = buildTeamAvro("Team NotUpdated");
//		final TeamAvro expected = buildTeamAvro("Team OK");
//
//		new Thread(runnable(ProcessRequest.builder().payload("testRelease").build(), response)).start();
//		sleep(50);
//
//		appContext.publishEvent(new MessageEvent(requestId, expected));
//		sleep(50);
//
//		assertThat(response, Matchers.equalTo(expected));
	}

	@Test
	public void testTimeout() {
//		final TeamAvro response = buildTeamAvro("Team XYZ");
//
//		new Thread(runnable(ProcessRequest.builder().payload("testTimeout").build(), response)).start();
//		sleep(350);
//
//		assertThat(response.getTeamName(), Matchers.equalTo("Team XYZ"));
	}

	@Test
	public void testConcurrent() {
//		final TeamAvro responseSuccess = buildTeamAvro("Team NotUpdated");
//		final TeamAvro expectedSuccess = buildTeamAvro("Team OK");
//
//		final TeamAvro responseTimeout = buildTeamAvro("Team TimeOut");
//		final TeamAvro expectedTimeout = buildTeamAvro("Team TimeOut");
//
//		new Thread(runnable(ProcessRequest.builder().payload("testConcurrent-AAA").build(), responseSuccess)).start();
//		sleep(50);
//		appContext.publishEvent(new MessageEvent(requestId, expectedSuccess));
//
//		new Thread(runnable(ProcessRequest.builder().payload("testConcurrent-XXX").build(), responseTimeout)).start();
//		sleep(250);
//
//		assertThat(responseSuccess, Matchers.equalTo(expectedSuccess));
//		assertThat(responseTimeout, Matchers.equalTo(expectedTimeout));
	}

	public Runnable runnable(final ProcessRequest request/* , final TeamAvro response */) {

		final Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					final GenericRecord resp = processor.handle(request);
//TODO: ativar					response.setTeamName(resp.getTeamName());
				} catch (final UnavailableResponseException e) {
					e.printStackTrace();
				}
			}
		};
		return runnable;
	}

//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	private TeamAvro buildTeamAvro(final String teamName) {
//		return TeamAvro.newBuilder().setTeamName(teamName)
//				.setEmployees(new ArrayList(Arrays.asList(new EmployeeAvro[] { EMPLOYEE_AVRO }))).build();
//	}
//
	private void sleep(final long duration) {
		try {
			Thread.sleep(duration);
		} catch (final InterruptedException e) {
		}
	}

}
