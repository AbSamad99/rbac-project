package com.syed.code.services.grid.sanitization.nonversioning;

import com.syed.code.entities.baseentity.NonVersioningEntity;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class GenericNonVersioningGridSanitizationServiceImpl<T extends NonVersioningEntity> extends BaseNonVersioningGridSanitizationServiceImpl<T> {

    @Override
    public void sanitizeDataInternal(List<T> dataList) {

    }
}
