package com.github.ricardocomar.springdataflowetl.etlconsumer.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.github.ricardocomar.springdataflowetl.etlconsumer.model.avro.TeamAvro;
import com.github.ricardocomar.springdataflowetl.etlconsumer.model.trancode.TeamTrancode;

@Mapper(componentModel = "spring", uses = { EmployeeMapper.class })
public interface TeamMapper {

	TeamMapper INSTANCE = Mappers.getMapper(TeamMapper.class);

	TeamAvro trancodeToAvro(TeamTrancode team);

	@InheritInverseConfiguration
	TeamTrancode avroToTrancode(TeamAvro team);
}
