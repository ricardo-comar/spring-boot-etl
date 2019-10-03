package com.github.ricardocomar.springbootetl.etlconsumer.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

import com.github.ricardocomar.springbootetl.etlconsumer.model.Team;
import com.github.ricardocomar.springbootetl.model.TeamAvro;

@Mapper(componentModel = "spring", uses = { EmployeeMapper.class })
public abstract class TeamMapper implements ConsumerAvroMapper<TeamAvro>, ConsumerModelMapper<Team> {

	@Override
	@InheritInverseConfiguration
	public abstract Team fromAvro(TeamAvro avro);

	@Override
	public abstract TeamAvro fromModel(Team model);
}
