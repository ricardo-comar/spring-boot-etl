package com.github.ricardocomar.springbootetl.etlconsumer.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.github.ricardocomar.springbootetl.etlconsumer.consumer.model.EmployeeTrancode;
import com.github.ricardocomar.springbootetl.model.EmployeeAvro;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

	EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);

	@Mappings({
			@Mapping(target = "salary", expression = "java(new java.math.BigDecimal(team.getSalary()).divide(new java.math.BigDecimal(100), 2, java.math.RoundingMode.HALF_EVEN))"),
			@Mapping(target = "hireDate", expression = "java(com.github.ricardocomar.springbootetl.etlconsumer.transformer.LocalDateTypeHandler.builder().format(\"yyyy-MM-dd\").build().format(team.getHireDate()))") 
		})
	EmployeeAvro trancodeToAvro(EmployeeTrancode team);

	@InheritInverseConfiguration
	@Mappings({
			@Mapping(target = "salary", expression = "java(String.format(\"%01.0f\", team.getSalary().multiply(new java.math.BigDecimal(100))))"),
			@Mapping(target = "hireDate", expression = "java((java.time.LocalDate) com.github.ricardocomar.springbootetl.etlconsumer.transformer.LocalDateTypeHandler.builder().format(\"yyyy-MM-dd\").build().parse(team.getHireDate()))") })
	EmployeeTrancode avroToTrancode(EmployeeAvro team);
}
