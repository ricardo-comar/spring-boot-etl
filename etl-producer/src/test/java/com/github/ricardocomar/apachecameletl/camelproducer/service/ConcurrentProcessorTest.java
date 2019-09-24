package com.github.ricardocomar.apachecameletl.camelproducer.service;

import static org.hamcrest.MatcherAssert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.ricardocomar.springdataflowetl.etlproducer.config.AppProperties;
import com.github.ricardocomar.springdataflowetl.etlproducer.exception.UnavailableResponseException;
import com.github.ricardocomar.springdataflowetl.etlproducer.service.ConcurrentProcessor;
import com.github.ricardocomar.springdataflowetl.etlproducer.service.model.MessageEvent;
import com.github.ricardocomar.springdataflowetl.model.RequestMessage;
import com.github.ricardocomar.springdataflowetl.model.ResponseMessage;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@EnableConfigurationProperties(AppProperties.class)
@TestPropertySource(properties = { "kafka-producer.concurrent-processor.wait-timeout=200", })
@ContextConfiguration(classes = {
		ConcurrentProcessor.class }, initializers = ConfigFileApplicationContextInitializer.class)
public class ConcurrentProcessorTest {

	@MockBean
	private JmsTemplate template;

	@Autowired
	private ApplicationContext appContext;

	@Before
	public void before() {
		Mockito.doNothing().when(template).convertAndSend(Mockito.anyString(), Mockito.anyString());
	}

	@Test
	public void testRelease() {
		final ResponseMessage response = ResponseMessage.builder().id("123").build();
		final ResponseMessage expected = ResponseMessage.builder().id("123").payload("999").build();

		final ConcurrentProcessor processor = appContext.getBean(ConcurrentProcessor.class);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					final ResponseMessage resp = processor.handle(RequestMessage.builder().id("123").build());
					response.setPayload(resp.getPayload());
				} catch (final UnavailableResponseException e) {
					e.printStackTrace();
				}
			}
		}).start();
		sleep(50);

		appContext.publishEvent(new MessageEvent("", expected));
		sleep(50);

		assertThat(response, Matchers.equalTo(expected));
	}

	@Test
	public void testTimeout() {
		final ResponseMessage response = ResponseMessage.builder().id("456").build();

		final ConcurrentProcessor processor = appContext.getBean(ConcurrentProcessor.class);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					final ResponseMessage resp = processor.handle(RequestMessage.builder().id("456").build());
					response.setPayload(resp.getPayload());
				} catch (final UnavailableResponseException e) {
					e.printStackTrace();
				}
			}
		}).start();
		sleep(350);

		assertThat(response.getPayload(), Matchers.nullValue());
	}

	@Test
	public void testConcurrent() {
		final String successId = "AAA", timeoutId = "XXX";
		final ResponseMessage responseSuccess = ResponseMessage.builder().id(successId).build();
		final ResponseMessage expectedSuccess = ResponseMessage.builder().id(successId).payload("999").build();
		final ResponseMessage responseTimeout = ResponseMessage.builder().id(timeoutId).build();
		final ResponseMessage expectedTimeout = ResponseMessage.builder().id(timeoutId).build();

		final ConcurrentProcessor processor = appContext.getBean(ConcurrentProcessor.class);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					final ResponseMessage resp = processor.handle(RequestMessage.builder().id(successId).build());
					responseSuccess.setPayload(resp.getPayload());
				} catch (final UnavailableResponseException e) {
					e.printStackTrace();
				}
			}
		}).start();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					final ResponseMessage resp = processor.handle(RequestMessage.builder().id(timeoutId).build());
					responseTimeout.setPayload(resp.getPayload());
				} catch (final UnavailableResponseException e) {
					e.printStackTrace();
				}
			}
		}).start();

		sleep(50);
		appContext.publishEvent(new MessageEvent("", expectedSuccess));
		sleep(250);

		assertThat(responseSuccess, Matchers.equalTo(expectedSuccess));
		assertThat(responseTimeout, Matchers.equalTo(expectedTimeout));
	}

	private void sleep(final long duration) {
		try {
			Thread.sleep(duration);
		} catch (final InterruptedException e) {
		}
	}

}
