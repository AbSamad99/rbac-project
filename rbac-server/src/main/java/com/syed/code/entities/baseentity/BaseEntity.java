package com.syed.code.entities.baseentity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import lombok.Data;

import java.util.Date;
import java.util.Objects;

// Base Hibernate Entity which is extended by all our application entities. Contains versioning information. T passed in
// the generic parameters is used during object comparison
@Data
@MappedSuperclass
public abstract class BaseEntity<T> {

    @Column(name = "key")
    private Long key;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @Column(name = "modified_at", nullable = false)
    private Date modifiedAt;

    @Column(name = "modified_by", nullable = false)
    private Long modifiedBy;

    @Column(name = "version", nullable = false, precision = 10)
    private Long version;

    @Column(name = "is_active", nullable = false, precision = 1)
    private Integer isActive;

    @Transient
    private String createdByName;

    @Transient
    private String modifiedByName;

    @Transient
    Boolean copy = false;

    abstract public Long getId();

    abstract public void setId(Long id);

    public abstract boolean objectEquals(T other);

    @Override
    public boolean equals(Object other) {
        return objectEqualsInternal((T) other);
    }

    //    Main comparison method, used to determine if 2 objects are equal, calls the objectEquals method which is implemented
    //    by each and every Entity.
    public boolean objectEqualsInternal(T other) {
        if (other == this) return true;
        if (!(other instanceof T)) return false;
        return objectEquals(other);
    }

    public abstract String getEntityName();

    @Override
    public int hashCode() {
        return Objects.hash(key, createdAt, createdBy, modifiedAt, modifiedBy, version, isActive);
    }
}
