package com.anniken.tests_for_web_applications.it;

import com.anniken.tests_for_web_applications.TestsForWebApplicationsApplication;
import com.anniken.tests_for_web_applications.configuration.JsonResourceExtension;
import com.anniken.tests_for_web_applications.configuration.JsonResourceFolder;
import com.anniken.tests_for_web_applications.service.AssessmentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@JsonResourceFolder(folderName = "endpoint/assessmentEndpoint")
@ExtendWith({JsonResourceExtension.class, SpringExtension.class})
@SpringBootTest(
    classes = TestsForWebApplicationsApplication.class
)
public class AssessmentEndpointIntegrationTest {

  @Autowired
  private AssessmentService assessmentService;

  @LocalServerPort
  private int port;

  private String endpoint_url;

  private final HttpHeaders requestHeaders = new HttpHeaders();

  private static final String BASE_URL = "http://localhost";

  @BeforeEach
  void setUp() {
    endpoint_url = BASE_URL + ":" + port + "/api/v1/exams";
    requestHeaders.add("Accept", MediaType.APPLICATION_JSON_VALUE);
  }

  @AfterEach
  void cleanDb() {
    //assessmentRepository.delete();
  }

  private void fillDbWithAssessments() {
    //create assessment
  }

}
