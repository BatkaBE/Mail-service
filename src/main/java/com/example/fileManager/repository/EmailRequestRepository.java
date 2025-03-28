package com.example.fileManager.repository;

import com.example.fileManager.dto.EmailRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRequestRepository extends JpaRepository<EmailRequest, Long> {
}