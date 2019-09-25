package com.github.ricardocomar.springdataflowetl.etlconsumer.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.github.ricardocomar.springdataflowetl.etlconsumer.model.avro.EmployeeAvro;
import com.github.ricardocomar.springdataflowetl.etlconsumer.model.trancode.EmployeeTrancode;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

	EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);

	@Mappings({
			@Mapping(target = "salary", expression = "java(new java.math.BigDecimal(team.getSalary()).divide(new java.math.BigDecimal(100), 2, java.math.RoundingMode.HALF_EVEN))") })
	EmployeeAvro trancodeToAvro(EmployeeTrancode team);

	@InheritInverseConfiguration
	@Mappings({
			@Mapping(target = "salary", expression = "java(String.format(\"%01.0f\", team.getSalary().multiply(new java.math.BigDecimal(100))))") })
	EmployeeTrancode avroToTrancode(EmployeeAvro team);
}
