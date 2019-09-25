package com.github.ricardocomar.springbootetl.etlconsumer.validation;

import static br.com.fluentvalidator.predicate.ComparablePredicate.lessThanOrEqual;
import static br.com.fluentvalidator.predicate.LogicalPredicate.not;
import static br.com.fluentvalidator.predicate.ObjectPredicate.nullValue;
import static br.com.fluentvalidator.predicate.StringPredicate.isNumber;
import static br.com.fluentvalidator.predicate.StringPredicate.stringEmptyOrNull;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.github.ricardocomar.springbootetl.etlconsumer.consumer.model.EmployeeTrancode;

import br.com.fluentvalidator.AbstractValidator;

@Component
public class ValidatorEmployee extends AbstractValidator<EmployeeTrancode> {
	
	@Override
	public void rules() {

		failFastRule();

		ruleFor(EmployeeTrancode::getFirstName)
			.must(not(stringEmptyOrNull()))
				.withMessage("first name is mandatory")
				.withFieldName("firstName")
				.critical(ETLValidationException.class);

		ruleFor(EmployeeTrancode::getLastName)
			.must(not(stringEmptyOrNull()))
				.withMessage("last name is mandatory")
				.withFieldName("lastName")
				.critical(ETLValidationException.class);

		ruleFor(EmployeeTrancode::getTitle)
			.must(not(stringEmptyOrNull()))
				.withMessage("title is mandatory")
				.withFieldName("title")
				.critical(ETLValidationException.class);

		ruleFor(EmployeeTrancode::getSalary)
			.must(not(stringEmptyOrNull()))
				.withMessage("salary is mandatory")
				.withFieldName("salary")
			.must(isNumber())
				.withMessage("salary must be a number")
				.withFieldName("salary")
				.critical(ETLValidationException.class);

		ruleFor(EmployeeTrancode::getHireDate)
			.must(not(nullValue()))
				.withMessage("hire date is mandatory")
				.withFieldName("hireDate")
				.must(lessThanOrEqual(LocalDate.now()))
				.withMessage("hire date must be in the past")
				.withFieldName("hireDate")
				.critical(ETLValidationException.class);

	}


}
