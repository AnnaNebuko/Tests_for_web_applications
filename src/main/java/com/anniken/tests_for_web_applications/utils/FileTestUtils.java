package com.anniken.tests_for_web_applications.utils;

import static com.anniken.tests_for_web_applications.utils.FileTestUtils.MappersHolder.mapper;
import static com.anniken.tests_for_web_applications.utils.FileTestUtils.MappersHolder.yamlMapper;
import static java.nio.file.Files.readAllBytes;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class FileTestUtils {

  private static final String FAILED_READING_MSG = "Failed reading '%s'!";

  public static class MappersHolder {

    public static final ObjectMapper mapper = new ObjectMapper()
        .registerModule(new JavaTimeModule());
    public static final ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());

    private MappersHolder() {
    }

    static {
      mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
      mapper.disable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS);
      yamlMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
      yamlMapper.disable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS);
    }
  }

  private FileTestUtils() {
  }

  /**
   * References a file in classpath, particularly in resources folder
   *
   * @param path relative resource path without leading '/'
   */
  public static File file(String path) {
    URL systemResource = ClassLoader.getSystemResource(path);
    if (systemResource == null) {
      throw new IllegalStateException(String.format("Resource '%s' not found!", path));
    }
    try {
      return new File(systemResource.toURI());
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Reads object from json file in classpath
   *
   * @param filePath relative resource path without leading '/'
   */
  public static <T> T readJson(String filePath, Class<T> clazz) {
    try {
      return mapper.readValue(file(filePath), clazz);
    } catch (IOException e) {
      throw new IllegalStateException(String.format(FAILED_READING_MSG, filePath), e);
    }
  }

  /**
   * Reads object from json file in classpath
   *
   * @param filePath relative resource path without leading '/'
   */
  public static <T> T readJson(String filePath, JavaType type) {
    try {
      return mapper.readValue(file(filePath), type);
    } catch (IOException e) {
      throw new IllegalStateException(String.format(FAILED_READING_MSG, filePath), e);
    }
  }

  /**
   * Reads object from json file in classpath into string
   *
   * @param filePath relative resource path without leading '/'
   */
  public static String readJson(String filePath) {
    try {
      return new String(readAllBytes(file(filePath).toPath()));
    } catch (IOException e) {
      throw new IllegalStateException(String.format(FAILED_READING_MSG, filePath), e);
    }
  }

  /**
   * Reads properties map from yaml file in classpath
   *
   * @param filePath relative resource path without leading '/'
   */
  public static Map readYaml(String filePath) {
    try {
      return yamlMapper.readValue(file(filePath), Map.class);
    } catch (IOException e) {
      throw new IllegalStateException(String.format(FAILED_READING_MSG, filePath), e);
    }
  }

  /**
   * Reads properties file in classpath
   *
   * @param filePath relative resource path without leading '/'
   */
  public static Properties readProperties(String filePath) {
    try {
      return PropertiesLoaderUtils.loadAllProperties(filePath);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

}
