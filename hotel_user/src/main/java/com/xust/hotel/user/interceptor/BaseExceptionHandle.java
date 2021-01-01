package com.xust.hotel.user.interceptor;

import com.xust.hotel.common.exception.ExpiredException;
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
}
