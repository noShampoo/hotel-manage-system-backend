package com.xust.hotel.user.interceptor;

import com.xust.hotel.common.exception.ExpiredException;
import com.xust.hotel.common.exception.InnerErrorException;
import com.xust.hotel.common.exception.UserNotFoundException;
import com.xust.hotel.common.restful.Result;
import com.xust.hotel.common.restful.StatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author bhj
 */
@Slf4j
@ControllerAdvice
public class BaseExceptionHandle {

    @ResponseBody
    @ExceptionHandler(value = ExpiredException.class)
    public Result expired(ExpiredException e) {
        log.error("expired, ExpiredException msg={}, e={}", e.getMessage(), e);
        return new Result(true, StatusEnum.EXPIRED_ERROR, e.getMessage(), null);
    }

    @ResponseBody
    @ExceptionHandler(value = InnerErrorException.class)
    public Result inner(ExpiredException e) {
        log.error("inner, server inner error.msg={}, e={}", e.getMessage(), e);
        return new Result(false, StatusEnum.INNER_ERROR, e.getMessage(), null);
    }

    @ResponseBody
    @ExceptionHandler(value = UserNotFoundException.class)
    public Result userNotFound(Exception e) {
        log.error("userNotFound, user not found.msg={}, e={}", e.getMessage(), e);
        return new Result(false, StatusEnum.USER_NOT_FOUND, e.getMessage(), null);
    }
}
