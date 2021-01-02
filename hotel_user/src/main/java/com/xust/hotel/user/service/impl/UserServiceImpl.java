package com.xust.hotel.user.service.impl;

import com.xust.hotel.common.UniversalConstant;
import com.xust.hotel.common.exception.InnerErrorException;
import com.xust.hotel.common.exception.UserNotFoundException;
import com.xust.hotel.common.security.CodingUtil;
import com.xust.hotel.common.security.CryptUtil;
import com.xust.hotel.user.mapper.UserMapper;
import com.xust.hotel.user.pojo.UserDO;
import com.xust.hotel.user.pojo.UserDTO;
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
            throw new InnerErrorException("matchUserToPass occur exception.", e);
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
            throw new InnerErrorException("registerAdmin occur exception", e);
        }
    }

    @Override
    public boolean logout(String user, String password) throws Exception {
        try {
            if (StringUtils.isBlank(user) || StringUtils.isBlank(password)) {
                log.error("logout, param error.user={}, password={}", user, password);
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
            log.error("logout occur exception");
            throw new InnerErrorException("logout occur exception", e);
        }
    }

    @Override
    public UserDTO createUser(String name, String password, String type) throws InnerErrorException {
        try {
            if (StringUtils.isBlank(name) || StringUtils.isBlank(password) || StringUtils.isBlank(type)) {
                log.error("createUser, param error.name={}, password={}, type={}", name, password, type);
                return null;
            }
            String user = CodingUtil.generateUser(name);
            UserDO userDO = UserDO.builder()
                    .user(user)
                    .name(name)
                    .password(CryptUtil.encrypt(password))
                    .type(UniversalConstant.USER_TABLE_TYPE_NORMAL)
                    .status(UniversalConstant.USER_TABLE_STATUS_USING)
                    .build();
            int i = userMapper.insertDynamic(userDO);
            if (i != 1) {
                log.error("createUser, mapper insert error.userDO={}", userDO.toString());
                return null;
            }
            return UserDTO.builder()
                    .user(userDO.getUser())
                    .name(userDO.getName())
                    .password(CryptUtil.decrypt(userDO.getPassword()))
                    .type(userDO.getType())
                    .build();
        } catch (Exception e) {
            log.error("createUser occur exception.");
            throw new InnerErrorException("createUser occur exception.", e);
        }
    }

    @Override
    public UserDTO modifyUserInfo(String user, String name, String password) throws InnerErrorException {
        try {
            if (StringUtils.isBlank(user) || StringUtils.isBlank(name) || StringUtils.isBlank(password)) {
                log.error("modifyUserInfo, param error.user={}, name={}, password={}", user, name, password);
                return null;
            }
            UserDO queryUser = userMapper.selectByUser(user);
            if (queryUser == null || queryUser.getId() == null) {
                log.error("modifyUserInfo, query result is null.user={}", user);
                return null;
            }
            UserDO userDO = UserDO.builder()
                    .id(queryUser.getId())
                    .name(name)
                    .password(CryptUtil.encrypt(password))
                    .build();
            int i = userMapper.updateDynamic(userDO);
            if (i != 1) {
                log.error("modifyUserInfo, mapper update error.");
                return null;
            }
            return UserDTO.builder()
                    .user(user)
                    .name(name)
                    .password(CryptUtil.decrypt(userDO.getPassword()))
                    .build();
        } catch (Exception e) {
            log.error("modifyUserInfo occur exception.");
            throw new InnerErrorException("modifyUserI" +
                    "nfo occur exception.");
        }
    }

    @Override
    public boolean deleteUser(String user) throws InnerErrorException {
        try {
            if (StringUtils.isBlank(user)) {
                log.error("deleteUser, user is null.");
                return false;
            }
            UserDO queryUser = userMapper.selectByUser(user);
            if (queryUser == null || queryUser.getId() == null) {
                log.error("deleteUser, query result is null.user={}", user);
                return false;
            }
            UserDO userDO = UserDO.builder()
                    .id(queryUser.getId())
                    .status(UniversalConstant.USER_TABLE_STATUS_NO_USING)
                    .build();
            int i = userMapper.updateDynamic(userDO);
            if (i != 1) {
                log.error("deleteUser, mapper delete error.");
                return false;
            }
            return true;
        } catch (Exception e) {
            log.error("deleteUser occur exception.");
            throw new InnerErrorException("deleteUser occur exception.", e);
        }
    }
}
