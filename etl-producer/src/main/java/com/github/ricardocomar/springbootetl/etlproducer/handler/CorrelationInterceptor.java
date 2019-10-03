package com.github.ricardocomar.springbootetl.etlproducer.handler;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.github.ricardocomar.springbootetl.etlproducer.config.AppProperties;

@Component
public class CorrelationInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
			throws Exception {
		final String correlationId = getCorrelationIdFromHeader(request);
		MDC.put(AppProperties.PROP_CORRELATION_ID, correlationId);
		return true;
	}

	@Override
	public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response,
			final Object handler, final Exception ex) throws Exception {
		MDC.remove(AppProperties.PROP_CORRELATION_ID);
	}

	private String getCorrelationIdFromHeader(final HttpServletRequest request) {
		String correlationId = request.getHeader(AppProperties.HEADER_CORRELATION_ID);
		if (StringUtils.isBlank(correlationId)) {
			correlationId = generateUniqueCorrelationId();
		}
		return correlationId;
	}

	private String generateUniqueCorrelationId() {
		return UUID.randomUUID().toString();
	}
}
