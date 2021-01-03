package com.xust.hotel.hosing.mapper;

import com.xust.hotel.acl_pojo.dbo.RoomInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 客房详情单 Mapper 接口
 * </p>
 *
 * @author bhj
 * @since 2021-01-03
 */
@Mapper
@Repository
public interface RoomInfoMapper {

    /**
     * save dynamic
     * @param roomInfoDO
     * @return
     */
    boolean saveDynamic(RoomInfoDO roomInfoDO);

    /**
     * query by key
     * @param roomTypeKey
     * @return
     */
    RoomInfoDO queryByRoomTypeKey(String roomTypeKey);

    /**
     * query by type
     * @param roomType
     * @return
     */
    RoomInfoDO queryByRoomType(String roomType);

    /**
     * query all
     * @return
     */
    List<RoomInfoDO> queryAll();

    /**
     * delete
     * @param roomType
     * @return
     */
    boolean deleteByRoomType(String roomType);

    /**
     * delete room
     * @param roomTypeKey
     * @return
     */
    boolean deleteByRoomTypeKey(String roomTypeKey);

    /**
     * update by room key
     * @param roomInfoDO
     * @return
     */
    boolean updateByRoomTypeKey(RoomInfoDO roomInfoDO);
}
