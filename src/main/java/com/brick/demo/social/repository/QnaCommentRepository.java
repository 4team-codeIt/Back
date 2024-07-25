package com.brick.demo.social.repository;

import com.brick.demo.social.entity.Qna;
import com.brick.demo.social.entity.QnaComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QnaCommentRepository extends JpaRepository<QnaComment, Long> {


}
