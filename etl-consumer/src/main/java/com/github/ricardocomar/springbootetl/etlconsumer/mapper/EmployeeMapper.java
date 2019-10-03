package com.github.ricardocomar.springbootetl.etlconsumer.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.github.ricardocomar.springbootetl.etlconsumer.model.Employee;
import com.github.ricardocomar.springbootetl.model.EmployeeAvro;

@Mapper(componentModel = "spring")
public abstract class EmployeeMapper implements ConsumerAvroMapper<EmployeeAvro>, ConsumerModelMapper<Employee> {

	@Override
	@Mappings({
			@Mapping(target = "hireDate", expression = "java(model.getHireDate().format(java.time.format.DateTimeFormatter.ofPattern(\"yyyy-MM-dd\")))")
		})
	public abstract EmployeeAvro fromModel(Employee model);

	@Override
	@InheritInverseConfiguration
	@Mappings({
			@Mapping(target = "hireDate", expression = "java(java.time.LocalDate.parse(avro.getHireDate(), java.time.format.DateTimeFormatter.ofPattern(\"yyyy-MM-dd\")))") })
	public abstract Employee fromAvro(EmployeeAvro avro);
}
