package com.github.ricardocomar.springbootetl.etlconsumer.config;

import java.io.IOException;
import java.util.Map;

import com.github.ricardocomar.springbootetl.model.TeamAvro;

import io.confluent.kafka.schemaregistry.client.MockSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.rest.exceptions.RestClientException;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;

public class CustomKafkaAvroDeserializer extends KafkaAvroDeserializer {

	@Override
	public void configure(final Map<String, ?> configs, final boolean isKey) {
		super.configure(configs, isKey);
		this.schemaRegistry = new MockSchemaRegistryClient();
		try {
			this.schemaRegistry.register("team", TeamAvro.SCHEMA$);
		} catch (IOException | RestClientException e) {
			e.printStackTrace();
		}
	}

}
