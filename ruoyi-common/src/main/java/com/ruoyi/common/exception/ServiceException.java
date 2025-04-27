package com.ruoyi.common.exception;

import com.ruoyi.common.utils.StringUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 业务异常
 *
 * @author ruoyi
 */
@Data
@NoArgsConstructor
public final class ServiceException extends RuntimeException {

    /**
     * 错误码
     */
    private Integer code;

    public ServiceException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public ServiceException(String message, Object... params) {
        super(StringUtils.format(message, params));
    }

}
