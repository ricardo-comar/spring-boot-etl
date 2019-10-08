package com.github.ricardocomar.springbootetl.etlconsumer.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.beanio.annotation.Field;
import org.beanio.annotation.Record;
import org.beanio.annotation.Segment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Record
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Purchase implements ConsumerMainModel {

	@Field(ordinal = 0, length = 10)
	private String transaction;

	@Field(ordinal = 0, length = 12)
	private String id;

	@Field(ordinal = 0, length = 30)
	private String customer;

	@Field(ordinal = 1, length = 10, format = "10,2")
	private BigDecimal totalValue;

	@Field(ordinal = 2, length = 10)
	private PurchaseStatus status;

	@Field(ordinal = 3, format = "ddMMyyyy", length = 8)
	private LocalDate date;

	@Segment(ordinal = 4, collection = ArrayList.class, minOccurs = 0, maxOccurs = 4, until = 200)
	private List<PurchaseItem> items;

	public enum PurchaseStatus {
		OPEN, PAID, CANCELLED, RETURNED, DELIVERED;
	}


}