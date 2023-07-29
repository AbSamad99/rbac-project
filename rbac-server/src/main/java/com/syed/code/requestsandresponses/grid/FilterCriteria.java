package com.syed.code.requestsandresponses.grid;

import com.syed.code.entities.filterandSort.FilterConfig;
import lombok.Data;

@Data
public class FilterCriteria {

    private Long filterConfigId;

    private FilterConfig filterConfig;

    private Object filterValue;
}
