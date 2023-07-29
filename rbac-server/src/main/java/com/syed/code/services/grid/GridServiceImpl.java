package com.syed.code.services.grid;

import com.syed.code.entities.baseentity.BaseEntity;
import com.syed.code.entities.baseentity.NonVersioningEntity;
import com.syed.code.entities.filterandSort.BaseConfig;
import com.syed.code.entities.filterandSort.FilterConfig;
import com.syed.code.entities.filterandSort.GridColumnConfig;
import com.syed.code.enums.EntityEnums;
import com.syed.code.enums.PermissionEnums;
import com.syed.code.enums.codes.ErrorCodes;
import com.syed.code.enums.codes.SuccessCodes;
import com.syed.code.exceptions.PermissionMissingException;
import com.syed.code.repositories.FilterAndSortRepository;
import com.syed.code.requestsandresponses.base.MessageVariable;
import com.syed.code.requestsandresponses.grid.GridDataResponse;
import com.syed.code.requestsandresponses.grid.GridMetadataResponse;
import com.syed.code.requestsandresponses.grid.GridRequest;
import com.syed.code.requestsandresponses.grid.SortCriteria;
import com.syed.code.services.grid.query.GridQueryService;
import com.syed.code.services.grid.query.QueryDetails;
import com.syed.code.services.grid.sanitization.BaseGridSanitizationService;
import com.syed.code.services.grid.sanitization.nonversioning.GenericNonVersioningGridSanitizationServiceImpl;
import com.syed.code.services.grid.sanitization.versioning.GenericVersioningGridSanitizationServiceImpl;
import com.syed.code.services.loggedinuser.LoggedInUserService;
import com.syed.code.utils.EntityGridSanitizationMapUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class GridServiceImpl implements GridService {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private EntityGridSanitizationMapUtil entityGridSanitizationMapUtil;

    @Autowired
    private GridQueryService gridQueryService;

    @Autowired
    private LoggedInUserService loggedInUserService;

    @Autowired
    private FilterAndSortRepository filterAndSortRepository;

    @Override
    public GridDataResponse getGridData(GridRequest gridRequest) throws PermissionMissingException {
        GridDataResponse response = new GridDataResponse();

        Session session = sessionFactory.openSession();
        List gridData;
        int itemNumber = gridRequest.getItemNumber() != null && gridRequest.getItemNumber() != 0 ? gridRequest.getItemNumber() : 20;
        int pageNumber = gridRequest.getPageNumber() != null ? gridRequest.getPageNumber() : 0;
        EntityEnums.Entity entity = EntityEnums.Entity.getByEntityId(gridRequest.getEntityId());
        MessageVariable messageVariable = getMessageVariable(entity);

        if (!loggedInUserService.isActionAllowed(entity, PermissionEnums.GenericPerms.View)) {
            throw new PermissionMissingException(entity, "Entity view permission missing");
        }

        QueryDetails queryDetails = gridQueryService.formGridQueryDetails(gridRequest);


        try {
            Query query = session.createQuery(queryDetails.getQueryString());
            for (Map.Entry<String, Object> entry : queryDetails.getQueryParamMap().entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
            gridData = query.setMaxResults(itemNumber).setFirstResult(itemNumber * (pageNumber - 1)).getResultList();

            List<SortCriteria> criteriaList = new ArrayList<>();

            if (gridData != null && !gridData.isEmpty()) {
                Object instance = gridData.get(0);
                Object bean = null;
                BaseGridSanitizationService service = null;
                Class clazz = entityGridSanitizationMapUtil.entityGridSanitizationMap.get(entity);
                if (clazz != null)
                    bean = context.getBean(clazz);
                if (bean == null) {
                    if (instance instanceof NonVersioningEntity) {
                        bean = context.getBean(GenericNonVersioningGridSanitizationServiceImpl.class);
                    } else if (instance instanceof BaseEntity) {
                        bean = context.getBean(GenericVersioningGridSanitizationServiceImpl.class);
                    }
                }
                if (bean != null) {
                    service = (BaseGridSanitizationService) bean;
                    service.sanitizeData(gridData, criteriaList);
                }
            }

            response.setGridData(gridData);
            messageVariable.setApplicationCode(SuccessCodes.Generic.S00_004.code);
            response.setStatusCode(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            messageVariable.setApplicationCode(ErrorCodes.Generic.E00_003.code);
            response.setStatusCode(HttpStatus.BAD_REQUEST);
        }

        response.getMessageVariables().add(messageVariable);
        return response;
    }

    @Override
    public GridMetadataResponse getGridMetadata(Integer id) {
        GridMetadataResponse response = new GridMetadataResponse();
        List<GridColumnConfig> gridColumnConfigs = filterAndSortRepository.getGridColumnConfigsByEntityId(id);
        for (GridColumnConfig config : gridColumnConfigs)
            sanitizeConfig(config);

        List<FilterConfig> filterConfigs = filterAndSortRepository.getFilterConfigsByEntityId(id);
        for (FilterConfig config : filterConfigs)
            sanitizeConfig(config);

        response.setGridColumnConfigs(gridColumnConfigs);
        response.setFilterConfigs(filterConfigs);
        response.setStatusCode(HttpStatus.OK);
        return response;
    }

    private MessageVariable getMessageVariable(EntityEnums.Entity entity) {
        MessageVariable messageVariable = new MessageVariable();
        messageVariable.setEntity(entity.entityName);
        messageVariable.setEntityId(entity.entityId);
        return messageVariable;
    }

    private void sanitizeConfig(BaseConfig config) {
        config.setColumnLogicalName(null);
        config.setDependantTable(null);
        config.setDependantColumn(null);
        config.setDependantFilterColumn(null);
    }

}
