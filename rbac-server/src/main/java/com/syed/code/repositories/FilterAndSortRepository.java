package com.syed.code.repositories;

import com.syed.code.entities.filterandSort.FilterConfig;
import com.syed.code.entities.filterandSort.GridColumnConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FilterAndSortRepository extends JpaRepository<GridColumnConfig, Long> {

    @Query("select g from GridColumnConfig g where g.id in :ids and g.entityId = :entityId")
    public List<GridColumnConfig> getGridColumnConfigsByIds(@Param("ids") List<Long> ids, @Param("entityId") Integer entityId);

    @Query("select f from FilterConfig f where f.id in :ids and f.entityId = :entityId and f.isFilterable = 1")
    public List<FilterConfig> getFilterConfigsByIds(@Param("ids") List<Long> ids, @Param("entityId") Integer entityId);

    @Query("select g from GridColumnConfig g where g.entityId = :entityId")
    public List<GridColumnConfig> getGridColumnConfigsByEntityId(@Param("entityId") Integer entityId);

    @Query("select f from FilterConfig f where f.entityId = :entityId")
    public List<FilterConfig> getFilterConfigsByEntityId(@Param("entityId") Integer entityId);
}
