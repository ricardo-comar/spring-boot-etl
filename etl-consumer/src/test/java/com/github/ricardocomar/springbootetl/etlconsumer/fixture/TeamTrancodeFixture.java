package com.github.ricardocomar.springbootetl.etlconsumer.fixture;

import com.github.ricardocomar.springbootetl.etlconsumer.consumer.model.TeamTrancode;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

public class TeamTrancodeFixture implements TemplateLoader {

	@Override
	public void load() {
		Fixture.of(TeamTrancode.class).addTemplate("valid", new Rule() {
			{
				add("teamName", "Team ABC");
			}
		});

	}

}
