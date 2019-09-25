package com.github.ricardocomar.springdataflowetl.etlconsumer.transformer;

import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.ricardocomar.springdataflowetl.etlconsumer.fixture.TeamTrancodeFixture;
import com.github.ricardocomar.springdataflowetl.etlconsumer.model.trancode.EmployeeTrancode;
import com.github.ricardocomar.springdataflowetl.etlconsumer.model.trancode.TeamTrancode;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TransformerSpringConfig.class)
public class TeamTrancodeTransformerTest {

	@Autowired
	private TeamTrancodeTransformer transformer;

	@BeforeClass
	public static void setUp() {
		FixtureFactoryLoader.loadTemplates(TeamTrancodeFixture.class.getPackage().getName());
	}

	@Test
	public void testValid() throws Exception {

		final TeamTrancode trancodeTeam = Fixture.from(TeamTrancode.class).gimme("valid");
		trancodeTeam.setEmployees(Fixture.from(EmployeeTrancode.class).gimme(3, "boss", "dev1", "dev2"));

		final String trancode = transformer.to(trancodeTeam);
		assertThat(trancode, not(emptyOrNullString()));

		final TeamTrancode newTrancode = transformer.from(trancode);

		assertThat(newTrancode, equalTo(trancodeTeam));
	}
}
