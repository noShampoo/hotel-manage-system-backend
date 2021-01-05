package com.xust.hotel.hosing.mapper;

import com.xust.hotel.acl_pojo.dbo.HosingRecordDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 操作轨迹单 Mapper 接口
 * </p>
 *
 * @author bhj
 * @since 2021-01-03
 */
@Mapper
@Repository
public interface HosingRecordMapper {


    /**
     * add
     * @param hosingRecordDO
     * @return
     */
    @Insert("insert into hosing_record(order_no, customer_info, operate_time, operate_event, operate_obj, " +
            "operate_cp, feature) values(#{orderNo}, #{customerInfo}, #{operateTime}, #{operateEvent}, " +
            "#{operateObj}, #{operateCp}, #{feature})")
    boolean addRecordDynamic(HosingRecordDO hosingRecordDO);

    /**
     * delete
     * @param orderNo
     * @return
     */
    @Delete("delete from hosing_record where order_no = #{orderNo}")
    int deleteByOrderNo(String orderNo);
}
