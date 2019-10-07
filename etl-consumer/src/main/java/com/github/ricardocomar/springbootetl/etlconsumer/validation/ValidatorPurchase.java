package com.github.ricardocomar.springbootetl.etlconsumer.validation;

import static br.com.fluentvalidator.predicate.ComparablePredicate.between;
import static br.com.fluentvalidator.predicate.ComparablePredicate.lessThanOrEqual;
import static br.com.fluentvalidator.predicate.LogicalPredicate.not;
import static br.com.fluentvalidator.predicate.ObjectPredicate.equalTo;
import static br.com.fluentvalidator.predicate.ObjectPredicate.nullValue;
import static br.com.fluentvalidator.predicate.StringPredicate.stringEmptyOrNull;
import static br.com.fluentvalidator.predicate.StringPredicate.stringMatches;

import java.time.LocalDate;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.ricardocomar.springbootetl.etlconsumer.model.Purchase;

import br.com.fluentvalidator.AbstractValidator;

@Component
public class ValidatorPurchase extends AbstractValidator<Purchase> {
	
	@Autowired
	private ValidatorPurchaseItem valItem;

	@Override
	public void rules() {

		failFastRule();

		ruleFor("transaction", Purchase::getTransaction).must(stringMatches("TRANPURC-1"))
				.withMessage("transaction must be equal to TRANPURC-1").critical(ETLValidationException.class);

		ruleFor("id", Purchase::getId).must(not(stringEmptyOrNull())).withMessage("id is mandatory")
				.critical(ETLValidationException.class);

		ruleFor("customer", Purchase::getCustomer).must(not(stringEmptyOrNull())).withMessage("customer is mandatory")
				.critical(ETLValidationException.class);

		ruleFor("status", Purchase::getStatus).must(not(nullValue())).withMessage("status is mandatory")
				.critical(ETLValidationException.class);

		ruleFor("date", Purchase::getDate).must(not(nullValue())).withMessage("date is mandatory")
				.must(lessThanOrEqual(LocalDate.now())).withMessage("date must be in the past")
				.critical(ETLValidationException.class);

		ruleFor("items", Purchase::getItems).must(between(Collection::size, 0, 6))
				.withMessage("items is mandatory and must be 1-5 size").critical(ETLValidationException.class);
		ruleForEach("items", Purchase::getItems)
				.whenever(not(nullValue())).withValidator(valItem).critical(ETLValidationException.class);

		ruleFor("totalValue", Purchase::getTotalValue).must(not(nullValue())).withMessage("totalValue is mandatory")
				.critical(ETLValidationException.class);
		ruleFor("totalValue", Purchase::isValidTotalValue).must(equalTo(true)).withMessage("invalid totalValue")
				.critical(ETLValidationException.class);

	}


}
