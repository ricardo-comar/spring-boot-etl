package com.github.ricardocomar.springbootetl.etlconsumer.fixture;

import java.time.LocalDate;

import com.github.ricardocomar.springbootetl.etlconsumer.consumer.model.EmployeeTrancode;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

public class EmployeeTrancodeFixture implements TemplateLoader {

	@Override
	public void load() {
		Fixture.of(EmployeeTrancode.class).addTemplate("boss", new Rule() {
			{
				add("title", "Boss");
				add("firstName", "Jown");
				add("lastName", "Snow");
				add("salary", "1200010");
				add("hireDate", LocalDate.now().minusYears(10));
			}
		});
		Fixture.of(EmployeeTrancode.class).addTemplate("dev1", new Rule() {
			{
				add("title", "Developer 1");
				add("firstName", "Tyrion 1");
				add("lastName", "Lannister");
				add("salary", "900005");
				add("hireDate", LocalDate.now().minusYears(5));
			}
		});
		Fixture.of(EmployeeTrancode.class).addTemplate("dev2", new Rule() {
			{
				add("title", "Developer 2");
				add("firstName", "Tyrion 2");
				add("lastName", "Lannister");
				add("salary", "800004");
				add("hireDate", LocalDate.now().minusYears(4));
			}
		});
		Fixture.of(EmployeeTrancode.class).addTemplate("dev3", new Rule() {
			{
				add("title", "Developer 3");
				add("firstName", "Tyrion 3");
				add("lastName", "Lannister");
				add("salary", "700003");
				add("hireDate", LocalDate.now().minusYears(3));
			}
		});

	}

}
