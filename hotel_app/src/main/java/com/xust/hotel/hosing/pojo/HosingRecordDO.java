package com.xust.hotel.hosing.pojo;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.xust.hotel.common.dto.BasePojo;
import lombok.*;

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

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 入住人
     */
    private String hosingName;

    /**
     * 入住人电话
     */
    private String hosingPhone;

    /**
     * 入住人证件类型
     */
    private String hosingIdenType;

    /**
     * 入住人证件号码
     */
    private String hosingIdenNum;

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
