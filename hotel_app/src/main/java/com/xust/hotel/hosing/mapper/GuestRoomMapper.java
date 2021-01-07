package com.xust.hotel.hosing.mapper;

import com.xust.hotel.acl_pojo.dbo.GuestRoomDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 客房单 Mapper 接口
 * </p>
 *
 * @author bhj
 * @since 2021-01-03
 */
@Mapper
@Repository
public interface GuestRoomMapper {

    /**
     * add
     * @param guestRoomDO
     * @return
     */
    boolean insertDynamic(GuestRoomDO guestRoomDO);

    /**
     * query
     * @param roomTypeKey
     * @return
     */
    int countNotNoPersonByRoomTypeKey(String roomTypeKey);

    /**
     * update by detail
     * @param roomTypeKey
     * @param phyStatus
     * @return
     */
    int updatePhyStatusByRoomDetail(String roomTypeKey, String phyStatus);

    /**
     * update
     * @param roomNo
     * @param roomDetail
     * @return
     */
    boolean updateRoomDetailByRoomNo(String roomNo, String roomDetail);

    /**
     * update
     * @param roomNo
     * @param phyStatus
     * @return
     */
    boolean updatePhyStatusByRoomNo(String roomNo, String phyStatus);

    /**
     * query
     * @param roomDetail
     * @return
     */
    List<GuestRoomDO> queryByRoomDetail(String roomDetail);

    /**
     * query
     * @param roomNo
     * @return
     */
    GuestRoomDO queryByRoomNo(String roomNo);

    /**
     * query all
     * @return
     */
    List<GuestRoomDO> queryAll();

    /**
     * update
     * @param guestStatus
     * @param orderNo
     * @return
     */
    boolean updateGuestRoomAndOrderNoStatusByRoomNo(String guestStatus, String orderNo, String roomNo);

    /**
     * count
     * @param roomTypeKey
     * @return
     */
    int countByRoomTypeKey(String roomTypeKey);
}
