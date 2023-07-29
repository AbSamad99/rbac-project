package com.syed.code.services.grid.sanitization.nonversioning;

import com.syed.code.entities.baseentity.NonVersioningEntity;
import com.syed.code.requestsandresponses.grid.SortCriteria;
import com.syed.code.services.grid.sanitization.BaseGridSanitizationServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public abstract class BaseNonVersioningGridSanitizationServiceImpl<T extends NonVersioningEntity> extends BaseGridSanitizationServiceImpl<T> {
    @Override
    public void sanitizeData(List<T> dataList, List<SortCriteria> criteriaList) {
        sanitizeDataInternal(dataList);
//        sortList(dataList, criteriaList);
    }
}
