package com.brick.demo.social.entity;

import com.brick.demo.common.entity.BaseEntity;
import com.brick.demo.social.dto.SocialCreateRequest;
import com.brick.demo.social.dto.SocialUpdateRequest;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "social_detail")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SocialDetail extends BaseEntity {

  @Id
  @Column(name = "id")
  private Long id;

  @MapsId
  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "social_id")
  private Social social;

  @Column(name = "description", nullable = false, length = 3000)
  private String description;

  @Column(name = "geo_location")
  private String geoLocation;

  public SocialDetail(final Social social, final String description, final String geoLocation) {
    this.social = social;
    this.description = description;
    this.geoLocation = geoLocation;
  }

  public SocialDetail(final Social social, final SocialCreateRequest dto) {
    this.social = social;
    this.description = dto.description();
    this.geoLocation = dto.place().latitude() + " " + dto.place().longitude();
  }

  public void update(final SocialUpdateRequest dto) {
    this.description = dto.description();
    this.geoLocation = dto.place().latitude() + " " + dto.place().longitude();
  }
}
