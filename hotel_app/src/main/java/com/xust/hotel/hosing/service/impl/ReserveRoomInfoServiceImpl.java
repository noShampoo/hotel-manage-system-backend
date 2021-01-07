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
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

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

    @Transactional(rollbackFor = MapperErrorException.class)
    @Override
    public String reserveRoom(ReserveVO data) throws CustomerInfoException, CanNotReserveException, AccessException, NoSuchKeyException, InnerErrorException, MapperErrorException {
        try {
            log.info("reserveRoom, data={}", data);
            if (StringUtils.isBlank(data.getRoomNo()) || data.getReserveDay() == null || data.getReserveDay() < 1
                    || StringUtils.isBlank(data.getReserveTime()) || CollectionUtils.isEmpty(data.getCustomerInfo())) {
                log.error("reserveRoom, param error.data={}", data.toString());
                return null;
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
                return null;
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

                boolean flagGuestRoom = guestRoomMapper.updateGuestRoomAndOrderNoStatusByRoomNo(
                                UniversalConstant.GUEST_ROOM_TABLE_R_STATUS_RESERVE, orderNo, data.getRoomNo()
                );
                log.info("reserveRoom, flagGuestRoom={}", flagGuestRoom);

                boolean flagRecord = hosingRecordMapper.addRecordDynamic(hosingRecordDO);
                log.info("reserveRoom, flagRecord={}", flagRecord);
                if (flagGuestRoom && flagRecord && flagReserve) {
                    return orderNo;
                }
                log.error("mapper reserve error.");
                throw new MapperErrorException("mapper reserve error.");
            } catch (Exception e) {
                log.error("mapper reserve error.");
                throw new MapperErrorException("mapper reserve error.");
            }
        } catch (MapperErrorException e) {
            log.error("reserveRoom, mapper reserve error.");
            throw new MapperErrorException("mapper reserve error.");
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

    @Transactional(rollbackFor = MapperErrorException.class)
    @Override
    public boolean chancelRoom(ReserveVO data) throws NoSuchFieldException, ChancelReserveErrorException, BizInfoErrorException, MapperErrorException {
        try {
            log.info("chancelRoom, data={}", data.toString());
            if (StringUtils.isBlank(data.getOrderNo()) || StringUtils.isBlank(data.getRoomNo())) {
                return false;
            }
            GuestRoomDO guestRoomDO = guestRoomMapper.queryByRoomNo(data.getRoomNo());
            if (guestRoomDO == null) {
                log.error("chancelRoom, this roomNo isn't exist.");
                throw new NoSuchKeyException("no such roomNo");
            }
            if (guestRoomDO.getPhyStatus().equals(UniversalConstant.GUEST_ROOM_TABLE_PHY_STATUS_NOT_USING)) {
                log.error("chancelRoom, this room not using.");
                throw new NoSuchKeyException("this room not using");
            }
            log.info("chancelRoom, guestRoomDO={}", guestRoomDO.toString());
            if (!guestRoomDO.getRoomStatus().equals(UniversalConstant.GUEST_ROOM_TABLE_R_STATUS_RESERVE)
                    || StringUtils.isBlank(guestRoomDO.getOrderNo())) {
                log.error("chancelRoom, this room isn't reserved room");
                throw new ChancelReserveErrorException("can't reserve.");
            }
            if (!data.getOrderNo().equals(guestRoomDO.getOrderNo())) {
                log.error("chancelRoom, this room is reserved by another order.orderNo differently");
                throw new NoSuchKeyException("orderNo differently");
            }
            ReserveRoomInfoDO reserveRoomInfoDO = reserveRoomInfoMapper.queryByOrderNo(data.getOrderNo());
            if (!data.getRoomNo().equals(reserveRoomInfoDO.getReserveRoomNo())
                    || !UniversalConstant.ROOM_OPERATE_STATUS_RESERVE.equals(reserveRoomInfoDO.getReserveStatus())) {
                log.error("chancelRoom, biz info error.roomNo={}, reserveStatus={}", reserveRoomInfoDO.getReserveRoomNo(), reserveRoomInfoDO.getReserveStatus());
                log.error("chancelRoom, biz info error.reserveRoomInfoDo={}", reserveRoomInfoDO.toString());
                throw new BizInfoErrorException("biz info error");
            }
            String header = request.getHeader(JwtConstantConfig.HEADER_KEY);
            String token = CryptUtil.decrypt(header);
            if (StringUtils.isBlank(token)) {
                log.error("chancelRoom, token is null");
                return false;
            }
            token = token.substring(JwtConstantConfig.SUB_START_NUM);
            token = token.substring(0, token.length() - JwtConstantConfig.SUB_END_NUM);
            String operateCp = jwtUtil.getLoginUserDTO(token).getUser();
            String operateTime = new Date(System.currentTimeMillis()).toString();

            try {
                boolean b = reserveRoomInfoMapper.updateReserveInfo(data.getOrderNo(), UniversalConstant.ROOM_OPERATE_STATUS_CHANCEL, operateCp, operateTime);
                boolean b1 = guestRoomMapper.updateGuestRoomAndOrderNoStatusByRoomNo(UniversalConstant.GUEST_ROOM_TABLE_R_STATUS_NO_PERSON, null, data.getRoomNo());
                if (b && b1) {
                    return true;
                }
                log.error("chancel, mapper chancel error.");
                throw new MapperErrorException("chancel error");
            } catch (Exception e) {
                log.error("chancel error", e);
                throw new MapperErrorException("chancel error");
            }
        } catch (MapperErrorException e) {
            log.error("chancelRoom, chancel error");
            throw new MapperErrorException("chancel error");
        } catch (NoSuchKeyException e) {
            log.error("chancelRoom, no such key.", e);
            throw new NoSuchFieldException("no such key");
        } catch (ChancelReserveErrorException e) {
            log.error("chancelRoom, chancel error.");
            throw new ChancelReserveErrorException("chancel error");
        } catch (BizInfoErrorException e) {
            log.error("chancelRoom, biz info error.", e);
            throw new BizInfoErrorException("biz info error");
        }
    }




}
