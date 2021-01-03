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
}
