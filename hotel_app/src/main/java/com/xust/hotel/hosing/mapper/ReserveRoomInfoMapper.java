package com.xust.hotel.hosing.mapper;

import com.xust.hotel.acl_pojo.dbo.ReserveRoomInfoDO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

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


    /**
     * queryByOrderNo
     * @param orderNo
     * @return
     */
    @Select("select * from reserve_room_info where order_no = #{orderNo}")
    ReserveRoomInfoDO queryByOrderNo(String orderNo);

    @Select("select * from reserve_room_info where reserve_room_no = #{roomNo} and reserve_status = #{status}")
    ReserveRoomInfoDO queryByRoomNoAndStatus(String roomNo, String status);

    /**
     * query by status
     * @param status
     * @return
     */
    @Select("select * from reserve_room_info where reserve_status=#{status}")
    List<ReserveRoomInfoDO> queryByStatus(String status);

    /**
     * update reserve info
     */
    @Update("update reserve_room_info set reserve_status = #{reserve_status}, " +
            "operate_cp = #{operate_cp}, operate_time = #{operate_time} where order_no = #{orderNo}")
    boolean updateReserveInfo(String orderNo, String reserve_status, String operate_cp, String operate_time);
}
