package com.xust.hotel.hosing.service;

import com.xust.hotel.common.exception.*;

/**
 * <p>
 * 客房单 服务类
 * </p>
 *
 * @author bhj
 * @since 2021-01-03
 */
public interface GuestRoomService {

    /**
     * add
     * @param roomNo
     * @param roomDetail
     * @param roomStatus
     * @return
     */
    boolean add(String roomNo, String roomDetail, String roomStatus) throws StatusErrorException, KeyExistException, InnerErrorException, NoSuchKeyException;

    /**
     * modify
     * @param roomNo
     * @param roomDetail
     * @return
     */
    boolean modify(String roomNo, String roomDetail) throws NoSuchKeyException, NotChangeException, InnerErrorException;

    /**
     * delete
     * @param roomNo
     * @return
     */
    boolean delete(String roomNo) throws NoSuchKeyException, KeyExistException, StatusErrorException, InnerErrorException, NotChangeException;
}
