package com.example.fileManager.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;
import java.util.List;

@Document(collection = "email_records")
@Data
public class EmailRecord {
    @Id
    private String id;
    private String toAddress;
    private String subject;
    private Date sentDate;
    private List<String> attachments;
    private boolean success;
    private String errorMessage;
}