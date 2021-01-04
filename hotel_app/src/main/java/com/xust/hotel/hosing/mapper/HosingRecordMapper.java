package com.xust.hotel.hosing.mapper;

import com.xust.hotel.acl_pojo.dbo.HosingRecordDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
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

    @Select(value = "select * from hosing_record")
    List<HosingRecordDO> queryAll();

}
