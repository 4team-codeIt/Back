package com.brick.demo.common.converter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToLongListConverter implements Converter<String, List<Long>> {

  public List<Long> convert(String source) {
    if (source == null || source.isEmpty()) {
      return List.of();
    }
    return Arrays.stream(source.split(","))
        .map(String::trim)
        .map(Long::parseLong)
        .collect(Collectors.toList());
  }
}
