package com.github.ricardocomar.springbootetl.etlconsumer;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.UUID;

import javax.jms.JMSException;
import javax.jms.Message;

import org.apache.avro.generic.GenericRecord;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.ricardocomar.springbootetl.etlconsumer.config.AppProperties;
import com.github.ricardocomar.springbootetl.etlconsumer.consumer.model.RequestMessage;
import com.github.ricardocomar.springbootetl.etlconsumer.fixture.TeamModelFixture;
import com.github.ricardocomar.springbootetl.model.PurchaseAvro;
import com.github.ricardocomar.springbootetl.model.TeamAvro;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SDFConsumerApplication.class)
@ContextConfiguration(initializers = ConfigFileApplicationContextInitializer.class)
public abstract class AbstractApplicationTests {

	@ClassRule
	public static final EmbeddedKafkaRule rule = new EmbeddedKafkaRule(1, true, "topicOutbound");

	@BeforeClass
	public static void setUp() {
		FixtureFactoryLoader.loadTemplates(TeamModelFixture.class.getPackage().getName());
	}

	@Autowired
	private JmsTemplate jmsTemplate;

	private static GenericRecord RESPONSE_AVRO;
	private static String RESPONSE_REQUEST_ID;
	private final String lock = "lock";

	private GenericRecord expectedResponse;

	private String requestId;

	@Before
	public void beforeEachTest() {
		RESPONSE_AVRO = null;
		RESPONSE_REQUEST_ID = null;

		expectedResponse = null;
		requestId = UUID.randomUUID().toString();
	}

	@After
	public void afterEachTest() {
		assertThat(requestId, equalTo(RESPONSE_REQUEST_ID));
		assertThat(expectedResponse, equalTo(RESPONSE_AVRO));
	}

	@Test
	public void simpleTeamMessage() {
		expectedResponse = Fixture.from(TeamAvro.class).gimme("valid");

		sendAndWait(Fixture.from(RequestMessage.class).gimme("team"), requestId);
	}

	@Test
	public void simplePurchaseMessage() {
		expectedResponse = Fixture.from(PurchaseAvro.class).gimme("valid");

		sendAndWait(Fixture.from(RequestMessage.class).gimme("purchase"), requestId);
	}

	private void sendAndWait(final RequestMessage msg, final String requestId) {
		jmsTemplate.convertAndSend("queue.sample", msg.getTrancode(), new MessagePostProcessor() {
			@Override
			public Message postProcessMessage(final Message message) throws JMSException {
				message.setStringProperty(AppProperties.HEADER_REQUEST_ID, requestId);
				return message;
			}
		});

		try {
			synchronized (lock) {
				lock.wait(500);
			}
		} catch (final InterruptedException e) {
		}
	}

	@Bean
	@KafkaListener(topics = "topicOutbound", groupId = "test-${random.value}")
	public void consumeResponse(@Payload final TeamAvro message,
			@Header(AppProperties.HEADER_REQUEST_ID) final String requestId) throws Exception {

		RESPONSE_AVRO = message;
		RESPONSE_REQUEST_ID = requestId;
		synchronized (lock) {
			lock.notify();
		}
	}

}
