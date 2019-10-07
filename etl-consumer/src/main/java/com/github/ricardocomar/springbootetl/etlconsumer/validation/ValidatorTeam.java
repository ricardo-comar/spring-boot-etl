package com.github.ricardocomar.springbootetl.etlconsumer.validation;

import static br.com.fluentvalidator.predicate.ComparablePredicate.between;
import static br.com.fluentvalidator.predicate.LogicalPredicate.not;
import static br.com.fluentvalidator.predicate.ObjectPredicate.nullValue;
import static br.com.fluentvalidator.predicate.StringPredicate.stringEmptyOrNull;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.ricardocomar.springbootetl.etlconsumer.model.Team;

import br.com.fluentvalidator.AbstractValidator;

@Component
public class ValidatorTeam extends AbstractValidator<Team> {
	
	@Autowired
	private ValidatorEmployee valEmployee;

	@Override
	public void rules() {

		failFastRule();

		ruleFor(Team::getTeamName).must(not(stringEmptyOrNull())).when(stringEmptyOrNull())
				.withMessage("name is mandatory").withFieldName("teamName").critical(ETLValidationException.class);

		ruleFor(Team::getEmployees).must(between(Collection::size, 0, 5))
				.withMessage("employees is mandatory and must be 1-4 size").withFieldName("teamName")
				.critical(ETLValidationException.class)
				;

		ruleForEach(Team::getEmployees).must(not(nullValue())).withMessage("team emplyees cannot be null")
				.whenever(not(nullValue())).withValidator(valEmployee).critical(ETLValidationException.class);
	}


}
