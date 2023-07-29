package com.syed.code.requestsandresponses.grid;

import lombok.Data;

import java.util.List;

@Data
public class GridRequest {

    Integer entityId;

    Integer pageNumber;

    Integer itemNumber;

    List<SortCriteria> sortCriteria;

    List<FilterCriteria> filterCriteria;
}
