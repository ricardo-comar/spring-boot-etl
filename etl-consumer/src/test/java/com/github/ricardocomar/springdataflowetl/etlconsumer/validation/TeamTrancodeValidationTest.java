package com.github.ricardocomar.springdataflowetl.etlconsumer.validation;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.ricardocomar.springdataflowetl.etlconsumer.fixture.TeamTrancodeFixture;
import com.github.ricardocomar.springdataflowetl.etlconsumer.model.trancode.EmployeeTrancode;
import com.github.ricardocomar.springdataflowetl.etlconsumer.model.trancode.TeamTrancode;

import br.com.fluentvalidator.exception.ValidationException;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ValidationSpringConfig.class)
public class TeamTrancodeValidationTest {

	@Autowired
	private ValidatorTeam validator;

	@BeforeClass
	public static void setUp() {
		FixtureFactoryLoader.loadTemplates(TeamTrancodeFixture.class.getPackage().getName());
	}

	@Test
	public void testValid() {

		final TeamTrancode trancodeTeam = Fixture.from(TeamTrancode.class).gimme("valid");
		trancodeTeam.setEmployees(Fixture.from(EmployeeTrancode.class).gimme(3, "boss", "dev1", "dev2"));

		validator.validate(trancodeTeam);
	}

	@Test(expected = ValidationException.class)
	public void testEmptyTeamName() throws Exception {

		final TeamTrancode trancodeTeam = Fixture.from(TeamTrancode.class).gimme("valid");
		trancodeTeam.setTeamName(null);

		validator.validate(trancodeTeam);
	}

	@Test(expected = ValidationException.class)
	public void testEmptyEmployees() throws Exception {

		final TeamTrancode trancodeTeam = Fixture.from(TeamTrancode.class).gimme("valid");

		validator.validate(trancodeTeam);
	}

	@Test(expected = ValidationException.class)
	public void testOverloadedEmployees() throws Exception {

		final TeamTrancode trancodeTeam = Fixture.from(TeamTrancode.class).gimme("valid");
		trancodeTeam.setEmployees(Fixture.from(EmployeeTrancode.class).gimme(5, "boss"));

		validator.validate(trancodeTeam);
	}
}
