package com.xust.hotel.hosing.service;

import com.xust.hotel.acl_pojo.dbo.RoomInfoDO;
import com.xust.hotel.acl_pojo.vo.RoomDetailVO;
import com.xust.hotel.common.exception.InnerErrorException;

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
    boolean saveDynamic(RoomDetailVO data) throws InnerErrorException;
}
