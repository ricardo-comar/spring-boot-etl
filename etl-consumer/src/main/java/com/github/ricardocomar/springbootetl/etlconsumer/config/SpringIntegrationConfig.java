package com.github.ricardocomar.springbootetl.etlconsumer.config;

import javax.jms.ConnectionFactory;

import org.apache.avro.specific.SpecificRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.jms.config.JmsChannelFactoryBean;
import org.springframework.integration.kafka.outbound.KafkaProducerMessageHandler;
import org.springframework.jms.support.converter.MessagingMessageConverter;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.MessageHandler;

import com.github.ricardocomar.springbootetl.etlconsumer.model.ConsumerModel;
import com.github.ricardocomar.springbootetl.etlconsumer.transformer.AvroTransformer;
import com.github.ricardocomar.springbootetl.etlconsumer.transformer.TrancodeTransformer;
import com.github.ricardocomar.springbootetl.etlconsumer.validation.ModelValidator;

@Profile("sp-int")
@Configuration
@EnableIntegration
public class SpringIntegrationConfig {

	@Autowired
	private AvroTransformer avroTransformer;

	@Autowired
	private TrancodeTransformer trancodeTransformer;

	@Autowired
	private ModelValidator modelValidator;

	@Bean(destroyMethod = "destroy")
	public JmsChannelFactoryBean jmsInboundChannel(final ConnectionFactory jmsConnFactory) {
		final JmsChannelFactoryBean factory = new JmsChannelFactoryBean(true);
		factory.setMessageConverter(new MessagingMessageConverter());
		factory.setConnectionFactory(jmsConnFactory);
		factory.setSessionTransacted(true);
		factory.setDestinationName("queue.sample");
		return factory;
	}

	@Transformer(inputChannel = "jmsInboundChannel", outputChannel = "avroTransformerChannel")
	public ConsumerModel fromPayloadToTeam(final String payload) {
		final ConsumerModel model = trancodeTransformer.fromTrancode(payload);
		modelValidator.validate(model);
		return model;
	}

	@Transformer(inputChannel = "avroTransformerChannel", outputChannel = "kafkaOutboundChannel")
	public SpecificRecord fromTeamToTeamAvro(final ConsumerModel team) {
		return avroTransformer.from(team);
	}

	@Bean
	@ServiceActivator(inputChannel = "kafkaOutboundChannel")
	public MessageHandler kafkaMessageHandler(final KafkaTemplate<String, SpecificRecord> kafkaTemplate) {
		final KafkaProducerMessageHandler<String, SpecificRecord> handler = new KafkaProducerMessageHandler<>(
				kafkaTemplate);
		handler.setTopicExpression(new LiteralExpression("topicOutbound"));
		return handler;
	}

}
