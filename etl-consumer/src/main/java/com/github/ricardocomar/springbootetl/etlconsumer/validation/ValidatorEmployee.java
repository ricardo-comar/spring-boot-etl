package com.github.ricardocomar.springbootetl.etlconsumer.validation;

import static br.com.fluentvalidator.predicate.ComparablePredicate.greaterThan;
import static br.com.fluentvalidator.predicate.ComparablePredicate.lessThanOrEqual;
import static br.com.fluentvalidator.predicate.LogicalPredicate.not;
import static br.com.fluentvalidator.predicate.ObjectPredicate.nullValue;
import static br.com.fluentvalidator.predicate.StringPredicate.stringEmptyOrNull;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.github.ricardocomar.springbootetl.etlconsumer.model.Employee;

import br.com.fluentvalidator.AbstractValidator;

@Component
public class ValidatorEmployee extends AbstractValidator<Employee> {
	
	@Override
	public void rules() {

		failFastRule();

		ruleFor("firstName", Employee::getFirstName)
			.must(not(stringEmptyOrNull()))
				.withMessage("first name is mandatory")
				.critical(ETLValidationException.class);

		ruleFor("lastName", Employee::getLastName)
			.must(not(stringEmptyOrNull()))
				.withMessage("last name is mandatory")
				.critical(ETLValidationException.class);

		ruleFor("title", Employee::getTitle)
			.must(not(stringEmptyOrNull()))
				.withMessage("title is mandatory")
				.critical(ETLValidationException.class);

		ruleFor("salary", Employee::getSalary)
			.must(not(nullValue()))
				.withMessage("salary is mandatory")
				.must(greaterThan(new BigDecimal(0.0))).withMessage("salary must be a positive number")
				.critical(ETLValidationException.class);

		ruleFor("hireDate", Employee::getHireDate)
			.must(not(nullValue()))
				.withMessage("hire date is mandatory")
				.must(lessThanOrEqual(LocalDate.now()))
				.withMessage("hire date must be in the past")
				.critical(ETLValidationException.class);

	}


}
