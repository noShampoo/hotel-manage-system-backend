package com.xust.hotel.hosing.controller;

import com.xust.hotel.common.exception.InnerErrorException;
import com.xust.hotel.common.restful.RequestParam;
import com.xust.hotel.common.restful.Result;
import com.xust.hotel.common.restful.StatusEnum;
import com.xust.hotel.common.security.AccessUtil;
import com.xust.hotel.common.security.JwtConstantConfig;
import com.xust.hotel.acl_pojo.vo.RoomDetailVO;
import com.xust.hotel.hosing.service.RoomInfoService;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping(value = "/home")
public class RoomController {


    @Autowired
    private RoomInfoService roomInfoService;

    /**
     * add room type
     */
    @PostMapping("/detail/add")
    public Result addDetail(@RequestBody RequestParam<RoomDetailVO> requestParam,
                            HttpServletRequest request) throws InnerErrorException {
        if (requestParam == null || requestParam.getData() == null) {
            log.error("addDetail, param is null.");
            return new Result(true, StatusEnum.OK, "param is null", null);
        }
        RoomDetailVO data = requestParam.getData();
        if (StringUtils.isBlank(data.getRoomKey()) || StringUtils.isBlank(data.getRoomType())
            || data.getRoomPrice() == null) {
            log.error("addDetail, param error.roomKey={}, roomType={}, roomPrice={}", data.getRoomKey(),
                    data.getRoomType(), data.getRoomPrice());
            return new Result(true, StatusEnum.PARAM_ERROR, null, null);
        }
        if (!AccessUtil.checkAccess(request, JwtConstantConfig.USER_ROLE_ADMIN)) {
            log.error("addDetail, access error.");
            return new Result(true, StatusEnum.ACCESS_ERROR, null, null);
        }
        if (roomInfoService.saveDynamic(data)) {
            return new Result(true, StatusEnum.OK, null, null);
        }
        log.error("addDetail, manager error.");
        return new Result(true, StatusEnum.ERROR, null, null);
    }

}
