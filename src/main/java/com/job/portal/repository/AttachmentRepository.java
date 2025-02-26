package com.job.portal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.job.portal.entity.Attachment;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

}
