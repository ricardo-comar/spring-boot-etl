package com.github.ricardocomar.springdataflowetl.etlconsumer.transformer;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.github.ricardocomar.springdataflowetl.etlconsumer.mapper.MapperSpringConfig;

@Configuration
@ComponentScan(basePackageClasses = { TransformerSpringConfig.class, MapperSpringConfig.class })
public class TransformerSpringConfig {

}
