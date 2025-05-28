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

@Comment("字典表")
@Entity
@Table(name = "sys_dict", catalog = "ruoyi")
@DynamicInsert
@DynamicUpdate
@SoftDelete
@Data
public class SysDict extends AuditableEntity {

    @Comment("字典ID")
    @Id
    @Column(length = 50)
    private String dictId;

    @Comment("字典排序")
    private Integer dictSort;

    @Comment("字典标签")
    @Column(length = 100)
    private String dictLabel;

    @Comment("字典键值")
    @Column(length = 100)
    private String dictValue;

    @Comment("字典类型")
    @Column(length = 100)
    private String dictType;

    @Comment("样式属性（其他样式扩展）")
    @Column(length = 100)
    private String cssClass;

    @Comment("表格字典样式")
    @Column(length = 100)
    private String listClass;

    @Comment("是否默认（Y是 N否）")
    @Column(length = 1)
    private Character isDefault;

    @Comment("状态（0正常 1停用）")
    @Column(length = 1)
    private Character status;

    @Comment("备注")
    @Column(length = 500)
    private String remark;

    @Override
    public void setId(String id) {
        this.dictId = id;
    }

    @Override
    public @NonNull String getId() {
        return dictId;
    }

}
