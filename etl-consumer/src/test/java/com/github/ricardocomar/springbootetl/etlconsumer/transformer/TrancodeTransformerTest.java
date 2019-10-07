package com.github.ricardocomar.springbootetl.etlconsumer.transformer;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.ricardocomar.springbootetl.etlconsumer.fixture.TeamModelFixture;
import com.github.ricardocomar.springbootetl.etlconsumer.model.ConsumerModel;
import com.github.ricardocomar.springbootetl.etlconsumer.model.Purchase;
import com.github.ricardocomar.springbootetl.etlconsumer.model.Team;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TransformerSpringConfig.class)
public class TrancodeTransformerTest {

	@Autowired
	private TrancodeTransformer transformer;

	@BeforeClass
	public static void setUp() {
		FixtureFactoryLoader.loadTemplates(TeamModelFixture.class.getPackage().getName());
	}

	@Test
	public void testValidTeam() throws Exception {

		final Team trancodeTeam = Fixture.from(Team.class).gimme("valid");

		final String trancode = transformer.to(trancodeTeam);
		assertThat(trancode, not(isEmptyOrNullString()));

		final ConsumerModel newModel = transformer.fromTrancode(trancode);

		assertThat(newModel, equalTo(trancodeTeam));
	}

	@Test
	public void testValidPurchase() throws Exception {

		final Purchase purchase = Fixture.from(Purchase.class).gimme("valid");

		final String trancode = transformer.to(purchase);
		assertThat(trancode, not(isEmptyOrNullString()));

		final ConsumerModel newModel = transformer.fromTrancode(trancode);

		assertThat(newModel, equalTo(purchase));
	}
}
