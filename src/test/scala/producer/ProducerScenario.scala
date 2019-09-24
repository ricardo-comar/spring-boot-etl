package producer

import io.gatling.core.Predef._  
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._  
import scala.concurrent.duration._  

object ProducerScenario {

  def getProcessScenario (prefixRequestId: String, minWaitMs: Duration,  maxWaitMs: Duration, duration: Duration) : ScenarioBuilder = {
    
    val processScenario = scenario("Producer-Post-Process-Message")
      .during(duration, "count") { 
        exec{session =>session.set("requestId",prefixRequestId + "-" + session("count").as[String])}
        .exec(_.set("delayMin",minWaitMs.toMillis))
        .exec(_.set("delayMax",maxWaitMs.toMillis))
        .exec(ProducerRequest.httpPostProcessService)
        .pause(minWaitMs, maxWaitMs)
      }

    return processScenario;
  }

}
