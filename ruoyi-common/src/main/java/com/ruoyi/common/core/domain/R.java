package com.ruoyi.common.core.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.List;

@Schema(description = "R")
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class R<T> implements Serializable {

    @Schema(description = "状态码")
    private int code;

    @Schema(description = "状态码的描述")
    private String message;

    @Schema(description = "返回数据")
    private T data;

    @Schema(description = "返回数据的总条数")
    private Long total;

    public static R<Void> ok() {
        return new R<Void>()
                .setCode(HttpStatus.OK.value())
                .setMessage("操作成功");
    }

    public static <T> R<T> ok(T data) {
        return new R<T>()
                .setCode(HttpStatus.OK.value())
                .setMessage("操作成功")
                .setData(data);
    }

    public static <T> R<List<T>> ok(Page<T> page) {
        return new R<List<T>>()
                .setCode(HttpStatus.OK.value())
                .setMessage("操作成功")
                .setData(page.getContent())
                .setTotal(page.getTotalElements());
    }

    public static R<Void> fail(String message) {
        return new R<Void>()
                .setCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .setMessage(message);
    }

}
