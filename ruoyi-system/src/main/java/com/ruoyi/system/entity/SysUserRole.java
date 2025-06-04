package com.ruoyi.system.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.Comment;

import java.io.Serializable;

@Comment("用户和角色关联表")
@Entity
@Table(name = "sys_user_role", catalog = "ruoyi")
@Data
public class SysUserRole implements Serializable {

    @Comment("用户ID")
    @Id
    private Long userId;

    @Comment("角色ID")
    @Id
    @Column(length = 50)
    private String roleId;

}
