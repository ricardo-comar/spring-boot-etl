package com.github.ricardocomar.springdataflowetl.etlconsumer.processor;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.ricardocomar.springdataflowetl.etlconsumer.fixture.RequestMessageFixture;
import com.github.ricardocomar.springdataflowetl.etlconsumer.mapper.MapperSpringConfig;
import com.github.ricardocomar.springdataflowetl.etlconsumer.model.avro.EmployeeAvro;
import com.github.ricardocomar.springdataflowetl.etlconsumer.model.avro.TeamAvro;
import com.github.ricardocomar.springdataflowetl.etlconsumer.producer.ReturnProducer;
import com.github.ricardocomar.springdataflowetl.etlconsumer.transformer.TransformerSpringConfig;
import com.github.ricardocomar.springdataflowetl.etlconsumer.validation.ValidationSpringConfig;
import com.github.ricardocomar.springdataflowetl.model.RequestMessage;
import com.github.ricardocomar.springdataflowetl.model.ResponseMessage;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { MessageProcessor.class, ValidationSpringConfig.class, TransformerSpringConfig.class,
		MapperSpringConfig.class })
public class MessageProcessorTest {

	@Autowired
	private MessageProcessor processor;

	@MockBean
	private ReturnProducer mockProducer;

	@BeforeClass
	public static void setUp() {
		FixtureFactoryLoader.loadTemplates(RequestMessageFixture.class.getPackage().getName());
	}

	@Test
	public void testValid() throws Exception {

		Mockito.doAnswer((Answer<?>) invocation -> {
			final TeamAvro arg0 = invocation.getArgument(0);
			final TeamAvro team = Fixture.from(TeamAvro.class).gimme("valid");
			team.setEmployees(Fixture.from(EmployeeAvro.class).gimme(3, "boss", "dev1", "dev2"));

			assertEquals(team, arg0);
			return null;
		}).when(mockProducer).sendMessage(Mockito.any(ResponseMessage.class));

		final RequestMessage requestMessage = Fixture.from(RequestMessage.class).gimme("valid");
		processor.process(requestMessage.getPayload());

		Mockito.verify(mockProducer, Mockito.atLeastOnce()).sendMessage(Mockito.any(ResponseMessage.class));

	}
}
