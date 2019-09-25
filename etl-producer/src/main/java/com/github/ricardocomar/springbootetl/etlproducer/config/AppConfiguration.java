package com.github.ricardocomar.springbootetl.etlproducer.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.github.ricardocomar.springbootetl.etlproducer.handler.CorrelationInterceptor;

@Component
public class AppConfiguration implements WebMvcConfigurer {

	@Autowired
	private CorrelationInterceptor interceptor;

	@Override
	public void addInterceptors(final InterceptorRegistry registry) {
		registry.addInterceptor(interceptor);
	}
}
