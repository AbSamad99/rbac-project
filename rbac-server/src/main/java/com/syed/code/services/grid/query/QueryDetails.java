package com.syed.code.services.grid.query;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class QueryDetails {

    private String queryString;

    private Map<String, Object> queryParamMap = new HashMap<String, Object>();
}
