package org.sanmarcux.samples.sakila;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.util.Map;

import static org.assertj.core.api.BDDAssertions.then;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// "management.port" was the Boot 1.x spelling and silently did nothing. The modern key is
// management.server.port; 0 gives the management context its own random port, matching the
// production topology where actuator is bound separately (127.0.0.1:8182).
@TestPropertySource(properties = {"management.server.port=0"})
@AutoConfigureTestRestTemplate
public class SakilaApplicationTests extends AbstractIntegrationTest {

    @LocalServerPort
    private int port;

    @Value("${local.management.port}")
    private int mgt;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void shouldReturn500WhenSendingRequestToErrorController() {
        ResponseEntity<Map> entity = this.testRestTemplate.getForEntity(
                "http://localhost:" + this.port + "/error", Map.class);

        then(entity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void shouldReturn200WhenSendingRequestToManagementEndpoint() {
        ResponseEntity<Map> entity = this.testRestTemplate.getForEntity(
                "http://localhost:" + this.mgt + "/actuator", Map.class);

        then(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
