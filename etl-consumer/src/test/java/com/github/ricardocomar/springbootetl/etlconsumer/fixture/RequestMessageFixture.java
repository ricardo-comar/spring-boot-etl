package com.github.ricardocomar.springbootetl.etlconsumer.fixture;

import java.util.UUID;

import com.github.ricardocomar.springbootetl.etlconsumer.consumer.model.RequestMessage;

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
				add("trancode",
						"Team ABC  " + "Jown        Snow      Boss           1200010   26092009ACTIVE    "
								+ "Tyrion 1    Lannister Developer 1    900005    26092014DISMISSED "
								+ "Tyrion 2    Lannister Developer 2    800004    26092015RESIGNED  ");
			}
		});

	}

}
