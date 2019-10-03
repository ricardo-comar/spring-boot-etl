package com.github.ricardocomar.springbootetl.etlconsumer;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.jms.JMSException;
import javax.jms.Message;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.ricardocomar.springbootetl.etlconsumer.AbstractApplicationTests.IntegrationConfig;
import com.github.ricardocomar.springbootetl.etlconsumer.config.AppProperties;
import com.github.ricardocomar.springbootetl.etlconsumer.consumer.model.RequestMessage;
import com.github.ricardocomar.springbootetl.etlconsumer.fixture.TeamTrancodeFixture;
import com.github.ricardocomar.springbootetl.model.EmployeeAvro;
import com.github.ricardocomar.springbootetl.model.TeamAvro;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SDFConsumerApplication.class, webEnvironment = WebEnvironment.DEFINED_PORT)
@ContextConfiguration(initializers = ConfigFileApplicationContextInitializer.class, classes = IntegrationConfig.class)
public abstract class AbstractApplicationTests {

	@ClassRule
	public static final EmbeddedKafkaRule rule = new EmbeddedKafkaRule(1, true, "topicOutbound");

	@BeforeClass
	public static void setUp() {
		FixtureFactoryLoader.loadTemplates(TeamTrancodeFixture.class.getPackage().getName());
	}

	@Test
	public void contextLoads() {
	}

	@Autowired
	private JmsTemplate jmsTemplate;


	private static TeamAvro RESPONSE_AVRO;
	private static String RESPONSE_REQUEST_ID;

	@Test
	public void simpleMessage() {

		RESPONSE_AVRO = null;
		RESPONSE_REQUEST_ID = null;

		final RequestMessage msg = Fixture.from(RequestMessage.class).gimme("valid");
		final TeamAvro teamAvro = Fixture.from(TeamAvro.class).gimme("valid");
		final String requestId = UUID.randomUUID().toString();

		jmsTemplate.convertAndSend("queue.sample", msg.getTrancode(), new MessagePostProcessor() {
			@Override
			public Message postProcessMessage(final Message message) throws JMSException {
				message.setStringProperty(AppProperties.HEADER_REQUEST_ID, requestId);
				return message;
			}
		});

		try {
			Thread.sleep(2000);
		} catch (final InterruptedException e) {
		}

		assertThat(requestId, equalTo(RESPONSE_REQUEST_ID));
		assertThat(teamAvro, equalTo(RESPONSE_AVRO));
	}
	
	@Configuration
	public static class IntegrationConfig {
		
		@Component
		public class TestKafkaListener {
			
			@KafkaListener(topics = "topicOutbound", groupId = "test-${random.value}")
			public void consumeResponse(@Payload final TeamAvro message,
					@Header(AppProperties.HEADER_REQUEST_ID) final String requestId)
					throws Exception {

				RESPONSE_AVRO = message;
				RESPONSE_REQUEST_ID = requestId;
			}
		}

		@RestController
		public class SchemaRegistryStubController {

			private final Map<String, Schema> map = new HashMap<>();

			/**
			 * Register the acro objects you need stubbed. If there are many of them, this
			 * could be externalized into a more scalable pattern on controller load
			 */
			public SchemaRegistryStubController() {
				// Need to escape the string with some library that will encode it as json
				// value.
				// The Avro object has the SCHEMA$ static that is the schema on it.
				map.put("team-object-value", new Schema(1, "{\"schema\":\"" + TeamAvro.SCHEMA$.toString() + "\"}"));
				map.put("team-object-key", new Schema(2, "{\"schema\":\"\\\"string\\\"\"}"));
				map.put("employee-object-value",
						new Schema(3, "{\"schema\":\"" + EmployeeAvro.SCHEMA$.toString() + "\"}"));
				map.put("employee-object-key", new Schema(4, "{\"schema\":\"\\\"string\\\"\"}"));
			}

			/**
			 * Method that gets a version of an object, version is unique for both key and
			 * values
			 * 
			 * @param subject name of schema
			 * @param body    post body to ignore for now
			 * @return id of the schema
			 */
			@PostMapping(path = "/subjects/{subject}/versions")
			public ResponseEntity<String> getSchemaVersion(
					@PathVariable(name = "subject", required = true) final String subject,
					@RequestBody final String body) {
				return ResponseEntity.ok("{\"id\":" + map.get(subject).id + "}");
			}

			/**
			 * Get the schema by id
			 * 
			 * @param id id of the schema
			 * @return the schema registry json for the schema
			 */
			@GetMapping(path = "/schemas/ids/{id}")
			public ResponseEntity<String> getSchema(@PathVariable(name = "id", required = true) final Integer id) {
				return ResponseEntity.ok(map.values().stream().filter(schema -> schema.id.equals(id)).findFirst()
						.orElse(new Schema(0, "{\"schema\":\"\"}")).schema);
			}

			/**
			 * Schema object for easy of registry and tracking id
			 */
			class Schema {
				Integer id;
				String schema;

				Schema(final Integer id, final String schema) {
					this.id = id;
					this.schema = schema;
				}
			}
		}

	}

}
