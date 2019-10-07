package com.github.ricardocomar.springbootetl.etlconsumer.fixture;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.github.ricardocomar.springbootetl.model.EmployeeAvro;
import com.github.ricardocomar.springbootetl.model.EmployeeAvroStatus;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

public class EmployeeAvroFixture implements TemplateLoader {

	private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	@Override
	public void load() {
		Fixture.of(EmployeeAvro.class).addTemplate("boss", new Rule() {
			{
				add("title", "Boss");
				add("firstName", "Jown");
				add("lastName", "Snow");
				add("salary", new BigDecimal(12000.10).setScale(2, RoundingMode.HALF_EVEN));
				add("status", EmployeeAvroStatus.ACTIVE);
				add("hireDate", LocalDate.now().minusYears(10).format(TIME_FORMATTER));
			}
		});
		Fixture.of(EmployeeAvro.class).addTemplate("dev1", new Rule() {
			{
				add("title", "Developer 1");
				add("firstName", "Tyrion 1");
				add("lastName", "Lannister");
				add("salary", new BigDecimal(9000.05).setScale(2, RoundingMode.HALF_EVEN));
				add("status", EmployeeAvroStatus.DISMISSED);
				add("hireDate", LocalDate.now().minusYears(5).format(TIME_FORMATTER));
			}
		});
		Fixture.of(EmployeeAvro.class).addTemplate("dev2", new Rule() {
			{
				add("title", "Developer 2");
				add("firstName", "Tyrion 2");
				add("lastName", "Lannister");
				add("salary", new BigDecimal(8000.04).setScale(2, RoundingMode.HALF_EVEN));
				add("status", EmployeeAvroStatus.RESIGNED);
				add("hireDate", LocalDate.now().minusYears(4).format(TIME_FORMATTER));
			}
		});
		Fixture.of(EmployeeAvro.class).addTemplate("dev3", new Rule() {
			{
				add("title", "Developer 3");
				add("firstName", "Tyrion 3");
				add("lastName", "Lannister");
				add("salary", new BigDecimal(7000.03).setScale(2, RoundingMode.HALF_EVEN));
				add("status", EmployeeAvroStatus.RETIRED);
				add("hireDate", LocalDate.now().minusYears(3).format(TIME_FORMATTER));
			}
		});

	}

}
