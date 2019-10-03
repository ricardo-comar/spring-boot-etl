package com.github.ricardocomar.springbootetl.etlconsumer.config;

import org.apache.avro.Schema;

import com.github.ricardocomar.springbootetl.model.TeamAvro;

import io.confluent.kafka.schemaregistry.client.MockSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;

public class CustomKafkaAvroDeserializer extends KafkaAvroDeserializer {
	@Override
	public Object deserialize(final String topic, final byte[] bytes) {
		if (topic.equals("topicOutbound")) {
			this.schemaRegistry = getMockClient(TeamAvro.SCHEMA$);
		}
		return super.deserialize(topic, bytes);
	}

	private static SchemaRegistryClient getMockClient(final Schema schema$) {
		return new MockSchemaRegistryClient() {
			@Override
			public synchronized Schema getById(final int id) {
				return schema$;
			}
		};
	}
}
