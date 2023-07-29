package com.syed.code.services.grid.sanitization;

import com.syed.code.entities.baseentity.BaseEntity;
import com.syed.code.entities.baseentity.NonVersioningEntity;
import com.syed.code.enums.EntityEnums;
import com.syed.code.requestsandresponses.grid.SortCriteria;
import com.syed.code.utils.EntityClassMapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public abstract class BaseGridSanitizationServiceImpl<T> implements BaseGridSanitizationService<T> {

    @Autowired
    private EntityClassMapUtil entityClassMapUtil;

    private EntityEnums.Entity ent;

    public EntityEnums.Entity getEnt() {
        return ent;
    }

    public void setEnt(EntityEnums.Entity ent) {
        this.ent = ent;
    }

    protected void sortList(List<T> dataList, List<SortCriteria> criteriaList) {
        Comparator<T> comparator = Comparator.comparing(obj -> getFieldValues(obj, criteriaList), (values1, values2) -> {
            for (int i = 0; i < values1.size(); i++) {
                Object value1 = values1.get(i);
                Object value2 = values2.get(i);
                int result = compareFieldValues(value1, value2);
                if (result != 0) {
                    SortCriteria criteria = criteriaList.get(i);
                    if (criteria.getSortType() != 1) {
                        result *= -1;
                    }
                    return result;
                }
            }
            return 0;
        });

        dataList.sort(comparator);
    }

    private List<Object> getFieldValues(Object obj, List<SortCriteria> criteriaList) {
        List<Object> fieldValues = new ArrayList<>();
        Class clazz = entityClassMapUtil.entityClassMap.get(getEnt());
        if (clazz == null) {
            if (obj instanceof BaseEntity)
                clazz = BaseEntity.class;
            if (obj instanceof NonVersioningEntity<?>)
                clazz = NonVersioningEntity.class;
        }
        for (SortCriteria criteria : criteriaList) {
            String fieldName = criteria.getGridColumnConfig().getFieldName();
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                Object value = field.get(obj);
                fieldValues.add(value);
            } catch (Exception e) {
                e.printStackTrace();
                fieldValues.add(null);
            }
        }
        return fieldValues;
    }


    private int compareFieldValues(Object value1, Object value2) {
        if (value1 == null && value2 == null) {
            return 0;
        } else if (value1 == null) {
            return -1;
        } else if (value2 == null) {
            return 1;
        }
//        else if (value1 instanceof Comparable && value2 instanceof Comparable) {
//            return ((Comparable) value1).compareTo(value2);
//        }
        else {
            return ((String) value1).compareToIgnoreCase(((String) value2));
        }
    }
}
