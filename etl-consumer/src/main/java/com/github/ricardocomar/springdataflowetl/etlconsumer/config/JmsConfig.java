package com.github.ricardocomar.springdataflowetl.etlconsumer.config;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.SimpleMessageConverter;

@EnableJms
@Configuration
public class JmsConfig {

	@Value("${spring.activemq.broker-url}")
	private String brokerUrl;

	@Value("${spring.activemq.user}")
	private String user;

	@Value("${spring.activemq.password}")
	private String password;

	@Bean
	public ActiveMQConnectionFactory connectionFactory() {
		if ("".equals(user)) {
			return new ActiveMQConnectionFactory(brokerUrl);
		}
		return new ActiveMQConnectionFactory(user, password, brokerUrl);
	}

	@Bean
	public JmsListenerContainerFactory jmsFactoryTopic(@Autowired final ConnectionFactory connectionFactory,
			@Autowired final DefaultJmsListenerContainerFactoryConfigurer configurer) {
		final DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		configurer.configure(factory, connectionFactory);
		factory.setPubSubDomain(true);
		return factory;
	}

	@Bean
	public JmsTemplate jmsTemplate(@Autowired final ConnectionFactory connectionFactory,
			@Autowired final MessageConverter messageConverter) {
		final JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
		jmsTemplate.setMessageConverter(messageConverter);
		return jmsTemplate;
	}

	@Bean
	public JmsTemplate jmsTemplateTopic(@Autowired final ConnectionFactory connectionFactory,
			@Autowired final MessageConverter messageConverter) {
		final JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
		jmsTemplate.setMessageConverter(messageConverter);
		jmsTemplate.setPubSubDomain(true);
		return jmsTemplate;
	}

	@Bean
	public MessageConverter messageConverter() {
		return new SimpleMessageConverter() {

			@Override
			public Message toMessage(final Object object, final Session session)
					throws JMSException, MessageConversionException {
				final Message message = super.toMessage(object, session);

				if (message.propertyExists(AppProperties.HEADER_CORRELATION_ID)) {
					MDC.put(AppProperties.PROP_CORRELATION_ID,
							message.getStringProperty(AppProperties.HEADER_CORRELATION_ID));
				}

				return message;
			}

		};
	}
}
