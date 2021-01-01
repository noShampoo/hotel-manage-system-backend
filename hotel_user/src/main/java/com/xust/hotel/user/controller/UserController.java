package com.xust.hotel.user.controller;

import com.google.common.collect.Maps;
import com.xust.hotel.common.exception.InnerErrorException;
import com.xust.hotel.common.restful.RequestParam;
import com.xust.hotel.common.restful.Result;
import com.xust.hotel.common.restful.StatusEnum;
import com.xust.hotel.common.security.CryptUtil;
import com.xust.hotel.common.security.JwtConstantConfig;
import com.xust.hotel.common.security.JwtUtil;
import com.xust.hotel.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author bhj
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * register admin
     */
    @PostMapping("/login/register/admin")
    public Result register(@RequestBody RequestParam<Map<String, String>> requestParam,
                           HttpServletRequest request) throws InnerErrorException {
        String header = request.getHeader(JwtConstantConfig.REGISTER_ADMIN_HEADER_KEY);
        if (StringUtils.isBlank(header)) {
            log.error("register, header is null.");
            return new Result(true, StatusEnum.PARAM_ERROR, null, null);
        }
        if (!header.equals(JwtConstantConfig.REGISTER_ADMIN_HEADER_VALUE)) {
            log.error("register, header error");
            return new Result(true, StatusEnum.PARAM_ERROR, null, null);
        }
        if (requestParam == null || MapUtils.isEmpty(requestParam.getData())) {
            log.error("login, param is null.");
            return new Result(true, StatusEnum.PARAM_ERROR, "param is null", null);
        }
        Map<String, String> data = requestParam.getData();
        String user = data.get("user");
        String password = data.get("password");
        if (StringUtils.isBlank(user) || StringUtils.isBlank(password)) {
            log.error("login, param is null.user={}, password={}", user, password);
            return new Result(true, StatusEnum.PARAM_ERROR, "data error", null);
        }
        if (userService.registerAdmin(user, password)) {
            return new Result(true, StatusEnum.OK, null, null);
        }
        return new Result(true, StatusEnum.ERROR, null, null);
    }

    /**
     * login
     */
    @PostMapping("/login/in")
    public Result login(@RequestBody RequestParam<Map<String, String>> requestParam) throws InnerErrorException {
        if (requestParam == null || MapUtils.isEmpty(requestParam.getData())) {
            log.error("login, param is null.");
            return new Result(true, StatusEnum.PARAM_ERROR, "param is null", null);
        }
        Map<String, String> data = requestParam.getData();
        String user = data.get("user");
        String password = data.get("password");
        String type = data.get("type");
        if (StringUtils.isBlank(user) || StringUtils.isBlank(password) || StringUtils.isBlank(type)) {
            log.error("login, param is null.user={}, password={}, type={}", user, password, type);
            return new Result(true, StatusEnum.PARAM_ERROR, "data error", null);
        }
        if (!type.equals(JwtConstantConfig.USER_ROLE_NORMAL) && !type.equals(JwtConstantConfig.USER_ROLE_ADMIN)) {
            log.error("login, type error.type={}", type);
            return new Result(true, StatusEnum.PARAM_ERROR, "type error", null);
        }
        if (!userService.matchUserToPass(user, password)) {
            log.error("login, match fail.user={}, password={}", user, password);
            return new Result(true, StatusEnum.LOGIN_ERROR, null, null);
        }
        String jwt =
                JwtConstantConfig.HEADER_TOKEN_START + jwtUtil.createJwt(user, password, type) + JwtConstantConfig.HEADER_TOKEN_END;
        jwt = CryptUtil.encrypt(jwt);
        redisTemplate.opsForValue().set(user, jwt, 2, TimeUnit.HOURS);
        Map<String, String> map = Maps.newHashMap();
        map.put("authHeaderKey", JwtConstantConfig.HEADER_KEY);
        map.put("authHeaderValue", jwt);
        map.put("user", user);
        return new Result(true, StatusEnum.OK, null, map);
    }

    @PostMapping("/login/out")
    public Result logout(@RequestBody RequestParam<Map<String, String>> requestParam) throws Exception {
        if (requestParam == null || MapUtils.isEmpty(requestParam.getData())) {
            log.error("login, param is null.");
            return new Result(true, StatusEnum.PARAM_ERROR, "param is null", null);
        }
        Map<String, String> data = requestParam.getData();
        String user = data.get("user");
        String password = data.get("password");
        String type = data.get("type");
        if (StringUtils.isBlank(user) || StringUtils.isBlank(password) || StringUtils.isBlank(type)) {
            log.error("login, param is null.user={}, password={}, type={}", user, password, type);
            return new Result(true, StatusEnum.PARAM_ERROR, "data error", null);
        }
        if (!type.equals(JwtConstantConfig.USER_ROLE_NORMAL) && !type.equals(JwtConstantConfig.USER_ROLE_ADMIN)) {
            log.error("login, type error.type={}", type);
            return new Result(true, StatusEnum.PARAM_ERROR, "type error", null);
        }
        if (!userService.logout(user, password)) {
            log.error("login, match fail.user={}, password={}", user, password);
            return new Result(true, StatusEnum.LOGIN_ERROR, null, null);
        }
        return new Result(true, StatusEnum.OK, null, null);
    }

}
