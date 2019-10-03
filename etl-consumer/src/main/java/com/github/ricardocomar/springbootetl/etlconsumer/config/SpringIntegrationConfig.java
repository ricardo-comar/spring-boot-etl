package com.github.ricardocomar.springbootetl.etlconsumer.config;

import javax.jms.ConnectionFactory;

import org.apache.avro.generic.GenericRecord;
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

import com.github.ricardocomar.springbootetl.etlconsumer.model.Team;
import com.github.ricardocomar.springbootetl.etlconsumer.transformer.TeamAvroTransformer;
import com.github.ricardocomar.springbootetl.etlconsumer.transformer.TeamTrancodeTransformer;
import com.github.ricardocomar.springbootetl.etlconsumer.validation.ValidatorTeam;
import com.github.ricardocomar.springbootetl.model.TeamAvro;

@Profile("sp-int")
@Configuration
@EnableIntegration
public class SpringIntegrationConfig {

	@Autowired
	private TeamAvroTransformer avroTransformer;

	@Autowired
	private TeamTrancodeTransformer trancodeTransformer;

	@Autowired
	private ValidatorTeam validatorTeam;

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
	public Team fromPayloadToTeam(final String payload) {
		final Team team = trancodeTransformer.from(payload);
		validatorTeam.validate(team);
		return team;
	}

	@Transformer(inputChannel = "avroTransformerChannel", outputChannel = "kafkaOutboundChannel")
	public TeamAvro fromTeamToTeamAvro(final Team team) {
		return avroTransformer.from(team);
	}

	@Bean
	@ServiceActivator(inputChannel = "kafkaOutboundChannel")
	public MessageHandler kafkaMessageHandler(final KafkaTemplate<String, GenericRecord> kafkaTemplate) {
		final KafkaProducerMessageHandler<String, GenericRecord> handler = new KafkaProducerMessageHandler<>(
				kafkaTemplate);
		handler.setTopicExpression(new LiteralExpression("topicOutbound"));
		return handler;
	}

}
