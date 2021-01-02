package com.xust.hotel.user.service;

import com.xust.hotel.common.exception.InnerErrorException;
import com.xust.hotel.user.pojo.UserDTO;

/**
 * @author bhj
 */
public interface UserService {

    /**
     * match
     * @param user
     * @param password
     * @return
     * @throws InnerErrorException
     */
    public boolean matchUserToPass(String user, String password) throws InnerErrorException;

    /**
     * register admin user
     * @param user
     * @param password
     * @throws InnerErrorException
     * @return
     */
    public boolean registerAdmin(String user, String password) throws InnerErrorException;

    /**
     * log out
     * @param user
     * @param password
     * @throws Exception
     * @return
     */
    public boolean logout(String user, String password) throws Exception;

    /**
     * create user
     * @param name
     * @param password
     * @param type
     * @return
     * @throws InnerErrorException
     */
    public UserDTO createUser(String name, String password, String type) throws InnerErrorException;
}
