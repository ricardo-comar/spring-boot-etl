package com.github.ricardocomar.springdataflowetl.etlconsumer.model.trancode;

import java.util.Date;

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
public class EmployeeTrancode {
	
	@Field(ordinal = 0, length = 12)
	private String firstName;
	@Field(ordinal = 1, length = 10)
	private String lastName;
	@Field(ordinal = 2, length = 10)
	private String title;
	@Field(ordinal = 3, length = 10)
	private String salary;
	@Field(ordinal = 4, format = "ddMMyyyy", length = 8)
	private Date hireDate;
}