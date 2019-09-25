package com.github.ricardocomar.springbootetl.etlconsumer.consumer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.ricardocomar.springbootetl.etlconsumer.config.AppProperties;

@RunWith(SpringRunner.class)
@EnableConfigurationProperties(AppProperties.class)
@ActiveProfiles("test")
public class MessageConsumerIntegrationTest {

	@Test
	public void testContext() throws Exception {
	}

}
