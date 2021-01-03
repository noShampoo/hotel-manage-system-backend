package com.xust.hotel.hosing.controller;

import com.xust.hotel.common.exception.InnerErrorException;
import com.xust.hotel.common.exception.KeyExistException;
import com.xust.hotel.common.exception.NoSuchKeyException;
import com.xust.hotel.common.exception.NotDeleteException;
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
import java.util.List;
import java.util.Map;

/**
 * @author bhj
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = "/room")
public class RoomController {


    @Autowired
    private RoomInfoService roomInfoService;

    /**
     * add room type
     */
    @PostMapping("/detail/add")
    public Result addDetail(@RequestBody RequestParam<RoomDetailVO> requestParam,
                            HttpServletRequest request) throws InnerErrorException, KeyExistException {
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
        log.error("addDetail, service error.");
        return new Result(true, StatusEnum.ERROR, null, null);
    }

    /**
     * modify
     */
    @PostMapping("detail/modify")
    public Result modify(@RequestBody RequestParam<RoomDetailVO> requestParam,
                         HttpServletRequest request) throws InnerErrorException, NoSuchKeyException, KeyExistException {
        if (requestParam == null || requestParam.getData() == null) {
            log.error("modify, param is null.");
            return new Result(true, StatusEnum.OK, "param is null", null);
        }
        RoomDetailVO data = requestParam.getData();
        if (StringUtils.isBlank(data.getRoomKey()) || StringUtils.isBlank(data.getRoomType())
                || data.getRoomPrice() == null) {
            log.error("modify, param error.roomKey={}, roomType={}, roomPrice={}", data.getRoomKey(),
                    data.getRoomType(), data.getRoomPrice());
            return new Result(true, StatusEnum.PARAM_ERROR, null, null);
        }
        if (!AccessUtil.checkAccess(request, JwtConstantConfig.USER_ROLE_ADMIN)) {
            log.error("modify, access error.");
            return new Result(true, StatusEnum.ACCESS_ERROR, null, null);
        }
        RoomDetailVO roomDetailVO = roomInfoService.modifyDynamic(data);
        if (roomDetailVO != null) {
            log.info("modify, roomDetailVO={}", roomDetailVO.toString());
            return new Result(true, StatusEnum.OK, null, roomDetailVO);
        }
        log.error("modify, service error.");
        return new Result(true, StatusEnum.ERROR, null, null);
    }

    /**
     * delete
     */
    @PostMapping("/detail/delete")
    public Result delete(@RequestBody RequestParam<Map<String, String>> requestParam,
                         HttpServletRequest request) throws InnerErrorException, NotDeleteException, NoSuchKeyException {
        if (requestParam == null || requestParam.getData() == null) {
            log.error("modify, param is null.");
            return new Result(true, StatusEnum.OK, "param is null", null);
        }
        String roomKey = requestParam.getData().get("roomKey");
        if (StringUtils.isBlank(roomKey)) {
            log.error("delete, roomKey is null.");
            return new Result(true, StatusEnum.PARAM_ERROR, "roomKey is none", null);
        }
        if (!AccessUtil.checkAccess(request, JwtConstantConfig.USER_ROLE_ADMIN)) {
            log.error("delete, access error.");
            return new Result(true, StatusEnum.ACCESS_ERROR, null, null);
        }
        if (roomInfoService.delete(roomKey)) {
            return new Result(true, StatusEnum.OK, null, null);
        }
        log.error("delete, service error.");
        return new Result(true, StatusEnum.ERROR, null, null);
    }


    /**
     * get all
     */
    @GetMapping("/detail/all/get/{page}/{size}")
    public Result all(@PathVariable("page") int page,
                      @PathVariable("size") int size,
                      HttpServletRequest request) throws InnerErrorException {
        if (page < 0 || size <= 0) {
            log.error("all, param error.page={}, size={}", page, size);
            return new Result(true, StatusEnum.PARAM_ERROR, "page:" + page + ", size={}" + size, null);
        }
        if (!AccessUtil.checkAccess(request, JwtConstantConfig.USER_ROLE_ADMIN)) {
            log.error("all, access error.");
            return new Result(true, StatusEnum.ACCESS_ERROR, null, null);
        }
        List<RoomDetailVO> voList = roomInfoService.query(page, size);
        return new Result(true, StatusEnum.OK, null, voList);
    }

    /**
     * get some
     */
    @GetMapping("detail/get/{roomKey}")
    public Result get(@PathVariable("roomKey") String roomKey,
                      HttpServletRequest request) throws InnerErrorException {
        if (StringUtils.isBlank(roomKey)) {
            log.error("get, param error.roomKey is null.");
            return new Result(true, StatusEnum.PARAM_ERROR, "path variable roomKey is null", null);
        }
        if (!AccessUtil.checkAccess(request, JwtConstantConfig.USER_ROLE_ADMIN)) {
            log.error("all, access error.");
            return new Result(true, StatusEnum.ACCESS_ERROR, null, null);
        }
        RoomDetailVO roomDetailVO = roomInfoService.queryByRoomKeyOrRoomType(roomKey, null);
        return new Result(true, StatusEnum.OK, null, roomDetailVO);
    }

}
