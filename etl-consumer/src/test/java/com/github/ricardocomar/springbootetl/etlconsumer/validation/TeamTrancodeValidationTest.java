package com.github.ricardocomar.springbootetl.etlconsumer.validation;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.ricardocomar.springbootetl.etlconsumer.fixture.TeamTrancodeFixture;
import com.github.ricardocomar.springbootetl.etlconsumer.model.Employee;
import com.github.ricardocomar.springbootetl.etlconsumer.model.Team;

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

		final Team trancodeTeam = Fixture.from(Team.class).gimme("valid");
		trancodeTeam.setEmployees(Fixture.from(Employee.class).gimme(3, "boss", "dev1", "dev2"));

		validator.validate(trancodeTeam);
	}

	@Test(expected = ValidationException.class)
	public void testEmptyTeamName() throws Exception {

		final Team trancodeTeam = Fixture.from(Team.class).gimme("valid");
		trancodeTeam.setTeamName(null);

		validator.validate(trancodeTeam);
	}

	@Test(expected = ValidationException.class)
	public void testEmptyEmployees() throws Exception {

		final Team trancodeTeam = Fixture.from(Team.class).gimme("valid");

		validator.validate(trancodeTeam);
	}

	@Test(expected = ValidationException.class)
	public void testOverloadedEmployees() throws Exception {

		final Team trancodeTeam = Fixture.from(Team.class).gimme("valid");
		trancodeTeam.setEmployees(Fixture.from(Employee.class).gimme(5, "boss"));

		validator.validate(trancodeTeam);
	}
}
