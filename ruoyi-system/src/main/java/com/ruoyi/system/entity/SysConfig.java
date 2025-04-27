package com.ruoyi.system.entity;

import com.ruoyi.framework.jpa.entity.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NonNull;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SoftDelete;

@Comment("配置表")
@Entity
@Table(name = "sys_config", catalog = "ruoyi")
@DynamicInsert
@DynamicUpdate
@SoftDelete
@Data
public class SysConfig extends AuditableEntity {

    @Comment("配置ID")
    @Id
    @Column(length = 50)
    private String configId;

    @Comment("配置名称")
    @Column(length = 100)
    private String configName;

    @Comment("配置键名")
    @Column(length = 100)
    private String configKey;

    @Comment("配置键值")
    @Column(length = 500)
    private String configValue;

    @Comment("系统内置（Y是 N否）")
    @Column(length = 1)
    private String configType;

    @Comment("备注")
    @Column(length = 500)
    private String remark;

    @Override
    public void setId(String id) {
        this.configId = id;
    }

    @Override
    public @NonNull String getId() {
        return configId;
    }

}
