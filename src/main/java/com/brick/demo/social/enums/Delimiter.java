package com.brick.demo.social.enums;

public enum Delimiter {
  ADDRESS(","),
  ADDRESS_REPLACE(" "),

  GEOLOCATION(" "),

  TAGS(","),
  IMAGE_URLS(",");

  private final String delimiter;

  Delimiter(String delimiter) {
    this.delimiter = delimiter;
  }

  public String value() {
    return delimiter;
  }
}
