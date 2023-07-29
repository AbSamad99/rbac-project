package com.syed.code.entities.filterandSort;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "grid_column_config")
public class GridColumnConfig extends BaseConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(name = "column_name", length = 50)
//    private String columnName;

    @Column(name = "field_name", nullable = false, length = 50)
    private String fieldName;

    @Column(name = "is_shown", nullable = false)
    private Integer isShown;

    @Column(name = "show_default", nullable = false)
    private Integer showDefault;

}
