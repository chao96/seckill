package com.seckill.api.enums;

/**
 * 通用状态码
 *
 * @author xuchao
 * @since 2020/2/26 08:29
 */
public enum StatusCode {

    Success(0, "成功"),
    Fail(-1, "失败"),
    InvalidParams(201, "非法的参数!"),
    UserNotLogin(202, "用户没登录"),
    ;

    private final Integer code;
    private final String msg;

    StatusCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
