package com.xust.hotel.hosing.service.impl;

import com.xust.hotel.acl_pojo.dbo.RoomInfoDO;
import com.xust.hotel.acl_pojo.vo.RoomDetailVO;
import com.xust.hotel.common.constantAndMapper.UniversalConstant;
import com.xust.hotel.common.exception.InnerErrorException;
import com.xust.hotel.hosing.mapper.RoomInfoMapper;
import com.xust.hotel.hosing.service.RoomInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 客房详情单 服务实现类
 * </p>
 *
 * @author bhj
 * @since 2021-01-03
 */
@Slf4j
@Service
public class RoomInfoServiceImpl implements RoomInfoService {

    @Autowired
    private RoomInfoMapper roomInfoMapper;

    @Override
    public boolean saveDynamic(RoomDetailVO data) throws InnerErrorException {
        try {
            log.info("add, data={}", data.toString());
            if (StringUtils.isBlank(data.getRoomKey())) {
                log.error("add, roomKey is null. data={}", data.toString());
                return false;
            }
            if (StringUtils.isBlank(data.getRoomType())) {
                log.error("add, roomType is null. data={}", data.toString());
                return false;
            }
            if (data.getRoomPrice() == null) {
                log.error("add, roomPrice is null, data={}", data.toString());
                return false;
            }
            return 1 == roomInfoMapper.saveDynamic(convertVO2DO(data));
        } catch (Exception e) {
            log.error("add occur exception.");
            throw new InnerErrorException("add occur exception.", e);
        }
    }


    /**
     * convert
     */
    private RoomInfoDO convertVO2DO(RoomDetailVO roomDetailVO) {
        return RoomInfoDO.builder()
                .roomTypeKey(roomDetailVO.getRoomKey())
                .roomType(roomDetailVO.getRoomType())
                .roomPrice(roomDetailVO.getRoomPrice())
                .priceUnit(UniversalConstant.DEFAULT_ROOM_PRICE_UNIT)
                .build();
    }
}
