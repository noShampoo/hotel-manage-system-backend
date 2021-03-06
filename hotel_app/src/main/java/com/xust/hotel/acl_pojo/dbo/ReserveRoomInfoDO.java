package com.xust.hotel.acl_pojo.dbo;

import com.alibaba.fastjson.JSONObject;

import com.xust.hotel.common.dto.BasePojo;
import lombok.*;

import java.util.List;

/**
 * <p>
 * 预定信息单
 * </p>
 *
 * @author bhj
 * @since 2021-01-03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ReserveRoomInfoDO extends BasePojo {
    private static final long serialVersionUID = -7176667496320364832L;


    private Integer id;

    /**
     * 预定房间号
     */
    private String reserveRoomNo;

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
    private String customerInfo;

    /**
     * rt0->已预定,rt1->已消费,rt2->已取消
     */
    private String reserveStatus;

    /**
     * 当前操作人登录账号
     */
    private String operateCp;

    /**
     * 操作时间
     */
    private String operateTime;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 备用
     */
    private String feature;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
