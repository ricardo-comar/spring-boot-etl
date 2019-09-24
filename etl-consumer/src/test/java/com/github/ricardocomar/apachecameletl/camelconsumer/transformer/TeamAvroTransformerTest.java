package com.github.ricardocomar.apachecameletl.camelconsumer.transformer;

import java.util.ArrayList;

import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.github.ricardocomar.springdataflowetl.etlconsumer.model.avro.TeamAvro;
import com.github.ricardocomar.springdataflowetl.etlconsumer.model.trancode.EmployeeTrancode;
import com.github.ricardocomar.springdataflowetl.etlconsumer.model.trancode.TeamTrancode;
import com.github.ricardocomar.springdataflowetl.etlconsumer.transformer.TeamAvroTransformer;

@RunWith(CamelSpringBootRunner.class)
public class TeamAvroTransformerTest extends CamelTestSupport{

	@Test
	public void testEmptyTypes() throws Exception {

		getMockEndpoint("mock:result").expectedBodiesReceived(new TeamAvro());

		final TeamTrancode trancodeTeam = TeamTrancode.builder()
			.teamName("Team ABC")
				.employees(new ArrayList<>()).build();
		trancodeTeam.getEmployees().add(EmployeeTrancode.builder()
					.firstName("John")
					.lastName("Snow")
				.salary("1200053")
				.build());
		
		
		template.sendBody("direct:start", trancodeTeam);

		assertMockEndpointsSatisfied();
		assertThat(null, Matchers.anything());
	}

	@Override
	protected RoutesBuilder createRouteBuilder() {
		return new RouteBuilder() {
			@Override
			public void configure() {
				transformer().fromType(TeamAvroTransformer.TYPE_JAVA).toType(TeamAvroTransformer.TYPE_AVRO)
						.withJava(TeamAvroTransformer.class);

				from("direct:start").inputType(TeamAvroTransformer.TYPE_JAVA).to("direct:avro");
				from("direct:avro").inputType(TeamAvroTransformer.TYPE_AVRO).to("mock:result");
			}
		};
	}
}
