package com.anniken.tests_for_web_applications.model;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssessmentDto {

  @NonNull
  Long id;

  @NonNull
  Map<Integer, List<String>> results;

}
