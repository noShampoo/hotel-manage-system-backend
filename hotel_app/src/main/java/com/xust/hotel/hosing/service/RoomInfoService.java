package com.xust.hotel.hosing.service;

import com.xust.hotel.acl_pojo.dbo.RoomInfoDO;
import com.xust.hotel.acl_pojo.vo.RoomDetailVO;
import com.xust.hotel.common.exception.InnerErrorException;
import com.xust.hotel.common.exception.KeyExistException;
import com.xust.hotel.common.exception.NoSuchKeyException;
import com.xust.hotel.common.exception.NotDeleteException;

import java.util.List;

/**
 * <p>
 * 客房详情单 服务类
 * </p>
 *
 * @author bhj
 * @since 2021-01-03
 */
public interface RoomInfoService {

    /**
     * save
     * @param data
     * @return
     */
    boolean saveDynamic(RoomDetailVO data) throws InnerErrorException, KeyExistException;

    /**
     * modify
     * @param data
     * @return
     */
    RoomDetailVO modifyDynamic(RoomDetailVO data) throws InnerErrorException, NoSuchKeyException, KeyExistException;

    /**
     * delete
     * @param roomKey
     * @return
     */
    boolean delete(String roomKey) throws NotDeleteException, NoSuchKeyException, InnerErrorException;

    /**
     * query
     * @param page
     * @param size
     * @return
     * @throws InnerErrorException
     */
    List<RoomDetailVO> query(int page, int size) throws InnerErrorException;

    /**
     * get some
     * @param roomKey
     * @param roomType
     * @return
     * @throws InnerErrorException
     */
    RoomDetailVO queryByRoomKeyOrRoomType(String roomKey, String roomType) throws InnerErrorException;
}
