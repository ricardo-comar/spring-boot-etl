package com.github.ricardocomar.springbootetl.etlproducer.service.model;

import org.springframework.context.ApplicationEvent;

import com.github.ricardocomar.springbootetl.model.TeamAvro;

public class MessageEvent extends ApplicationEvent {

	private final String requestId;

	public MessageEvent(final String requestId, final TeamAvro source) {
		super(source);
		this.requestId = requestId;
	}

	public String getRequestId() {
		return requestId;
	}
	
	public TeamAvro getResponse() {
		return super.getSource() != null ? (TeamAvro) super.getSource() : null;
	}

	private static final long serialVersionUID = 8366022661649087L;

}
