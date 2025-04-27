package com.ruoyi.framework.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Data
@FieldNameConstants
public abstract class AuditableEntity extends PersistableEntity {

    @Comment("创建者")
    @Column(updatable = false, length = 20)
    @CreatedBy
    private String createBy;

    @Comment("创建时间")
    @Column(updatable = false)
    @CreatedDate
    private LocalDateTime createTime;

    @Comment("更新者")
    @Column(insertable = false, length = 20)
    @LastModifiedBy
    private String updateBy;

    @Comment("更新时间")
    @Column(insertable = false)
    @LastModifiedDate
    private LocalDateTime updateTime;

    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Fields {
    }
}
