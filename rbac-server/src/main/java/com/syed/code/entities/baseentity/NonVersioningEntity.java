package com.syed.code.entities.baseentity;

public abstract class NonVersioningEntity<T> {

    @Override
    public boolean equals(Object other) {
        return objectEqualsInternal((T) other);
    }

    public boolean objectEqualsInternal(T other) {
        if (other == this) return true;
        if (!(other instanceof T)) return false;
        return objectEquals(other);
    }

    public abstract boolean objectEquals(T other);
}
