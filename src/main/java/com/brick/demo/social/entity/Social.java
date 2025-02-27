package com.brick.demo.social.entity;

import com.brick.demo.auth.entity.Account;
import com.brick.demo.common.entity.BaseEntity;
import com.brick.demo.social.dto.common.Place;
import com.brick.demo.social.dto.request.SocialCreateRequest;
import com.brick.demo.social.dto.request.SocialUpdateRequest;
import com.brick.demo.social.enums.Delimiter;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "social")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Social extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "name", nullable = false, length = 20)
  private String name;

  @Column(name = "gathering_date", nullable = false)
  private LocalDateTime gatheringDate;

  @Column(name = "address")
  @Setter
  private String address;

  @Column(name = "image_urls", length = 700)
  @Setter
  private String imageUrls;

  @Column(name = "tags")
  private String tags;

  @Column(name = "min_count", nullable = false)
  private Integer minCount;

  @Column(name = "max_count", nullable = false)
  private Integer maxCount;

  @Column(name = "dues", nullable = false)
  private Integer dues = 0;

  @Column(name = "canceled")
  @Setter
  private Boolean canceled;

  @OneToOne(
      mappedBy = "social",
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY,
      optional = false)
  @Setter
  private SocialDetail detail;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "account_id", nullable = false)
  private Account owner;

  @OneToMany(mappedBy = "social", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<SocialParticipant> participants = new HashSet<>();

  public Social(final Account account, final SocialCreateRequest dto) {
    String imageUrls =
        dto.imageUrls() == null ? null : String.join(Delimiter.IMAGE_URLS.value(), dto.imageUrls());
    String tags = dto.tags() == null ? null : String.join(Delimiter.TAGS.value(), dto.tags());

    this.name = dto.name();
    this.gatheringDate = dto.gatheringDate();
    this.address = addressFrom(dto.place());
    this.imageUrls = imageUrls;
    this.tags = tags;
    this.minCount = dto.participantCount().min();
    this.maxCount = dto.participantCount().max();
    this.dues = dto.dues();
    this.owner = account;
  }

  public void update(final SocialUpdateRequest dto) {
    this.name = dto.name();
    this.address = addressFrom(dto.place());
    this.imageUrls = String.join(Delimiter.IMAGE_URLS.value(), dto.imageUrls());
  }

  public void cancel() {
    this.canceled = true;
  }

  private String addressFrom(final Place place) {
    return place.address() + Delimiter.ADDRESS.value() + place.detailAddress();
  }
}
