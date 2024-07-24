package com.brick.demo.social.enums;

public enum ParticipantRole {
  OWNER("owner"),
  PARTICIPANT("participant");

  private final String name;

  ParticipantRole(String name) {
    this.name = name;
  }
}
