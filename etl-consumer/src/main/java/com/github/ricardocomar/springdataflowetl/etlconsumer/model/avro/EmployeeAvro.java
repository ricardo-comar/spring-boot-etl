package com.github.ricardocomar.springdataflowetl.etlconsumer.model.avro;

import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeAvro {
	
	private String firstName;
	private String lastName;
	private String title;
	private BigDecimal salary;
	private Date hireDate;
}