package com.github.ricardocomar.springbootetl.etlconsumer.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.github.ricardocomar.springbootetl.etlconsumer.consumer.model.TeamTrancode;
import com.github.ricardocomar.springbootetl.model.TeamAvro;

@Mapper(componentModel = "spring", uses = { EmployeeMapper.class })
public interface TeamMapper {

	TeamMapper INSTANCE = Mappers.getMapper(TeamMapper.class);

	TeamAvro trancodeToAvro(TeamTrancode team);

	@InheritInverseConfiguration
	TeamTrancode avroToTrancode(TeamAvro team);
}
