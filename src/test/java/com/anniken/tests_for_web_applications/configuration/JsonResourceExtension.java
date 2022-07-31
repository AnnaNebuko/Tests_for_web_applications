package com.anniken.tests_for_web_applications.configuration;

import static org.apache.logging.log4j.util.Strings.isNotEmpty;

import com.anniken.tests_for_web_applications.utils.FileTestUtils;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;
import org.springframework.util.ReflectionUtils;

@Slf4j
public class JsonResourceExtension implements TestInstancePostProcessor {

  private Map<String, Object> cache = new ConcurrentHashMap<>();
  private String folder;

  @Override
  public void postProcessTestInstance(Object testInstance, ExtensionContext context)
      throws Exception {
    if (Objects.nonNull(testInstance)) {
      folder = getFolderName(testInstance);

      ReflectionUtils.doWithFields(testInstance.getClass(),
          field -> initResource(testInstance, field),
          field -> field.isAnnotationPresent(JsonResource.class));
    }

  }

  private String getFolderName(Object testInstance) throws IOException {

    Optional<JsonResourceFolder> annotation = getJsonResourceFolderAnnotation(testInstance);

    if (annotation.isPresent()) {
      String folderFromAnnotation = annotation.get().folderName();
      if (!folderFromAnnotation.equals("")) {
        return folderFromAnnotation;
      }
      throw new IOException("FolderName in JsonResourceFolder annotation is required");
    }
    throw new IOException("JsonResourceFolder annotation is required");
  }

  private Optional<JsonResourceFolder> getJsonResourceFolderAnnotation(Object testInstance) {
    return Arrays.stream(testInstance.getClass().getAnnotations())
        .filter(annotation -> annotation instanceof JsonResourceFolder)
        .findFirst()
        .map(annotation -> (JsonResourceFolder) annotation);
  }

  private void initResource(Object test, Field resourceField) {
    String resourcePath = getResourcePath(resourceField);

    Object value = readData(resourceField, resourcePath);

    resourceField.setAccessible(true);
    ReflectionUtils.setField(resourceField, test, value);
  }

  private Object readData(Field resourceField, String resourcePath) {
    String genericType = resourceField.getGenericType().toString();

    if (genericType.contains("<")) {
      JavaType javaType = TypeFactory.defaultInstance().constructFromCanonical(genericType);
      return cache.compute(resourcePath, (path, value) -> FileTestUtils.readJson(path, javaType));
    }

    return cache.compute(resourcePath,
        (path, value) -> FileTestUtils.readJson(path, resourceField.getType()));
  }

  private String getResourcePath(Field resourceField) {
    JsonResource annotation = resourceField.getAnnotation(JsonResource.class);
    String resourceName = isNotEmpty(annotation.fileName()) ? annotation.fileName()
        : toDashedCase(resourceField.getName());

    return folder == null ? resourceName : folder + "/" + resourceName;
  }

  private String toDashedCase(String camelCase) {
    Matcher m = Pattern.compile("(?<=[a-z0-9])[A-Z]").matcher(camelCase);

    StringBuffer sb = new StringBuffer();
    while (m.find()) {
      m.appendReplacement(sb, "-" + m.group().toLowerCase());
    }
    m.appendTail(sb);

    return sb.toString() + ".json";
  }

}
