package com.github.ricardocomar.springdataflowetl.etlconsumer.model.avro;

import java.util.List;

import com.github.ricardocomar.springdataflowetl.model.ResponseMessage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamAvro implements ResponseMessage {
	
	private String teamName;
	private List<EmployeeAvro> employees;
}