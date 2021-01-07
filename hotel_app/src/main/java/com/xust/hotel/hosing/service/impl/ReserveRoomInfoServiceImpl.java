package com.xust.hotel.hosing.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.util.StringUtil;
import com.xust.hotel.acl_pojo.dbo.GuestRoomDO;
import com.xust.hotel.acl_pojo.dbo.HosingRecordDO;
import com.xust.hotel.acl_pojo.dbo.ReserveRoomInfoDO;
import com.xust.hotel.acl_pojo.vo.ReserveVO;
import com.xust.hotel.common.constantAndMapper.UniversalConstant;
import com.xust.hotel.common.exception.*;
import com.xust.hotel.common.security.CodingUtil;
import com.xust.hotel.common.security.CryptUtil;
import com.xust.hotel.common.security.JwtConstantConfig;
import com.xust.hotel.common.security.JwtUtil;
import com.xust.hotel.utils.UniversalUtil;
import com.xust.hotel.hosing.mapper.GuestRoomMapper;
import com.xust.hotel.hosing.mapper.HosingRecordMapper;
import com.xust.hotel.hosing.mapper.ReserveRoomInfoMapper;
import com.xust.hotel.hosing.service.ReserveRoomInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;

/**
 * <p>
 * 预定信息单 服务实现类
 * </p>
 *
 * @author bhj
 * @since 2021-01-03
 */
@Slf4j
@Service
public class ReserveRoomInfoServiceImpl implements ReserveRoomInfoService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private GuestRoomMapper guestRoomMapper;

    @Autowired
    private ReserveRoomInfoMapper reserveRoomInfoMapper;

    @Autowired
    private HosingRecordMapper hosingRecordMapper;

    @Override
    public boolean reserveRoom(ReserveVO data) throws CustomerInfoException, CanNotReserveException, AccessException, NoSuchKeyException, InnerErrorException {
        try {
            log.info("reserveRoom, data={}", data);
            if (StringUtils.isBlank(data.getRoomNo()) || data.getReserveDay() == null || data.getReserveDay() < 1
                    || StringUtils.isBlank(data.getReserveTime()) || CollectionUtils.isEmpty(data.getCustomerInfo())) {
                log.error("reserveRoom, param error.data={}", data.toString());
                return false;
            }
            if (!UniversalUtil.checkCustomerInfoList(data.getCustomerInfo())) {
                log.error("reserveRoom, customerInfo error.");
                throw new CustomerInfoException("customerInfo error.");
            }
            GuestRoomDO guestRoomDO = guestRoomMapper.queryByRoomNo(data.getRoomNo());
            if (guestRoomDO == null) {
                log.error("reserveRoom, this roomNo isn't exist.");
                throw new NoSuchKeyException("no such roomNo");
            }
            if (guestRoomDO.getPhyStatus().equals(UniversalConstant.GUEST_ROOM_TABLE_PHY_STATUS_NOT_USING)) {
                log.error("reserveRoom, this room not using.");
                throw new NoSuchKeyException("this room not using");
            }
            log.info("reserveRoom, guestRoomDO={}", guestRoomDO.toString());

            if (!guestRoomDO.getRoomStatus().equals(UniversalConstant.GUEST_ROOM_TABLE_R_STATUS_NO_PERSON)
                    || StringUtils.isNotBlank(guestRoomDO.getOrderNo())) {
                log.error("reserveRoom, can't reserve.");
                throw new CanNotReserveException("can't reserve.");
            }
            String header = request.getHeader(JwtConstantConfig.HEADER_KEY);
            String token = CryptUtil.decrypt(header);
            if (StringUtils.isBlank(token)) {
                log.error("resereveRoom, token is null");
                return false;
            }
            token = token.substring(JwtConstantConfig.SUB_START_NUM);
            token = token.substring(0, token.length() - JwtConstantConfig.SUB_END_NUM);
            String operateCp = jwtUtil.getLoginUserDTO(token).getUser();
            String operateTime = new Date(System.currentTimeMillis()).toString();
            String orderNo = CodingUtil.generateOrderNo();
            if (StringUtil.isEmpty(operateCp) || StringUtils.isBlank(operateTime) || StringUtils.isBlank(orderNo)) {
                log.error("reserveRoom, operateCp={}. operateTime={}, operateNo={}", operateCp, operateTime
                        , orderNo);
                throw new AccessException("cp error.");
            }
            ReserveRoomInfoDO reserveRoomInfoDO = ReserveRoomInfoDO.builder()
                    .reserveRoomNo(data.getRoomNo())
                    .reserveDay(data.getReserveDay())
                    .reserveTime(data.getReserveTime())
                    .customerInfo(JSONObject.toJSONString(data.getCustomerInfo()))
                    .reserveStatus(UniversalConstant.ROOM_OPERATE_STATUS_RESERVE)
                    .operateCp(operateCp)
                    .operateTime(operateTime)
                    .orderNo(orderNo)
                    .build();
            HosingRecordDO hosingRecordDO = HosingRecordDO.builder()
                    .orderNo(orderNo)
                    .customerInfo(JSONObject.toJSONString(data.getCustomerInfo()))
                    .operateCp(operateCp)
                    .operateEvent(UniversalConstant.ROOM_OPERATE_STATUS_RESERVE)
                    .operateObj(data.getRoomNo())
                    .operateTime(operateTime)
                    .feature(UniversalConstant.HOSING_RECORD_FEATURE_OPERATE_CONTENT + ":" + data.getReserveTime())
                    .build();

            /*
             * ！！！！！！！！！如果时间充足一定要优化成事务，这种方法只能暂时使用
             */
            try {
                boolean flagReserve = reserveRoomInfoMapper.insertDynamic(reserveRoomInfoDO);
                log.info("reserveRoom, flagReserve={}", flagReserve);
                if (!flagReserve) {
                    throw new CanNotReserveException("reserveRoomInfoMapper error");
                }
                boolean flagGuestRoom = guestRoomMapper.updateGuestRoomAndOrderNoStatusByRoomNo(
                                UniversalConstant.GUEST_ROOM_TABLE_R_STATUS_REVERSE, orderNo, data.getRoomNo()
                );
                log.info("reserveRoom, flagGuestRoom={}", flagGuestRoom);
                if (!flagGuestRoom) {
                    throw new CanNotReserveException("guestRoomMapper error");
                }
                boolean flagRecord = hosingRecordMapper.addRecordDynamic(hosingRecordDO);
                log.info("reserveRoom, flagRecord={}", flagRecord);
                if (!flagRecord) {
                    throw new CanNotReserveException("hosingRecordMapper error");
                }

                return true;
            } catch (Exception e) {
                for (int i = 0; i < 5; i++) {
                    if (reserveRoomInfoMapper.deleteByRoomNo(data.getRoomNo())
                            && guestRoomMapper.updateGuestRoomAndOrderNoStatusByRoomNo(
                            UniversalConstant.GUEST_ROOM_TABLE_R_STATUS_NO_PERSON, null,
                            data.getRoomNo())) {
                        log.error("rollback, i={}", i);
                        break;
                    }
                }
                log.error("reserveRoom, mapper error. cann't reserve room.data={}", data);
                throw new CanNotReserveException("reserve error.");
            }
        } catch (NoSuchKeyException e) {
            log.error("reserveRoom, no such roomNo.");
            throw new NoSuchKeyException("no such roomNo");
        } catch (AccessException e) {
            log.error("reserveRoom, cp error.", e);
            throw new AccessException("cp, error");
        } catch (CanNotReserveException e) {
            log.error("reserveRoom, mapper error. cann't reserve room.data={}", data);
            throw new CanNotReserveException("reserve error.");
        } catch (CustomerInfoException e) {
            log.error("reserveRoom, customerInfo error.", e);
            throw new CustomerInfoException("customerInfo error.");
        } catch (Exception e) {
            log.error("reserveRoom occur exception.", e);
            throw new InnerErrorException("reserveRoom.");
        }
    }


}
