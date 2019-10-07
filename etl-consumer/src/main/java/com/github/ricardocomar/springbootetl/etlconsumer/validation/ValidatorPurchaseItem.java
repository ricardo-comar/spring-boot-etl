package com.github.ricardocomar.springbootetl.etlconsumer.validation;

import static br.com.fluentvalidator.predicate.ComparablePredicate.greaterThanOrEqual;
import static br.com.fluentvalidator.predicate.LogicalPredicate.not;
import static br.com.fluentvalidator.predicate.StringPredicate.stringEmptyOrNull;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.github.ricardocomar.springbootetl.etlconsumer.model.PurchaseItem;

import br.com.fluentvalidator.AbstractValidator;

@Component
public class ValidatorPurchaseItem extends AbstractValidator<PurchaseItem> {
	
	@Override
	public void rules() {

		failFastRule();

		ruleFor("id", PurchaseItem::getId).must(not(stringEmptyOrNull())).withMessage("id is mandatory")
				.critical(ETLValidationException.class);

		ruleFor("sku", PurchaseItem::getSku).must(not(stringEmptyOrNull())).withMessage("id is mandatory")
				.critical(ETLValidationException.class);

		ruleFor("title", PurchaseItem::getTitle).must(not(stringEmptyOrNull())).withMessage("title is mandatory")
				.critical(ETLValidationException.class);

		ruleFor("value", PurchaseItem::getValue).must(greaterThanOrEqual(BigDecimal.ZERO))
				.withMessage("value is mandatory and must be >= 0.0").critical(ETLValidationException.class);

	}


}
