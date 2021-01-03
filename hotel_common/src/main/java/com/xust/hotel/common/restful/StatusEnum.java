package com.xust.hotel.common.restful;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author bhj
 */
@AllArgsConstructor
public enum StatusEnum implements Serializable {

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

    /**
     * 权限过期
     */
    EXPIRED_ERROR(20007, "权限过期"),

    /**
     * 用户不存在
     */
    USER_NOT_FOUND(2008, "用户不存在"),

    /**
     * inner error
     */
    INNER_ERROR(50000, "内部异常")
    ;

    /**
     * 状态码
     */
    private int code;

    /**
     * 状态说明
     */
    private String desc;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
