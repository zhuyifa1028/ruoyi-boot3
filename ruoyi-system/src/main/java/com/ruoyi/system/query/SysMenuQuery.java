package com.ruoyi.system.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Schema(description = "SysMenuQuery")
@Data
public class SysMenuQuery implements Serializable {

    @Schema(description = "菜单名称")
    private String menuName;

    @Schema(description = "显示状态（0显示 1隐藏）")
    private Character visible;

    @Schema(description = "菜单状态（0正常 1停用）")
    private Character status;

}
