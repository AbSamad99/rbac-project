package com.syed.code.services.grid;

import com.syed.code.exceptions.PermissionMissingException;
import com.syed.code.requestsandresponses.grid.GridDataResponse;
import com.syed.code.requestsandresponses.grid.GridMetadataResponse;
import com.syed.code.requestsandresponses.grid.GridRequest;

public interface GridService {

    public GridDataResponse getGridData(GridRequest gridRequest) throws PermissionMissingException;

    public GridMetadataResponse getGridMetadata(Integer id);
}
