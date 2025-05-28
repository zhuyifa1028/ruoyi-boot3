package com.ruoyi.system.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Schema(description = "SysDictVO")
@Data
public class SysDictVO implements Serializable {

    @Schema(description = "字典ID")
    private String dictId;

    @Schema(description = "字典排序")
    private Long dictSort;

    @Schema(description = "字典标签")
    private String dictLabel;

    @Schema(description = "字典键值")
    private String dictValue;

    @Schema(description = "字典类型")
    private String dictType;

    @Schema(description = "样式属性（其他样式扩展）")
    private String cssClass;

    @Schema(description = "表格字典样式")
    private String listClass;

    @Schema(description = "是否默认（Y是 N否）")
    private Character isDefault;

    @Schema(description = "状态（0正常 1停用）")
    private Character status;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
