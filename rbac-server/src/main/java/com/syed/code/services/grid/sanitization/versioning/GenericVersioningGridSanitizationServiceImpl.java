package com.syed.code.services.grid.sanitization.versioning;

import com.syed.code.entities.baseentity.BaseEntity;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class GenericVersioningGridSanitizationServiceImpl<T extends BaseEntity> extends BaseVersioningGridSanitizationServiceImpl<T> {

    @Override
    public void sanitizeDataInternal(List<T> dataList) {

    }
}
