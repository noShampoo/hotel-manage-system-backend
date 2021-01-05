package com.xust.hotel.hosing.service;

import com.xust.hotel.acl_pojo.vo.ReserveVO;
import com.xust.hotel.common.exception.*;

/**
 * <p>
 * 预定信息单 服务类
 * </p>
 *
 * @author bhj
 * @since 2021-01-03
 */
public interface ReserveRoomInfoService {

    /**
     * reserve
     * @param data
     * @return
     */
    boolean reserveRoom(ReserveVO data) throws CustomerInfoException, CanNotReserveException, AccessException, NoSuchKeyException, InnerErrorException;
}
