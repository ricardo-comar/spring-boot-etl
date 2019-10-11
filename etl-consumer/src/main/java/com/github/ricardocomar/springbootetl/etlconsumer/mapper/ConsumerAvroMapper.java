package com.github.ricardocomar.springbootetl.etlconsumer.mapper;

import org.apache.avro.generic.GenericRecord;

import com.github.ricardocomar.springbootetl.etlconsumer.model.ConsumerModel;

public interface ConsumerAvroMapper<A extends GenericRecord> {

	ConsumerModel fromAvro(A avro);
}
