package com.anniken.tests_for_web_applications.service;

import com.anniken.tests_for_web_applications.model.AssessmentDto;
import org.springframework.stereotype.Service;

@Service
public class AssessmentService {

  public Boolean checkResults(AssessmentDto assessmentDto) {
    return Boolean.TRUE;
  }
}
