package com.ruoyi.common.core.page;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Schema(description = "AbstractPageQuery")
@Data
public abstract class AbstractPageQuery implements Serializable {

    @Schema(description = "页面索引")
    @NotNull(message = "页面索引不能为空")
    @Min(value = 1, message = "页面索引不得小于 1")
    private Integer pageNumber;

    @Schema(description = "页面大小")
    @NotNull(message = "页面大小不能为空")
    @Min(value = 1, message = "页面大小不得小于 1")
    private Integer pageSize;

    public Integer getPageNumber() {
        return --pageNumber;
    }

}
