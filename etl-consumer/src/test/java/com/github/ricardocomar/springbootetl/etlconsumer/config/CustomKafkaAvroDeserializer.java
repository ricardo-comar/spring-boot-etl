package com.github.ricardocomar.springbootetl.etlconsumer.config;

import java.io.IOException;
import java.util.Map;

import org.apache.avro.Schema;

import com.github.ricardocomar.springbootetl.model.PurchaseAvro;
import com.github.ricardocomar.springbootetl.model.TeamAvro;

import io.confluent.kafka.schemaregistry.client.MockSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.rest.exceptions.RestClientException;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;

public class CustomKafkaAvroDeserializer extends KafkaAvroDeserializer {

	@Override
	public void configure(final Map<String, ?> configs, final boolean isKey) {
		super.configure(configs, isKey);
		this.schemaRegistry = new MockSchemaRegistryClient() {
			@Override
			public synchronized Schema getById(final int id) throws IOException, RestClientException {
				return super.getById(id);
			}
		};
		try {
			this.schemaRegistry.register("team", TeamAvro.SCHEMA$);
			this.schemaRegistry.register("purchase", PurchaseAvro.SCHEMA$);
		} catch (IOException | RestClientException e) {
			e.printStackTrace();
		}
	}

}
