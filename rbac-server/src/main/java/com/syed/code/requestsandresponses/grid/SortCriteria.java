package com.syed.code.requestsandresponses.grid;

import com.syed.code.entities.filterandSort.GridColumnConfig;
import lombok.Data;

@Data
public class SortCriteria {

    private Long gridColumnConfigId;

    private GridColumnConfig gridColumnConfig;

    private Integer sortType;
}
