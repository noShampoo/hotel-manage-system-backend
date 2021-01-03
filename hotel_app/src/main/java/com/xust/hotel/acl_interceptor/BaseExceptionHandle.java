package com.xust.hotel.acl_interceptor;

import com.xust.hotel.common.exception.*;
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
    public Result expired() {
        log.error("expired, ExpiredException");
        return new Result(true, StatusEnum.EXPIRED_ERROR, null, null);
    }

    @ResponseBody
    @ExceptionHandler(value = InnerErrorException.class)
    public Result inner() {
        log.error("inner, server inner error.");
        return new Result(false, StatusEnum.INNER_ERROR, null, null);
    }

    @ResponseBody
    @ExceptionHandler(value = UserNotFoundException.class)
    public Result userNotFound() {
        log.error("userNotFound, user not found");
        return new Result(false, StatusEnum.USER_NOT_FOUND,null, null);
    }

    @ResponseBody
    @ExceptionHandler(value = NotDeleteException.class)
    public Result notDelete() {
        log.error("notDelete, can't delete");
        return new Result(false, StatusEnum.CAN_NOT_DELETE, null, null);
    }

    @ResponseBody
    @ExceptionHandler(value = NoSuchKeyException.class)
    public Result noSuchKey() {
        log.error("noSuchKey, no such key");
        return new Result(false, StatusEnum.NO_SUCH_KEY, null, null);
    }

    @ResponseBody
    @ExceptionHandler(value = KeyExistException.class)
    public Result keyExist() {
        log.error("keyExist.");
        return new Result(false, StatusEnum.KEY_EXIST, null, null);
    }
}
