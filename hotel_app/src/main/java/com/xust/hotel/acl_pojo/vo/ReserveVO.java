package com.xust.hotel.acl_pojo.vo;

import com.alibaba.fastjson.JSONObject;
import com.xust.hotel.acl_pojo.dbo.CustomerInfoPojo;
import com.xust.hotel.common.dto.BasePojo;
import lombok.*;

import java.util.List;

/**
 * @author bhj
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ReserveVO extends BasePojo {

    private static final long serialVersionUID = 8649713448177974146L;
    /**
     * 预定房间号
     */
    private String roomNo;

    /**
     * 预定时长
     */
    private Integer reserveDay;

    /**
     * 预定时间段
     */
    private String reserveTime;

    /**
     * customer info
     */
    private List<CustomerInfoPojo> customerInfo;

    /**
     * 订单编号
     */
    private String orderNo;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}