package com.github.ricardocomar.springbootetl.etlconsumer.fixture;

import com.github.ricardocomar.springbootetl.model.EmployeeAvro;
import com.github.ricardocomar.springbootetl.model.TeamAvro;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

public class TeamAvroFixture implements TemplateLoader {

	@Override
	public void load() {
		Fixture.of(TeamAvro.class).addTemplate("valid", new Rule() {
			{
				add("transaction", "TRANTEAM-1");
				add("teamName", "Team ABC");
				add("employees", has(3).of(EmployeeAvro.class, "boss", "dev1", "dev2"));
			}
		});
	}

}
