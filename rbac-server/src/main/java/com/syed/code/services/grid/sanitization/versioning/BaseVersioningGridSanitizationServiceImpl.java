package com.syed.code.services.grid.sanitization.versioning;

import com.syed.code.entities.baseentity.BaseEntity;
import com.syed.code.entities.user.User;
import com.syed.code.repositories.UserRepository;
import com.syed.code.requestsandresponses.grid.SortCriteria;
import com.syed.code.services.grid.sanitization.BaseGridSanitizationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public abstract class BaseVersioningGridSanitizationServiceImpl<T extends BaseEntity> extends BaseGridSanitizationServiceImpl<T> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void sanitizeData(List<T> dataList, List<SortCriteria> criteriaList) {
        if (dataList.isEmpty())
            return;

        Map<Long, User> userMap = new HashMap<Long, User>();
        Set<Long> userKeys = new HashSet<>(dataList.stream().map(ent -> ent.getCreatedBy()).toList());
        userKeys.addAll(dataList.stream().map(ent -> ent.getModifiedBy()).toList());
        List<User> users = userRepository.getUsersByIds(userKeys.stream().toList());
        for (User user : users)
//            if (!userMap.containsKey(user.getKey()))
//                userMap.put(user.getKey(), user);
            if (!userMap.containsKey(user.getId()))
                userMap.put(user.getId(), user);

        for (T entity : dataList) {
            entity.setCreatedByName(userMap.get(entity.getCreatedBy()) != null ? userMap.get(entity.getCreatedBy()).getFirstName() : null);
            entity.setModifiedByName(userMap.get(entity.getModifiedBy()) != null ? userMap.get(entity.getModifiedBy()).getFirstName() : null);
        }

        sanitizeDataInternal(dataList);
//        sortList(dataList, criteriaList);
    }
}
