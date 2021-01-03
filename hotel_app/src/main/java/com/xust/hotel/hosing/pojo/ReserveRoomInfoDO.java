package com.xust.hotel.hosing.pojo;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.xust.hotel.common.dto.BasePojo;
import lombok.*;

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


    @TableId(value = "id", type = IdType.AUTO)
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
     * 客户姓名
     */
    private String customerName;

    /**
     * 客户电话
     */
    private String customerPhone;

    /**
     * 证件类型
     */
    private String customerIdentificationType;

    /**
     * 证件号码
     */
    private String customerIdentificationNum;

    /**
     * rt0->已预定,rt1->已消费,rt2->已取消
     */
    private String reserveStatus;

    /**
     * 当前操作人登录账号
     */
    private String createCp;

    /**
     * 操作时间
     */
    private String createTime;

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
