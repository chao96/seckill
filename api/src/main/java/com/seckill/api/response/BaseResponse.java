package com.seckill.api.response;

import com.seckill.api.enums.StatusCode;
import lombok.Data;

/**
 * 通用返回结果
 *
 * @author xuchao
 * @since 2020/2/26 08:29
 */
@Data
public class BaseResponse<T> {

    private Integer code;
    private String msg;
    private T data;

    public BaseResponse(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public BaseResponse(StatusCode statusCode) {
        this.code = statusCode.getCode();
        this.msg = statusCode.getMsg();
    }

    public BaseResponse(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
