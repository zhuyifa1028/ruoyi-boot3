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

@Comment("菜单表")
@Entity
@Table(name = "sys_menu", catalog = "ruoyi")
@DynamicInsert
@DynamicUpdate
@SoftDelete
@Data
public class SysMenu extends AuditableEntity {

    @Comment("菜单ID")
    @Id
    @Column(length = 50)
    private String menuId;

    @Comment("菜单名称")
    @Column(length = 50)
    private String menuName;

    @Comment("父菜单ID")
    @Column(length = 50)
    private String parentId;

    @Comment("父菜单名称")
    @Column(length = 50)
    private String parentName;

    @Comment("显示顺序")
    @Column(length = 10)
    private Integer orderNum;

    @Comment("路由地址")
    @Column(length = 200)
    private String path;

    @Comment("组件路径")
    @Column(length = 200)
    private String component;

    @Comment("路由参数")
    @Column(length = 200)
    private String query;

    @Comment("路由名称，默认和路由地址相同的驼峰格式（注意：因为vue3版本的router会删除名称相同路由，为避免名字的冲突，特殊情况可以自定义）")
    @Column(length = 50)
    private String routeName;

    @Comment("是否为外链（0是 1否）")
    @Column(length = 1)
    private Integer isFrame;

    @Comment("是否缓存（0缓存 1不缓存）")
    @Column(length = 1)
    private Integer isCache;

    @Comment("类型（M目录 C菜单 F按钮）")
    @Column(length = 1)
    private Character menuType;

    @Comment("显示状态（0显示 1隐藏）")
    @Column(length = 1)
    private Character visible;

    @Comment("菜单状态（0正常 1停用）")
    @Column(length = 1)
    private Character status;

    @Comment("权限字符串")
    @Column(length = 100)
    private String perms;

    @Comment("菜单图标")
    @Column(length = 100)
    private String icon;

    @Comment("备注")
    @Column(length = 500)
    private String remark;

    @Override
    public void setId(String id) {
        this.menuId = id;
    }

    @Override
    public @NonNull String getId() {
        return menuId;
    }

}
