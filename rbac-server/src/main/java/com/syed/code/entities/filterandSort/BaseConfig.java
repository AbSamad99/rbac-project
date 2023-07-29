package com.syed.code.entities.filterandSort;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

@Data
@MappedSuperclass
public class BaseConfig {

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "entity_id", nullable = false)
    private Integer entityId;

    @Column(name = "column_logical_name")
    private String columnLogicalName;

    @Column(name = "data_type_id", nullable = false)
    private Integer dataTypeId;

    @Column(name = "dependant_table", length = 50)
    private String dependantTable;

    @Column(name = "dependant_column", length = 50)
    private String dependantColumn;

    @Column(name = "dependant_filter_column", length = 50)
    private String dependantFilterColumn;
}
