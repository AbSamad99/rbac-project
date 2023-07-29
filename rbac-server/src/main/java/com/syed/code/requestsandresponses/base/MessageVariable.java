package com.syed.code.requestsandresponses.base;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MessageVariable {
    private String applicationCode;

    private Integer entityId;

    private String entity;

    private String entityName;

    private List<String> errorMessages = new ArrayList<>();

    private Long errorCount = 0l;

    private List<String> successMessages = new ArrayList<>();

    private Long successCount = 0l;
}
