package com.github.ricardocomar.springdataflowetl.etlconsumer.config;

import org.jboss.logging.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class LogConfiguration implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private Environment env;

	@Autowired
	private AppProperties props;

	@Override
	public void onApplicationEvent(final ContextRefreshedEvent event) {
		MDC.put("instance-id", props.getInstanceId());
	}

	
}
