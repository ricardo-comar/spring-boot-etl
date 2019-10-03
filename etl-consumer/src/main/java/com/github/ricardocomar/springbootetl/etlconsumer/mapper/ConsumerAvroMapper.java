package com.github.ricardocomar.springbootetl.etlconsumer.mapper;

import org.apache.avro.specific.SpecificRecord;

import com.github.ricardocomar.springbootetl.etlconsumer.model.ConsumerModel;

public interface ConsumerAvroMapper<A extends SpecificRecord> {

	ConsumerModel fromAvro(A avro);
}
