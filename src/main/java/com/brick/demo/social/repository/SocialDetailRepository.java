package com.brick.demo.social.repository;

import com.brick.demo.social.entity.SocialDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SocialDetailRepository extends JpaRepository<SocialDetail, Long> {}
