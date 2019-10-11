package com.github.ricardocomar.springbootetl.etlconsumer.transformer;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.apache.avro.generic.GenericRecord;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.ricardocomar.springbootetl.etlconsumer.fixture.TeamModelFixture;
import com.github.ricardocomar.springbootetl.etlconsumer.mapper.MapperSpringConfig;
import com.github.ricardocomar.springbootetl.etlconsumer.model.ConsumerModel;
import com.github.ricardocomar.springbootetl.etlconsumer.model.Purchase;
import com.github.ricardocomar.springbootetl.etlconsumer.model.Team;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TransformerSpringConfig.class, MapperSpringConfig.class })
public class AvroTransformerTest {

	@Autowired
	private AvroTransformer transformer;

	@BeforeClass
	public static void setUp() {
		FixtureFactoryLoader.loadTemplates(TeamModelFixture.class.getPackage().getName());
	}

	@Test
	public void testValidTeam() throws Exception {
		
		final Team trancodeTeam = Fixture.from(Team.class).gimme("valid");

		final GenericRecord avro = transformer.from(trancodeTeam);
		assertThat(avro, notNullValue());

		final ConsumerModel newTrancode = transformer.to(avro);

		assertThat(newTrancode, equalTo(trancodeTeam));
	}

	@Test
	public void testValidPurchase() throws Exception {

		final Purchase trancode = Fixture.from(Purchase.class).gimme("valid");

		final GenericRecord avro = transformer.from(trancode);
		assertThat(avro, notNullValue());

		final ConsumerModel newTrancode = transformer.to(avro);

		assertThat(newTrancode, equalTo(trancode));
	}
}
