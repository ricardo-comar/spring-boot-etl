package com.github.ricardocomar.springbootetl.etlconsumer.fixture;

import java.util.UUID;

import com.github.ricardocomar.springbootetl.model.RequestMessage;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

public class RequestMessageFixture implements TemplateLoader {

	@Override
	public void load() {
		Fixture.of(RequestMessage.class).addTemplate("valid", new Rule() {
			{
				add("id", UUID.randomUUID().toString());
				add("origin", "TestSuite");
				add("payload",
						"Team ABC  " + "Jown        Snow      Boss           1200010   25092009"
								+ "Tyrion 1    Lannister Developer 1    900005    25092014"
								+ "Tyrion 2    Lannister Developer 2    800004    25092015");
			}
		});

	}

}
