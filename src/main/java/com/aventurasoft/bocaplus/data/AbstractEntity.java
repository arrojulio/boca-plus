package com.aventurasoft.bocaplus.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;


import java.io.Serializable;
import java.util.Objects;

public abstract class AbstractEntity<T> implements Persistable, Serializable {

    @Id
    private T id;

    private int version;

    @Transient
    private boolean newEntity;

    @Override
    public T getId() {
        return id;
    }
    public void setId(T id) {this.id = id;}

    public void setVersion(int version) {
        this.version = version;
    }
    public int getVersion() {
        return version;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, version);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AbstractEntity that = (AbstractEntity) o;
        return version == that.version &&
                Objects.equals(id, that.id);
    }

    public void setNew(boolean newInstance) {
        this.newEntity = newInstance;
    }

    @Override
    public boolean isNew() {
        return newEntity;
    }
}
