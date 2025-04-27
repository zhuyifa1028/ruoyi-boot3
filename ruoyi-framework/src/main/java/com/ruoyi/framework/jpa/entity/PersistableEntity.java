package com.ruoyi.framework.jpa.entity;

import com.ruoyi.framework.jpa.generator.SnowflakeIdGenerator;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import org.springframework.data.domain.Persistable;

@MappedSuperclass
public abstract class PersistableEntity implements Persistable<String> {

    @Transient
    private boolean isNew = false;

    @Transient
    public boolean isNew() {
        return isNew;
    }

    @Transient
    public void markNew() {
        this.isNew = true;
        this.setId(SnowflakeIdGenerator.DEFAULT.nextIdAsString());
    }

    public abstract void setId(String id);

}
