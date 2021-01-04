package com.xust.hotel.hosing.service.impl;

import com.xust.hotel.acl_pojo.dbo.GuestRoomDO;
import com.xust.hotel.acl_pojo.dbo.RoomInfoDO;
import com.xust.hotel.common.constantAndMapper.UniversalConstant;
import com.xust.hotel.common.exception.*;
import com.xust.hotel.common.restful.Result;
import com.xust.hotel.common.restful.StatusEnum;
import com.xust.hotel.hosing.mapper.GuestRoomMapper;
import com.xust.hotel.hosing.mapper.RoomInfoMapper;
import com.xust.hotel.hosing.service.GuestRoomService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 客房单 服务实现类
 * </p>
 *
 * @author bhj
 * @since 2021-01-03
 */
@Slf4j
@Service
public class GuestRoomServiceImpl implements GuestRoomService {

    @Autowired
    private GuestRoomMapper guestRoomMapper;

    @Autowired
    private RoomInfoMapper roomInfoMapper;

    @Override
    public boolean add(String roomNo, String roomDetail, String roomStatus) throws StatusErrorException, KeyExistException, InnerErrorException, NoSuchKeyException {
        try {
            log.info("add, roomNo={}, roomDetail={}, roomStatus={}", roomNo, roomDetail
                    , roomStatus);
            if (StringUtils.isBlank(roomNo) || StringUtils.isBlank(roomDetail) || StringUtils.isBlank(roomStatus)) {
                log.error("add, param error.roomNo={}, roomDetail={}, roomStatus={}", roomNo, roomDetail
                        , roomStatus);
                return false;
            }
            if (!roomStatus.equals(UniversalConstant.GUEST_ROOM_TABLE_R_STATUS_NO_PERSON)
                    && !roomStatus.equals(UniversalConstant.GUEST_ROOM_TABLE_R_STATUS_PERSON)
                    && !roomStatus.equals(UniversalConstant.GUEST_ROOM_TABLE_R_STATUS_REVERSE)) {
                log.error("add, param error.roomNo={}, roomDetail={}, roomStatus={}", roomNo, roomDetail
                        , roomStatus);
                throw new StatusErrorException("status error.");
            }
            RoomInfoDO roomInfoDO = roomInfoMapper.queryByRoomTypeKey(roomDetail);
            GuestRoomDO guestRoomDO = guestRoomMapper.queryByRoomNo(roomNo);
            if (roomInfoDO == null) {
                log.error("add, this type exist.roomDetail={}", roomDetail);
                throw new NoSuchKeyException("roomDetail didn't exist.");
            }
            if (guestRoomDO != null) {
                log.error("add, this room no exist.guestRoomDO={}", guestRoomDO.toString());
                throw new KeyExistException("room no exist");
            }
            if (guestRoomMapper.insertDynamic(GuestRoomDO.builder()
                    .roomNo(roomNo)
                    .roomDetail(roomDetail)
                    .roomStatus(roomStatus)
                    .phyStatus(UniversalConstant.GUEST_ROOM_TABLE_PHY_STATUS_USING)
                    .build()
            )) {
                return true;
            }
            log.error("add, mapper insert error.");
            return false;
        } catch (NoSuchKeyException e) {
            log.error("add, roomDetail didn't exist.", e);
            throw new NoSuchKeyException("roomDetail exist.");
        } catch (KeyExistException e) {
            log.error("add, room no exist.", e);
            throw new KeyExistException("room no exist");
        } catch (StatusErrorException e) {
            log.error("add, status type error.", e);
            throw new StatusErrorException("status type error.");
        } catch (Exception e) {
            log.error("add occur exception.", e);
            throw new InnerErrorException("add occur exception.");
        }
    }

    @Override
    public boolean modify(String roomNo, String roomDetail) throws NoSuchKeyException, NotChangeException, InnerErrorException {
        try {
            log.info("modify, roomNo={}, roomDetail={}", roomNo, roomDetail);
            if (StringUtils.isBlank(roomNo) || StringUtils.isBlank(roomDetail)) {
                log.error("modify, param error.roomNo={}, roomDetail={}", roomNo, roomDetail);
                return false;
            }
            RoomInfoDO roomInfoDO = roomInfoMapper.queryByRoomTypeKey(roomDetail);
            GuestRoomDO guestRoomDO = guestRoomMapper.queryByRoomNo(roomNo);
            if (roomInfoDO == null) {
                log.error("modify, no such roomDetail..roomDetail={}", roomDetail);
                throw new NoSuchKeyException("no such roomDetail.");
            }
            if (guestRoomDO == null) {
                log.error("modify, no such roomNo.roomNo={}", roomNo);
                throw new NoSuchKeyException("no such roomNo.");
            }
            if (!guestRoomDO.getRoomStatus().equals(UniversalConstant.GUEST_ROOM_TABLE_R_STATUS_NO_PERSON)) {
                log.error("modify, this room hosing.roomNo={}", roomNo);
                throw new NotChangeException("this room hosing.");
            }
            if (guestRoomMapper.updateRoomDetailByRoomNo(roomNo, roomDetail)) {
                return true;
            }
            log.error("modify, mapper insert error.");
            return false;
        } catch (NoSuchKeyException e) {
            log.error("modify, no such roomDetail.", e);
            throw new NoSuchKeyException("no such roomDetail.");
        } catch (NotChangeException e) {
            log.error("modify, this room hosing.", e);
            throw new NotChangeException("this room hosing");
        } catch (Exception e) {
            log.error("modify occur exception.", e);
            throw new InnerErrorException("add occur exception.");
        }
    }
}
