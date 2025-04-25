package com.example.fileManager.repository;

import com.example.fileManager.model.EmailRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface EmailRecordRepository extends MongoRepository<EmailRecord, String> {
    List<EmailRecord> findByToAddress(String toAddress);
    List<EmailRecord> findAllByOrderBySentDateDesc();
}