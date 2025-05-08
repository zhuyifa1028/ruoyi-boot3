package com.ruoyi.system.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.Comment;

@Comment("角色和部门关联表")
@Entity
@Table(name = "sys_role_dept", catalog = "ruoyi")
@Data
public class SysRoleDept {

    @Comment("角色ID")
    @Id
    @Column(length = 50)
    private String roleId;

    @Comment("部门ID")
    @Id
    @Column(length = 50)
    private String deptId;

}
