package com.syed.code.entities.filterandSort;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "filter_config")
public class FilterConfig extends BaseConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "is_filterable", nullable = false)
    private Integer isFilterable;
}
