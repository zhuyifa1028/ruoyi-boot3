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

@Comment("部门表")
@Entity
@Table(name = "sys_dept", catalog = "ruoyi")
@DynamicInsert
@DynamicUpdate
@SoftDelete
@Data
public class SysDept extends AuditableEntity {

    @Comment("部门ID")
    @Id
    @Column(length = 50)
    private String deptId;

    @Comment("上级部门ID")
    @Column(length = 50)
    private String parentId;

    @Comment("所有上级部门ID")
    @Column(length = 500)
    private String parentAll;

    @Comment("部门名称")
    @Column(length = 100)
    private String deptName;

    @Comment("显示顺序")
    @Column(length = 10)
    private Integer orderNum;

    @Comment("负责人")
    @Column(length = 20)
    private String leader;

    @Comment("联系电话")
    @Column(length = 11)
    private String phone;

    @Comment("邮箱")
    @Column(length = 50)
    private String email;

    @Comment("部门状态（0正常 1停用）")
    @Column(length = 1)
    private String status;

    @Override
    public void setId(String id) {
        this.deptId = id;
    }

    @Override
    public @NonNull String getId() {
        return deptId;
    }

}
