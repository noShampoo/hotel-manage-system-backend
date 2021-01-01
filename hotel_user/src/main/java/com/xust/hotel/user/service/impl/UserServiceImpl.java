package com.xust.hotel.user.service.impl;

import com.xust.hotel.common.UniversalConstant;
import com.xust.hotel.common.exception.ExpiredException;
import com.xust.hotel.common.exception.InnerErrorException;
import com.xust.hotel.user.mapper.UserMapper;
import com.xust.hotel.user.pojo.UserDO;
import com.xust.hotel.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author bhj
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean matchUserToPass(String user, String password) throws InnerErrorException {
        try {
            if (StringUtils.isBlank(user) || StringUtils.isBlank(password)) {
                log.error("matchUserToPass, param error.user={}, password={}", user, password);
                return false;
            }
            UserDO userDO = userMapper.selectByUser(user);
            if (userDO == null) {
                log.error("matchUserToPass, query result is null.");
                return false;
            }
            if (StringUtils.isBlank(userDO.getPassword())) {
                log.error("matchUserToPass, query result password is null.");
                return false;
            }
            return bCryptPasswordEncoder.matches(password, userDO.getPassword());
        } catch (Exception e) {
            log.error("matchUserToPass occur exception.");
            throw new InnerErrorException(e);
        }
    }

    @Override
    public boolean registerAdmin(String user, String password) throws InnerErrorException {
        try {
            if (StringUtils.isBlank(user) || StringUtils.isBlank(password)) {
                log.error("registerAdmin, param error.user={}, password={}", user, password);
                return false;
            }
            UserDO userDO = UserDO.builder()
                    .user(user)
                    .name(UniversalConstant.ADMIN_USER_NAME)
                    .password(bCryptPasswordEncoder.encode(password))
                    .status(UniversalConstant.USER_TABLE_STATUS_USING)
                    .type(UniversalConstant.USER_TABLE_TYPE_ADMIN)
                    .build();
            int i = userMapper.insertDynamic(userDO);
            return i == 1;
        } catch (Exception e) {
            log.error("registerAdmin occur exception");
            throw new InnerErrorException(e);
        }
    }

    @Override
    public boolean logout(String user, String password) throws Exception {
        try {
            if (StringUtils.isBlank(user) || StringUtils.isBlank(password)) {
                log.error("registerAdmin, param error.user={}, password={}", user, password);
                return false;
            }
            if (!matchUserToPass(user, password)) {
                log.error("logout, match fail.user={}, password={}", user, password);
                return false;
            }
            if (StringUtils.isBlank(redisTemplate.opsForValue().get(user))) {
                return true;
            }
            return redisTemplate.delete(user);
        } catch (Exception e) {
            log.error("registerAdmin occur exception");
            throw new InnerErrorException(e);
        }
    }
}
