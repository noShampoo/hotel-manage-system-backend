package com.xust.hotel.hosing.mapper;

import com.xust.hotel.acl_pojo.dbo.ReserveRoomInfoDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 预定信息单 Mapper 接口
 * </p>
 *
 * @author bhj
 * @since 2021-01-03
 */
@Mapper
@Repository
public interface ReserveRoomInfoMapper {

    /**
     * insert
     * @param reserveRoomInfoDO
     * @return
     */
    @Insert("insert into reserve_room_info(reserve_room_no, reserve_day, reserve_time, customer_info, " +
            "reserve_status, operate_cp, operate_time, order_no) values(#{reserveRoomNo}, #{reserveDay}, " +
            "#{reserveTime}, #{customerInfo}, #{reserveStatus}, #{operateCp}, #{operateTime}, #{orderNo})")
    public boolean insertDynamic(ReserveRoomInfoDO reserveRoomInfoDO);


    /**
     * delete
     * @param roomNo
     * @return
     */
    @Delete("delete from reserve_room_info where reserve_room_no = #{roomNo}")
    boolean deleteByRoomNo(String roomNo);
}
