package com.anniken.tests_for_web_applications.endpoint;

import com.anniken.tests_for_web_applications.model.AssessmentDto;
import com.anniken.tests_for_web_applications.service.AssessmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/assessment")
public class AssessmentEndpoint {

  private final AssessmentService assessmentService;

  @PostMapping("/pass")
  Boolean checkResults(@RequestBody @Validated AssessmentDto assessmentDto) {
    return assessmentService.checkResults(assessmentDto);
  }

}
