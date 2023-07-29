package com.syed.code.services.objecthashmapper;

import com.syed.code.entities.baseentity.BaseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Service
public class ObjectHashMapperServiceImpl<T extends BaseEntity> {

    public Map<String, Object> getHashMapFromObject(Class clazz, T obj) throws IllegalAccessException {
        Map<String, Object> myObjectAsDict = new HashMap<>();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (!field.getName().equals("id"))
                myObjectAsDict.put(field.getName(), field.get(obj));
        }
        return myObjectAsDict;
    }
}
