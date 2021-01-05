package com.xust.hotel.acl_pojo.dbo;

import com.alibaba.fastjson.JSONObject;

import com.xust.hotel.common.dto.BasePojo;
import lombok.*;

import java.util.List;

/**
 * <p>
 * 操作轨迹单
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
public class HosingRecordDO extends BasePojo {
    private static final long serialVersionUID = 8100746077721188816L;

    private Integer id;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * hosing person info
     */
    private String customerInfo;

    /**
     * 操作时间
     */
    private String operateTime;

    /**
     * 操作事件
     */
    private String operateEvent;

    /**
     * 操作对象
     */
    private String operateObj;

    /**
     * 操作cp
     */
    private String operateCp;

    /**
     * 备用
     */
    private String feature;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
