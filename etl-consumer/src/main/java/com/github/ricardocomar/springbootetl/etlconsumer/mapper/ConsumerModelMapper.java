package com.github.ricardocomar.springbootetl.etlconsumer.mapper;

import org.apache.avro.generic.GenericRecord;

import com.github.ricardocomar.springbootetl.etlconsumer.model.ConsumerModel;

public interface ConsumerModelMapper<M extends ConsumerModel> {

	GenericRecord fromModel(M model);

}
