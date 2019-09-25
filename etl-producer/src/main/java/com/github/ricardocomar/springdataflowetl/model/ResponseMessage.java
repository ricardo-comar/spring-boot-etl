package com.github.ricardocomar.springdataflowetl.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseMessage {
	private String teamName;
	private List<Employee> employees;

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Employee {
		private String firstName;
		private String lastName;
		private String title;
		private BigDecimal salary;
		private LocalDate hireDate;

	}
}
