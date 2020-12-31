package com.xust.hotel.common.restful;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author bhj
 */
@AllArgsConstructor
public enum StatusEnum {

    /**
     * 成功
     */
    OK(20000, "成功"),

    /**
     * 失败
     */
    ERROR(20001, "失败"),

    /**
     * 登录失败
     */
    LOGIN_ERROR(20002, "用户名或密码错误"),

    /**
     * 权限不足
     */
    ACCESS_ERROR(20003, "远程调用失败"),

    /**
     * 远程调用失败
     */
    REMOTE_ERROR(20004, "远程调用失败"),

    /**
     * 重复操作
     */
    REP_ERROR(20004, "重复操作"),

    /**
     * 参数错误
     */
    PARAM_ERROR(20006, "参数错误"),

    EXPIRED_ERROR(20007, "权限过期")
    ;

    /**
     * 状态码
     */
    private int code;

    /**
     * 状态说明
     */
    private String desc;
}
