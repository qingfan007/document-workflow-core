package com.qingfan.documentcoredemo.repository;

import com.qingfan.documentcoredemo.domain.document.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    List<Document> findAllByOrderByCreatedAtDesc();
}
