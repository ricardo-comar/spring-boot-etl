package com.github.ricardocomar.springbootetl.etlconsumer.validation;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.ricardocomar.springbootetl.etlconsumer.fixture.TeamModelFixture;
import com.github.ricardocomar.springbootetl.etlconsumer.model.Employee;
import com.github.ricardocomar.springbootetl.etlconsumer.model.Team;

import br.com.fluentvalidator.exception.ValidationException;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ValidationSpringConfig.class)
public class ModelTeamValidationTest {

	@Autowired
	private ValidatorTeam validator;

	@BeforeClass
	public static void setUp() {
		FixtureFactoryLoader.loadTemplates(TeamModelFixture.class.getPackage().getName());
	}

	@Test
	public void testValid() {

		final Team team = Fixture.from(Team.class).gimme("valid");

		validator.validate(team);
	}

	@Test(expected = ValidationException.class)
	public void testEmptyName() throws Exception {

		final Team team = Fixture.from(Team.class).gimme("valid");
		team.setTeamName(null);

		validator.validate(team);
	}

	@Test(expected = ValidationException.class)
	public void testEmptyEmployees() throws Exception {

		final Team team = Fixture.from(Team.class).gimme("valid");
		team.setEmployees(null);

		validator.validate(team);
	}

	@Test(expected = ValidationException.class)
	public void testOverfloadedEmployees() throws Exception {

		final Team team = Fixture.from(Team.class).gimme("valid");
		team.setEmployees(Fixture.from(Employee.class).gimme(5, "boss"));

		validator.validate(team);
	}
}
