package com.xust.hotel.hosing.controller;

import com.xust.hotel.acl_pojo.vo.ReserveVO;
import com.xust.hotel.common.exception.*;
import com.xust.hotel.common.restful.RequestParam;
import com.xust.hotel.common.restful.Result;
import com.xust.hotel.common.restful.StatusEnum;
import com.xust.hotel.common.security.AccessUtil;
import com.xust.hotel.common.security.JwtConstantConfig;
import com.xust.hotel.hosing.service.HosingRecordService;
import com.xust.hotel.hosing.service.ReserveRoomInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author bhj
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = "/reserve")
public class ReserveController {

    @Autowired
    private ReserveRoomInfoService reserveRoomInfoService;

    @Autowired
    private HosingRecordService hosingRecordService;

    @PostMapping("/room")
    public Result reserveRoom(@RequestBody RequestParam<ReserveVO> requestParam,
                              HttpServletRequest request) throws InnerErrorException, NoSuchKeyException, CanNotReserveException, CustomerInfoException, AccessException, MapperErrorException {
        if (requestParam == null || requestParam.getData() == null) {
            log.error("reserveRoom, param error.");
            return new Result(true, StatusEnum.PARAM_ERROR, "param error", null);
        }
        if (!AccessUtil.checkAccess(request, JwtConstantConfig.USER_ROLE_ADMIN,
                JwtConstantConfig.USER_ROLE_NORMAL)) {
            log.error("reserveRoom, access error.");
            return new Result(true, StatusEnum.ACCESS_ERROR, null, null);
        }
        ReserveVO data = requestParam.getData();
        if (StringUtils.isBlank(data.getRoomNo()) || data.getReserveDay() == null || data.getReserveDay() < 1
            || StringUtils.isBlank(data.getReserveTime()) || CollectionUtils.isEmpty(data.getCustomerInfo())) {
            log.error("reserveRoom, param error.data={}", data.toString());
            return new Result(true, StatusEnum.PARAM_ERROR, "param's data={}" + data.toString(), null);
        }
        String orderNO = reserveRoomInfoService.reserveRoom(data);
        if (StringUtils.isNotBlank(orderNO)) {
            return new Result(true, StatusEnum.OK, null, orderNO);
        }
        log.error("reserveRoom, service reserve error.");
        return new Result(true, StatusEnum.ERROR, null, null);
    }

    @PostMapping("/chancel")
    public Result chancelRoom(@RequestBody RequestParam<ReserveVO> requestParam,
                              HttpServletRequest request) throws InnerErrorException, ChancelReserveErrorException, MapperErrorException, BizInfoErrorException, NoSuchFieldException {
        if (requestParam == null || requestParam.getData() == null) {
            log.error("reserveRoom, param error.");
            return new Result(true, StatusEnum.PARAM_ERROR, "param error", null);
        }
        if (!AccessUtil.checkAccess(request, JwtConstantConfig.USER_ROLE_ADMIN,
                JwtConstantConfig.USER_ROLE_NORMAL)) {
            log.error("reserveRoom, access error.");
            return new Result(true, StatusEnum.ACCESS_ERROR, null, null);
        }
        ReserveVO data = requestParam.getData();
        if (StringUtils.isBlank(data.getRoomNo()) || StringUtils.isBlank(data.getOrderNo())) {
            log.error("reserveRoom, param error.data={}", data.toString());
            return new Result(true, StatusEnum.PARAM_ERROR, "param's data={}" + data.toString(), null);
        }
        if (reserveRoomInfoService.chancelRoom(data)) {
            return new Result(true, StatusEnum.OK, null, null);
        }
        log.error("reserveRoom, service reserve error.");
        return new Result(true, StatusEnum.ERROR, null, null);
    }

}
