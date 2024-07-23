package com.brick.demo.social.entity;

import com.brick.demo.auth.entity.Account;
import com.brick.demo.common.entity.BaseEntity;
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
import java.util.Set;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "social")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
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
  private String address;

  @Column(name = "image_urls")
  private String imageUrls;

  @Column(name = "tags")
  private String tags;

  @Column(name = "min_count", nullable = false)
  private Integer minCount;

  @Column(name = "max_count", nullable = false)
  private Integer maxCount;

  @Column(name = "dues", nullable = false)
  private Integer dues = 0;

  @OneToOne(
      mappedBy = "social",
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY,
      optional = false)
  private SocialDetail socialDetail;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "account_id", nullable = false)
  private Account owner;

  @OneToMany(mappedBy = "social", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<SocialParticipant> participants;
}
