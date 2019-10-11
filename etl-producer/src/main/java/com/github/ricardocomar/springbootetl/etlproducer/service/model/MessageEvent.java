package com.github.ricardocomar.springbootetl.etlproducer.service.model;

import org.apache.avro.generic.GenericRecord;
import org.springframework.context.ApplicationEvent;

public class MessageEvent extends ApplicationEvent {

	private final String requestId;

	public MessageEvent(final String requestId, final GenericRecord source) {
		super(source);
		this.requestId = requestId;
	}

	public String getRequestId() {
		return requestId;
	}
	
	public GenericRecord getResponse() {
		return super.getSource() != null ? (GenericRecord) super.getSource() : null;
	}

	private static final long serialVersionUID = 8366022661649087L;

}
