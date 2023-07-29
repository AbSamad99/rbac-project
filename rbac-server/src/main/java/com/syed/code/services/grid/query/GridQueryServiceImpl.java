package com.syed.code.services.grid.query;

import com.syed.code.entities.filterandSort.BaseConfig;
import com.syed.code.entities.filterandSort.FilterConfig;
import com.syed.code.entities.filterandSort.GridColumnConfig;
import com.syed.code.enums.EntityEnums;
import com.syed.code.repositories.FilterAndSortRepository;
import com.syed.code.requestsandresponses.grid.FilterCriteria;
import com.syed.code.requestsandresponses.grid.GridRequest;
import com.syed.code.requestsandresponses.grid.SortCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GridQueryServiceImpl implements GridQueryService {

    @Autowired
    private FilterAndSortRepository filterAndSortRepository;

    @Override
    public QueryDetails formGridQueryDetails(GridRequest gridRequest) {
        QueryDetails queryDetails = new QueryDetails();
        EntityEnums.Entity entity = EntityEnums.Entity.getByEntityId(gridRequest.getEntityId());
        StringBuilder builder = new StringBuilder();
        String mainAlias = entity.entityName.substring(0, 2) + 0;
        Map<String, Object> queryParamMap = new HashMap<>();

        List<SortCriteria> validSortCriteria = getValidSortCriteria(gridRequest.getSortCriteria(), entity.entityId);
        List<FilterCriteria> validFilterCriteria = getValidFilterCriteria(gridRequest.getFilterCriteria(), entity.entityId);

        formBaseQuery(builder, entity.entityName, mainAlias);
        formJoinClause(builder, validSortCriteria, validFilterCriteria, mainAlias);
        formWhereClause(builder, validSortCriteria, validFilterCriteria, mainAlias, entity, queryParamMap);
        formOrderByClause(builder, validSortCriteria, mainAlias);

        queryDetails.setQueryString(builder.toString());
        queryDetails.setQueryParamMap(queryParamMap);

        return queryDetails;
    }

    private List<SortCriteria> getValidSortCriteria(List<SortCriteria> sortCriteria, Integer entityId) {
        List<SortCriteria> validSortCriteria = new ArrayList<>();
        Map<Long, GridColumnConfig> gridColumnConfigMap = new HashMap<>();
        if (sortCriteria != null && !sortCriteria.isEmpty()) {
            List<Long> ids = sortCriteria.stream().map(SortCriteria::getGridColumnConfigId).toList();
            List<GridColumnConfig> gridColumnConfigs = filterAndSortRepository.getGridColumnConfigsByIds(ids, entityId);
            if (!gridColumnConfigs.isEmpty())
                for (GridColumnConfig gridColumnConfig : gridColumnConfigs)
                    gridColumnConfigMap.put(gridColumnConfig.getId(), gridColumnConfig);
            for (SortCriteria criteria : sortCriteria) {
                GridColumnConfig columnConfig = gridColumnConfigMap.get(criteria.getGridColumnConfigId());
                if (columnConfig != null) {
                    criteria.setGridColumnConfig(columnConfig);
                    validSortCriteria.add(criteria);
                }
            }
        }
        return validSortCriteria;
    }

    private List<FilterCriteria> getValidFilterCriteria(List<FilterCriteria> filterCriteria, Integer entityId) {
        List<FilterCriteria> validFilterCriteria = new ArrayList<>();
        Map<Long, FilterConfig> filterConfigMap = new HashMap<>();
        if (filterCriteria != null && !filterCriteria.isEmpty()) {
            List<Long> ids = filterCriteria.stream().map(FilterCriteria::getFilterConfigId).toList();
            List<FilterConfig> filterConfigs = filterAndSortRepository.getFilterConfigsByIds(ids, entityId);
            if (!filterConfigs.isEmpty())
                for (FilterConfig filterConfig : filterConfigs)
                    filterConfigMap.put(filterConfig.getId(), filterConfig);
            for (FilterCriteria criteria : filterCriteria) {
                FilterConfig filterConfig = filterConfigMap.get(criteria.getFilterConfigId());
                if (filterConfig != null) {
                    criteria.setFilterConfig(filterConfig);
                    validFilterCriteria.add(criteria);
                }
            }
        }
        return validFilterCriteria;
    }

    private void formBaseQuery(StringBuilder builder, String entityName, String mainAlias) {
        builder
                .append("select ")
                .append(mainAlias)
                .append(" from ")
                .append(entityName)
                .append(" ")
                .append(mainAlias);
    }

//    private void formJoinClause(StringBuilder builder, List<SortCriteria> validSortCriteria, String mainAlias) {
////        StringBuilder builder = new StringBuilder();
//        for (SortCriteria criteria : validSortCriteria) {
//            GridColumnConfig columnConfig = criteria.getGridColumnConfig();
//            if (columnConfig.getDependantTable() != null && !columnConfig.getDependantTable().isEmpty() && columnConfig.getDependantColumn() != null && !columnConfig.getDependantColumn().isEmpty()) {
//                String table = columnConfig.getDependantTable();
//                String alias = table.substring(0, 2).toLowerCase();
//                String column = columnConfig.getDependantColumn();
//                builder
//                        .append(" inner join ")
//                        .append(table)
//                        .append(" ")
//                        .append(alias)
//                        .append(" on ")
//                        .append(mainAlias)
//                        .append(".")
//                        .append(columnConfig.getColumnLogicalName())
//                        .append("=")
//                        .append(alias)
//                        .append(".")
//                        .append(column)
//                        .append(" ");
//            }
//        }
//    }

    private void formJoinClause(StringBuilder query, List<SortCriteria> validSortCriteria, List<FilterCriteria> validFilterCriteria, String mainAlias) {
        Map<String, StringBuilder> stringBuilderMap = new HashMap<>();

        for (SortCriteria criteria : validSortCriteria)
            appendJoinClause(stringBuilderMap, criteria.getGridColumnConfig(), mainAlias);

        for (FilterCriteria criteria : validFilterCriteria)
            appendJoinClause(stringBuilderMap, criteria.getFilterConfig(), mainAlias);

        for (StringBuilder builder : stringBuilderMap.values())
            query.append(builder.toString());
    }

    private void appendJoinClause(Map<String, StringBuilder> stringBuilderMap, BaseConfig config, String mainAlias) {
        if (config.getDependantTable() != null && !config.getDependantTable().isEmpty() && config.getDependantColumn() != null && !config.getDependantColumn().isEmpty()) {
            String table = config.getDependantTable();
            String alias = table.substring(0, 2).toLowerCase();
            StringBuilder builder = stringBuilderMap.get(alias);
            if (builder == null) {
                builder = new StringBuilder();
                stringBuilderMap.put(alias, builder);
                builder
                        .append(" inner join ")
                        .append(table)
                        .append(" ")
                        .append(alias)
                        .append(" on ");
            } else {
                builder
                        .append(" and ");
            }
            builder
                    .append(mainAlias)
                    .append(".")
                    .append(config.getColumnLogicalName())
                    .append("=")
                    .append(alias)
                    .append(".")
                    .append(config.getDependantColumn())
                    .append(" ");
        }
    }

    private void formWhereClause(StringBuilder builder, List<SortCriteria> validSortCriteria, List<FilterCriteria> validFilterCriteria, String mainAlias, EntityEnums.Entity entity, Map<String, Object> queryParamMap) {
        Boolean whereClauseAdded = false;
        whereClauseAdded = formFilterWhereClause(builder, validFilterCriteria, mainAlias, whereClauseAdded, queryParamMap);
        whereClauseAdded = formWhereClauseIsActive(builder, validSortCriteria, validFilterCriteria, whereClauseAdded);
        if (!whereClauseAdded && entity.isVersioning == 1)
            builder
                    .append(" where ")
                    .append(mainAlias)
                    .append(".isActive=1 ");
    }

//    private Boolean formSortWhereClause(StringBuilder builder, List<SortCriteria> validSortCriteria, Boolean whereClauseAdded) {
////        StringBuilder builder = new StringBuilder();
//        for (SortCriteria criteria : validSortCriteria) {
//            GridColumnConfig columnConfig = criteria.getGridColumnConfig();
//            if (columnConfig.getDependantTable() != null && !columnConfig.getDependantTable().isEmpty() && columnConfig.getDependantColumn() != null && !columnConfig.getDependantColumn().isEmpty() && columnConfig.getDependantColumn().equals("key")) {
//                if (!whereClauseAdded) {
//                    builder.append(" where ");
//                    whereClauseAdded = true;
//                } else {
//                    builder.append(" and ");
//                }
//                String table = columnConfig.getDependantTable();
//                String alias = table.substring(0, 2).toLowerCase();
//                builder
//                        .append(alias)
//                        .append(".isActive=1 ");
//            }
//        }
//        return whereClauseAdded;
//    }

    private Boolean formWhereClauseIsActive(StringBuilder builder, List<SortCriteria> validSortCriteria, List<FilterCriteria> validFilterCriteria, Boolean whereClauseAdded) {
        for (SortCriteria criteria : validSortCriteria)
            whereClauseAdded = appendJoinIsActive(builder, criteria.getGridColumnConfig(), whereClauseAdded);
        for (FilterCriteria criteria : validFilterCriteria)
            whereClauseAdded = appendJoinIsActive(builder, criteria.getFilterConfig(), whereClauseAdded);

        return whereClauseAdded;
    }

    private Boolean appendJoinIsActive(StringBuilder builder, BaseConfig config, Boolean whereClauseAdded) {
        if (config.getDependantTable() != null && !config.getDependantTable().isEmpty() && config.getDependantColumn() != null && !config.getDependantColumn().isEmpty() && config.getDependantColumn().equals("key")) {
            appendWhereOrAnd(builder, whereClauseAdded);
            whereClauseAdded = true;
            String table = config.getDependantTable();
            String alias = table.substring(0, 2).toLowerCase();
            builder
                    .append(alias)
                    .append(".isActive=1 ");
        }
        return whereClauseAdded;
    }

    private void formOrderByClause(StringBuilder builder, List<SortCriteria> validSortCriteria, String mainAlias) {
        Boolean orderByClauseAdded = false;
        for (SortCriteria criteria : validSortCriteria) {
            GridColumnConfig columnConfig = criteria.getGridColumnConfig();
            appendOrderBy(builder, orderByClauseAdded);
            orderByClauseAdded = true;
            String aliasToUse = mainAlias;
            String columnToUse = columnConfig.getColumnLogicalName();
            if (columnConfig.getDependantTable() != null && !columnConfig.getDependantTable().isEmpty() && columnConfig.getDependantColumn() != null && !columnConfig.getDependantColumn().isEmpty()) {
                String table = columnConfig.getDependantTable();
                aliasToUse = table.substring(0, 2).toLowerCase();
                columnToUse = columnConfig.getDependantFilterColumn();
            }
            builder
                    .append(aliasToUse)
                    .append(".")
                    .append(columnToUse)
                    .append(" ")
                    .append(criteria.getSortType() != null && criteria.getSortType().equals(1) ? "asc" : "desc");
        }
    }

    private Boolean formFilterWhereClause(StringBuilder builder, List<FilterCriteria> validFilterCriteria, String mainAlias, Boolean whereClauseAdded, Map<String, Object> queryParamMap) {
        for (FilterCriteria criteria : validFilterCriteria) {
            FilterConfig filterConfig = criteria.getFilterConfig();
            Object filterValue = criteria.getFilterValue();
            appendWhereOrAnd(builder, whereClauseAdded);
            whereClauseAdded = true;

            String aliasToUse = mainAlias;
            String columnToUse = filterConfig.getColumnLogicalName();

            if (filterConfig.getDependantTable() != null && !filterConfig.getDependantTable().isEmpty() && filterConfig.getDependantColumn() != null && !filterConfig.getDependantColumn().isEmpty()) {
                String table = filterConfig.getDependantTable();
                aliasToUse = table.substring(0, 2).toLowerCase();
                columnToUse = filterConfig.getDependantFilterColumn();
            }

            String queryParamName = aliasToUse + columnToUse;
            String queryParam = ":" + queryParamName;

            int dataTypeId = filterConfig.getDataTypeId();
            EntityEnums.DataType dataType = EntityEnums.DataType.getByDataTypeId(dataTypeId);
            filterValue = getValueForDataType(filterValue, dataType);

            if (dataType == EntityEnums.DataType.String) {
                builder
                        .append("LOWER(")
                        .append(aliasToUse)
                        .append(".")
                        .append(columnToUse)
                        .append(")");
            } else {
                builder
                        .append(aliasToUse)
                        .append(".")
                        .append(columnToUse);
            }

            switch (dataType) {
                case Integer, Long, Boolean, Date -> builder
                        .append("=")
                        .append(queryParam);
                case String -> builder
                        .append(" like LOWER(CONCAT('%', ")
                        .append(queryParam)
                        .append(", '%'))");
            }

            queryParamMap.put(queryParamName, filterValue);
        }
        return whereClauseAdded;
    }

    private Object getValueForDataType(Object value, EntityEnums.DataType dataType) {
        return switch (dataType) {
            case Integer -> (Integer) value;
            case Long -> (Long) value;
            case String -> (String) value;
            case Boolean -> (Boolean) value;
            case Date -> (Date) value;
            default -> value;
        };
    }

    private void appendSeparatorOnCondition(StringBuilder builder, Boolean condition, String option1, String option2) {
        if (!condition) {
            builder.append(option1);
        } else {
            builder.append(option2);
        }
    }

    private void appendWhereOrAnd(StringBuilder builder, Boolean whereClauseAdded) {
        appendSeparatorOnCondition(builder, whereClauseAdded, " where ", " and ");
    }

    private void appendOrderBy(StringBuilder builder, Boolean orderByClauseAdded) {
        appendSeparatorOnCondition(builder, orderByClauseAdded, " order by ", ", ");
    }
}
