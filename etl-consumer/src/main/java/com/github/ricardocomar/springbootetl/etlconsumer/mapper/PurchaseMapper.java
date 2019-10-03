package com.github.ricardocomar.springbootetl.etlconsumer.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.github.ricardocomar.springbootetl.etlconsumer.model.Purchase;
import com.github.ricardocomar.springbootetl.model.PurchaseAvro;

@Mapper(componentModel = "spring", uses = { PurchaseItemMapper.class })
public abstract class PurchaseMapper implements ConsumerAvroMapper<PurchaseAvro>, ConsumerModelMapper<Purchase> {

	@Override
	@Mappings({
			@Mapping(target = "date", expression = "java(model.getDate().format(java.time.format.DateTimeFormatter.ofPattern(\"yyyy-MM-dd\")))") })
	public abstract PurchaseAvro fromModel(Purchase model);

	@InheritInverseConfiguration
	@Mappings({
			@Mapping(target = "date", expression = "java(java.time.LocalDate.parse(avro.getDate(), java.time.format.DateTimeFormatter.ofPattern(\"yyyy-MM-dd\")))") })
	@Override
	public abstract Purchase fromAvro(PurchaseAvro avro);
}
