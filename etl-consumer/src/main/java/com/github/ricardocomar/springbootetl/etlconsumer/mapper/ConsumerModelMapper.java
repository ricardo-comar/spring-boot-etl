package com.github.ricardocomar.springbootetl.etlconsumer.mapper;

import org.apache.avro.specific.SpecificRecord;

import com.github.ricardocomar.springbootetl.etlconsumer.model.ConsumerModel;

public interface ConsumerModelMapper<M extends ConsumerModel> {

	SpecificRecord fromModel(M model);

}
