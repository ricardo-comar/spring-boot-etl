package producer

import io.gatling.core.Predef._  
import io.gatling.http.Predef._  
import io.gatling.jdbc.Predef._  
import scala.concurrent.duration._  
import java.util.Calendar

class ProducerLoadTest extends Simulation {

  val timestamp = Calendar.getInstance.getTime.getTime
  val prefixRequestId = "gatling-" + timestamp

  val minWaitMs = 100 milliseconds
  val maxWaitMs = 300 milliseconds
  val duration  = 5 minutes

  val rampUp1Time = 60 seconds
  val rampUp2Time = 60 seconds
  val stableTime  = 1 minutes
  val maxDuration = (rampUp1Time + stableTime + rampUp2Time + duration) + 2.minutes

  val rampUp1Users = 10
  val rampUp2Users = 30
  val authFactor = 1

  val urlService = "http://localhost:8080/app/producer";
  val httpServiceConf = http
    .header("accept-encoding", "gzip, deflate")
    .header("Content-Type", "application/json;charset=UTF-8")
    .header("Connection", "alive")
    .header("accept-language", "en-US,en;q=0.8")
    .header("User-Agent", "Apache-HttpClient/4.2.6 (java 1.5)")
    .baseUrl(urlService)

  setUp(
        ProducerScenario.getProcessScenario(prefixRequestId, minWaitMs, maxWaitMs, duration).inject(
        rampUsers(rampUp1Users * authFactor) during (rampUp1Time),
        nothingFor(stableTime), 
        rampUsers(rampUp2Users * authFactor) during (rampUp2Time)
      ).protocols(httpServiceConf)
    )
    .maxDuration(maxDuration)
}
