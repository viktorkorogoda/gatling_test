package computerdatabase;

import static io.gatling.javaapi.core.CoreDsl.StringBody;
import static io.gatling.javaapi.core.CoreDsl.constantUsersPerSec;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import java.time.Duration;

public class PostRequestSimulation extends Simulation {

    // Define the HTTP protocol configuration
    private final HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:8088") // Replace with your service URL
            .acceptHeader("application/json")
            .contentTypeHeader("application/json")
            .userAgentHeader("Gatling/3.9.0");

    // Define the JSON body for the POST request
    private final String requestBody = "{\n"
            + "  \"id\": 1,\n"
            + "  \"time\" : 100,\n"
            + "  \"name\" : \"one\"\n"
            + "}"; // Replace with your JSON body

    // Define the scenario
    private final ScenarioBuilder scn = scenario("PostRequestSimulation")
            .exec(
                    http("Send POST Request")
                            .post("/accept") // Replace with your endpoint
                            .body(StringBody(requestBody)) // Attach the JSON body
                            .check(status().is(200)) // Validate the response status
            )
            .pause(Duration.ofMillis(20), Duration.ofMillis(100)); // 100ms pause between requests

    {
        setUp(
                scn.injectOpen(
                        constantUsersPerSec(1000.0/60).during(10) // 1000 requests per minute
                )
        ).protocols(httpProtocol);
    }
}