package com.github.ricardocomar.springbootetl.etlconsumer.fixture;

import com.github.ricardocomar.springbootetl.etlconsumer.model.Employee;
import com.github.ricardocomar.springbootetl.etlconsumer.model.Team;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

public class TeamTrancodeFixture implements TemplateLoader {

	@Override
	public void load() {
		Fixture.of(Team.class).addTemplate("valid", new Rule() {
			{
				add("teamName", "Team ABC");
				add("employees", has(3).of(Employee.class, "boss", "dev1", "dev2"));
			}
		});

	}

}
