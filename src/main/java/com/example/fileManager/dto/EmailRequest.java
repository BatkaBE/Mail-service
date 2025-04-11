package com.example.fileManager.dto;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class EmailRequest {

    private Long id;

    private String to;

    private String subject;

    private String message;
}