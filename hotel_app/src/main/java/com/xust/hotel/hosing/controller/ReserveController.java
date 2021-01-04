package com.xust.hotel.hosing.controller;

import com.xust.hotel.hosing.service.HosingRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author bhj
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = "/reserve")
public class ReserveController {

    @Autowired
    private HosingRecordService hosingRecordService;

    @PostMapping("/login/test")
    public void test() {
        hosingRecordService.test();
    }

}
