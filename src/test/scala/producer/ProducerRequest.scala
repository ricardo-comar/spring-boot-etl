package producer

import io.gatling.core.Predef._  
import io.gatling.http.Predef._  
import io.gatling.jdbc.Predef._  
import scala.concurrent.duration._  

object ProducerRequest {

  var processService = "/process";

  var httpPostProcessService = http("producerProcessService")
    .post(processService)
    .body(ElFileBody("bodies/process_payload.json"))
    .check(
      status.is(200), 
      jsonPath("$.id").is("${requestId}"),
      jsonPath("$.response").exists,
      jsonPath("$.duration").exists
    )

}