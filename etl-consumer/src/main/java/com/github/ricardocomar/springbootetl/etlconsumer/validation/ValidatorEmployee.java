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

		ruleFor(Employee::getFirstName)
			.must(not(stringEmptyOrNull()))
				.withMessage("first name is mandatory")
				.withFieldName("firstName")
				.critical(ETLValidationException.class);

		ruleFor(Employee::getLastName)
			.must(not(stringEmptyOrNull()))
				.withMessage("last name is mandatory")
				.withFieldName("lastName")
				.critical(ETLValidationException.class);

		ruleFor(Employee::getTitle)
			.must(not(stringEmptyOrNull()))
				.withMessage("title is mandatory")
				.withFieldName("title")
				.critical(ETLValidationException.class);

		ruleFor(Employee::getSalary)
			.must(not(nullValue()))
				.withMessage("salary is mandatory")
				.withFieldName("salary")
				.must(greaterThan(new BigDecimal(0.0))).withMessage("salary must be a positive number")
				.withFieldName("salary")
				.critical(ETLValidationException.class);

		ruleFor(Employee::getHireDate)
			.must(not(nullValue()))
				.withMessage("hire date is mandatory")
				.withFieldName("hireDate")
				.must(lessThanOrEqual(LocalDate.now()))
				.withMessage("hire date must be in the past")
				.withFieldName("hireDate")
				.critical(ETLValidationException.class);

	}


}
