package com.github.ricardocomar.springbootetl.etlconsumer.fixture;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import com.github.ricardocomar.springbootetl.etlconsumer.consumer.model.RequestMessage;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

public class RequestMessageFixture implements TemplateLoader {
	private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("ddMMyyyy");

	@Override
	public void load() {
		Fixture.of(RequestMessage.class).addTemplate("team", new Rule() {
			{
				final String hire1 = LocalDate.now().minusYears(10).format(TIME_FORMATTER);
				final String hire2 = LocalDate.now().minusYears(5).format(TIME_FORMATTER);
				final String hire3 = LocalDate.now().minusYears(4).format(TIME_FORMATTER);
				add("id", UUID.randomUUID().toString());
				add("origin", "TestSuite");
				add("trancode",
						"TRANTEAM-1" + "Team ABC  " + "Jown        Snow      Boss           1200010   " + hire1
								+ "ACTIVE    "
								+ "Tyrion 1    Lannister Developer 1    900005    " + hire2 + "DISMISSED "
								+ "Tyrion 2    Lannister Developer 2    800004    " + hire3 + "RESIGNED  ");
			}
		});
		Fixture.of(RequestMessage.class).addTemplate("purchase", new Rule() {
			{
				final String date = LocalDate.now().minusDays(10).format(TIME_FORMATTER);
				add("id", UUID.randomUUID().toString());
				add("origin", "TestSuite");
				add("trancode",
						"TRANPURC-1" + "purc-10     Lala Silva                    1590000   OPEN      " + date
								+ "123-45-6    sku123456 Motorola G6 Play  50000     "
								+ "654-32-1    sku654321 Iphone 10         390000    "
								+ "999-99-9    sku999999 Iphone 11 Pro     1150000   ");
			}
		});

	}

}
