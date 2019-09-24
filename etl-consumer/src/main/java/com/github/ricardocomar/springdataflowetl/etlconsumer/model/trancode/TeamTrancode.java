package com.github.ricardocomar.springdataflowetl.etlconsumer.model.trancode;

import java.util.ArrayList;
import java.util.List;

import org.beanio.annotation.Field;
import org.beanio.annotation.Record;
import org.beanio.annotation.Segment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Record(minOccurs = 1)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamTrancode {
	
	@Field(ordinal = 0, length = 10)
	private String teamName;
	
	@Segment(ordinal = 1, collection = ArrayList.class, minOccurs = 0, maxOccurs = 5, until = 250)
	private List<EmployeeTrancode> employees;
	
}