package com.xust.hotel.user.service;

import com.xust.hotel.common.exception.InnerErrorException;

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
}
