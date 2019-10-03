package com.github.ricardocomar.springbootetl.etlconsumer.config;

import java.util.Map;

import io.confluent.kafka.schemaregistry.client.MockSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.serializers.KafkaAvroSerializer;

public class CustomKafkaAvroSerializer extends KafkaAvroSerializer {
	public CustomKafkaAvroSerializer() {
		super();
		super.schemaRegistry = new MockSchemaRegistryClient();
	}

	public CustomKafkaAvroSerializer(final SchemaRegistryClient client) {
        super(new MockSchemaRegistryClient());
    }

	public CustomKafkaAvroSerializer(final SchemaRegistryClient client, final Map<String, ?> props) {
        super(new MockSchemaRegistryClient(), props);
    }
}