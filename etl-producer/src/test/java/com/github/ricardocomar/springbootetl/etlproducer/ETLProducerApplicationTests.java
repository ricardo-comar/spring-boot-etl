package com.github.ricardocomar.springbootetl.etlproducer;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.ricardocomar.springbootetl.etlproducer.SDFProducerApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SDFProducerApplication.class)
@ContextConfiguration(initializers = ConfigFileApplicationContextInitializer.class)
@ActiveProfiles("test") // Like this
public class ETLProducerApplicationTests {

	@ClassRule
	public static final EmbeddedKafkaRule rule = new EmbeddedKafkaRule(1, true, "topicInbound", "topicOutbound");

	@Test
	public void contextLoads() {
	}

}
