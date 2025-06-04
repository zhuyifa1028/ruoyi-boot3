package com.ruoyi.system.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Schema(description = "SysMenuVO")
@Data
public class SysMenuVO implements Serializable {

    @Schema(description = "菜单ID")
    private String menuId;

    @Schema(description = "菜单名称")
    private String menuName;

    @Schema(description = "父菜单ID")
    private String parentId;

    @Schema(description = "父菜单名称")
    private String parentName;

    @Schema(description = "显示顺序")
    private Integer orderNum;

    @Schema(description = "路由地址")
    private String path;

    @Schema(description = "组件路径")
    private String component;

    @Schema(description = "路由参数")
    private String query;

    @Schema(description = "路由名称，默认和路由地址相同的驼峰格式（注意：因为vue3版本的router会删除名称相同路由，为避免名字的冲突，特殊情况可以自定义）")
    private String routeName;

    @Schema(description = "是否为外链（0是 1否）")
    private Integer isFrame;

    @Schema(description = "是否缓存（0缓存 1不缓存）")
    private Integer isCache;

    @Schema(description = "类型（M目录 C菜单 F按钮）")
    private Character menuType;

    @Schema(description = "显示状态（0显示 1隐藏）")
    private Character visible;

    @Schema(description = "菜单状态（0正常 1停用）")
    private Character status;

    @Schema(description = "权限字符串")
    private String perms;

    @Schema(description = "菜单图标")
    private String icon;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "子菜单")
    private List<SysMenuVO> children;

}
