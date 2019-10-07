package com.github.ricardocomar.springbootetl.etlconsumer.transformer;

public class ETLTransformerException extends RuntimeException {

	public ETLTransformerException() {
	}

	public ETLTransformerException(final String msg) {
		super(msg);
	}

	public ETLTransformerException(final Throwable ex) {
		super(ex);
	}

	public ETLTransformerException(final String msg, final Throwable ex) {
		super(msg, ex);
	}

	/**	 */
	private static final long serialVersionUID = 8090569498845335399L;

}
