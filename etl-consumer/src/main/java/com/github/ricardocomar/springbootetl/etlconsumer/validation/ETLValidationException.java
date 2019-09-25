package com.github.ricardocomar.springbootetl.etlconsumer.validation;

import br.com.fluentvalidator.context.ValidationResult;
import br.com.fluentvalidator.exception.ValidationException;

public class ETLValidationException extends ValidationException {

	public ETLValidationException(final ValidationResult validationResult) {
		super(validationResult);
	}

	/**	 */
	private static final long serialVersionUID = 8090569498845335399L;

}
