package com.github.ricardocomar.springdataflowetl.etlconsumer.fixture;

import com.github.ricardocomar.springdataflowetl.etlconsumer.model.avro.TeamAvro;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

public class TeamAvroFixture implements TemplateLoader {

	@Override
	public void load() {
		Fixture.of(TeamAvro.class).addTemplate("valid", new Rule() {
			{
				add("teamName", "Team ABC");
			}
		});

	}

}
