package com.github.ricardocomar.springbootetl.etlconsumer.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.github.ricardocomar.springbootetl.etlconsumer.model.Employee;
import com.github.ricardocomar.springbootetl.model.EmployeeAvro;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

	EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);

	@Mappings({
			@Mapping(target = "hireDate", expression = "java(team.getHireDate().format(java.time.format.DateTimeFormatter.ofPattern(\"yyyy-MM-dd\")))")
		})
	EmployeeAvro trancodeToAvro(Employee team);

	@InheritInverseConfiguration
	@Mappings({
			@Mapping(target = "hireDate", expression = "java(java.time.LocalDate.parse(team.getHireDate(), java.time.format.DateTimeFormatter.ofPattern(\"yyyy-MM-dd\")))") })
	Employee avroToTrancode(EmployeeAvro team);
}
