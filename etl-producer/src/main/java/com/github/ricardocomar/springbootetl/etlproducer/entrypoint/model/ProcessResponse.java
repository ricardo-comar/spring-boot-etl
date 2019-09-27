package com.github.ricardocomar.springbootetl.etlproducer.entrypoint.model;

import java.math.BigDecimal;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProcessResponse {

	private String id;
	private Team response;
	private Long duration;

	@Data
	@Builder
	public static class Team {

		private String teamName;
		private List<Employee> employees;

		@Data
		@Builder
		public static class Employee {

			private String firstName;
			private String lastName;
			private String title;
			private BigDecimal salary;
			private String hireDate;
			private EmployeeStatus status;

			public enum EmployeeStatus {
				ACTIVE, DISMISSED, RESIGNED, RETIRED;
			}

		}
	}
}
