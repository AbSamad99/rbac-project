package com.syed.code.requestsandresponses.grid;

import com.syed.code.entities.filterandSort.FilterConfig;
import com.syed.code.entities.filterandSort.GridColumnConfig;
import com.syed.code.requestsandresponses.base.BaseResponse;
import lombok.Data;

import java.util.List;

@Data
public class GridMetadataResponse extends BaseResponse {

    List<GridColumnConfig> gridColumnConfigs;

    List<FilterConfig> filterConfigs;
}
