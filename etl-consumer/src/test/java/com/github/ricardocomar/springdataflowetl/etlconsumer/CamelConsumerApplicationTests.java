package com.github.ricardocomar.springdataflowetl.etlconsumer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.ricardocomar.springdataflowetl.etlconsumer.SDFConsumerApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SDFConsumerApplication.class)
@ContextConfiguration(initializers = ConfigFileApplicationContextInitializer.class)
@ActiveProfiles("test") // Like this
public class CamelConsumerApplicationTests {
	
	@Test
	public void contextLoads() {
	}

}
