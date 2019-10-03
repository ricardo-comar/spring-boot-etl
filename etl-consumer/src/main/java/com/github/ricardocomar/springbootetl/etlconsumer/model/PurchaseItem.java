package com.github.ricardocomar.springbootetl.etlconsumer.model;

import java.math.BigDecimal;

import org.beanio.annotation.Field;
import org.beanio.annotation.Record;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Record
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseItem implements ConsumerModel {
	
	@Field(ordinal = 0, length = 12)
	private String id;
	@Field(ordinal = 1, length = 10)
	private String sku;
	@Field(ordinal = 2, length = 18)
	private String title;
	@Field(ordinal = 3, length = 10, format = "10,2")
	private BigDecimal value;

}