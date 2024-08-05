package com.brick.demo.social.dto.request;

import java.util.List;

public record SocialQuery(
   int offset,
   int limit,
   String filterBy,
   String orderBy,
   String name,
   List<String> tags,
   List<Long> ids
) {

  public static SocialQuery forMe(int offset, int limit, String filterBy, String orderBy) {
    return new SocialQuery(offset, limit, filterBy, orderBy, null, null, null);
  }
}
