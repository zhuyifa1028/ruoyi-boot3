package com.ruoyi.system.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.Comment;

import java.io.Serializable;

@Comment("角色和菜单关联表")
@Entity
@Table(name = "sys_role_menu", catalog = "ruoyi")
@Data
public class SysRoleMenu implements Serializable {

    @Comment("角色ID")
    @Id
    @Column(length = 50)
    private String roleId;

    @Comment("菜单ID")
    @Id
    @Column(length = 50)
    private String menuId;

}
