package com.syed.code.entities.email;

import lombok.Data;

import java.util.Map;

@Data
public class Email {

    private String templateName;

    private String to;

    private String from;

    private String subject;

    private Map<String, Object> contentMap;
}
