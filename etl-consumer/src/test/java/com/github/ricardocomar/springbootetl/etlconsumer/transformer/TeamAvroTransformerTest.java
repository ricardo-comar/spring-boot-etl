package com.github.ricardocomar.springbootetl.etlconsumer.transformer;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.ricardocomar.springbootetl.etlconsumer.consumer.model.EmployeeTrancode;
import com.github.ricardocomar.springbootetl.etlconsumer.consumer.model.TeamTrancode;
import com.github.ricardocomar.springbootetl.etlconsumer.fixture.TeamTrancodeFixture;
import com.github.ricardocomar.springbootetl.etlconsumer.mapper.MapperSpringConfig;
import com.github.ricardocomar.springbootetl.model.TeamAvro;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TransformerSpringConfig.class, MapperSpringConfig.class })
public class TeamAvroTransformerTest {

	@Autowired
	private TeamAvroTransformer transformer;

	@BeforeClass
	public static void setUp() {
		FixtureFactoryLoader.loadTemplates(TeamTrancodeFixture.class.getPackage().getName());
	}

	@Test
	public void testValid() throws Exception {
		
		final TeamTrancode trancodeTeam = Fixture.from(TeamTrancode.class).gimme("valid");
		trancodeTeam.setEmployees(Fixture.from(EmployeeTrancode.class).gimme(3, "boss", "dev1", "dev2"));

		final TeamAvro avro = transformer.from(trancodeTeam);
		assertThat(avro, notNullValue());

		final TeamTrancode newTrancode = transformer.to(avro);

		assertThat(newTrancode, equalTo(trancodeTeam));
	}
}
