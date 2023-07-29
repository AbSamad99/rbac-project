package com.syed.code.services.grid.sanitization;

import com.syed.code.requestsandresponses.grid.SortCriteria;

import java.util.List;

public interface BaseGridSanitizationService<T> {

    public void sanitizeData(List<T> dataList, List<SortCriteria> criteriaList);

    public void sanitizeDataInternal(List<T> dataList);
}
