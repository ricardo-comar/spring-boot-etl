package com.github.ricardocomar.springdataflowetl.etlconsumer.fixture;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.github.ricardocomar.springdataflowetl.etlconsumer.model.avro.EmployeeAvro;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

public class EmployeeAvroFixture implements TemplateLoader {

	@Override
	public void load() {
		Fixture.of(EmployeeAvro.class).addTemplate("boss", new Rule() {
			{
				add("title", "Boss");
				add("firstName", "Jown");
				add("lastName", "Snow");
				add("salary", new BigDecimal("12000.10"));
				add("hireDate", LocalDate.now().minusYears(10));
			}
		});
		Fixture.of(EmployeeAvro.class).addTemplate("dev1", new Rule() {
			{
				add("title", "Developer 1");
				add("firstName", "Tyrion 1");
				add("lastName", "Lannister");
				add("salary", new BigDecimal("9000.05"));
				add("hireDate", LocalDate.now().minusYears(5));
			}
		});
		Fixture.of(EmployeeAvro.class).addTemplate("dev2", new Rule() {
			{
				add("title", "Developer 2");
				add("firstName", "Tyrion 2");
				add("lastName", "Lannister");
				add("salary", new BigDecimal("8000.04"));
				add("hireDate", LocalDate.now().minusYears(4));
			}
		});
		Fixture.of(EmployeeAvro.class).addTemplate("dev3", new Rule() {
			{
				add("title", "Developer 3");
				add("firstName", "Tyrion 3");
				add("lastName", "Lannister");
				add("salary", new BigDecimal("7000.03"));
				add("hireDate", LocalDate.now().minusYears(3));
			}
		});

	}

}
