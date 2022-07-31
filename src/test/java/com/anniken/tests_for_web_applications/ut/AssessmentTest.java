package com.anniken.tests_for_web_applications.ut;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.anniken.tests_for_web_applications.endpoint.AssessmentEndpoint;
import com.anniken.tests_for_web_applications.model.AssessmentDto;
import com.anniken.tests_for_web_applications.service.AssessmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(AssessmentEndpoint.class)
@ContextConfiguration(classes = AssessmentEndpoint.class)
public class AssessmentTest {

  private MockMvc mockMvc;

  @MockBean // For loading ApplicationContext
  private AssessmentService assessmentService;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
        .build();
  }

  @Test
  @DisplayName("shouldReturnTrueWhenCheckResult")
  void checkResultTest() throws Exception {
    // given
    Boolean expected = Boolean.TRUE;

    // when
    mockMvc.perform(post("/api/v2/assessment")
            .contentType(MediaType.APPLICATION_JSON)
            .content(""))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().string(expected.toString()));

    verify(assessmentService, times(1)).checkResults(new AssessmentDto());
    verifyNoMoreInteractions(assessmentService);
  }

}
