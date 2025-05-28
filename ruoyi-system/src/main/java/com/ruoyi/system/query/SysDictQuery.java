package com.ruoyi.system.query;

import com.ruoyi.common.core.page.AbstractPageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "SysDictQuery")
@Data
public class SysDictQuery extends AbstractPageQuery {

    @Schema(description = "字典类型")
    private String dictType;

    @Schema(description = "字典值")
    private String dictValue;

    @Schema(description = "字典标签")
    private String dictLabel;

    @Schema(description = "状态")
    private Character status;

}
