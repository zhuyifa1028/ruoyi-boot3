package com.ruoyi.system.query;

import com.ruoyi.common.core.page.AbstractPageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Schema(description = "SysConfigQuery")
@Data
public class SysConfigQuery extends AbstractPageQuery {

    @Schema(description = "参数名称")
    private String configName;

    @Schema(description = "系统内置（Y是 N否）")
    private String configType;

    @Schema(description = "参数键名")
    private String configKey;

    @Schema(description = "创建事件——区间开始")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startTime;

    @Schema(description = "创建时间——区间结束")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endTime;

}
