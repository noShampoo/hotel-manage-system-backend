package com.xust.hotel.hosing.pojo;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.xust.hotel.common.dto.BasePojo;
import lombok.*;

/**
 * <p>
 * 客房单
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
public class GuestRoomDO extends BasePojo {

    private static final long serialVersionUID = 4130172737390492943L;
    /**
     * 房间号
     */
    @TableId(value = "room_no", type = IdType.ASSIGN_ID)
    private String roomNo;

    /**
     * rs0->无人,rs1->有人,rs2->预定
     */
    private String roomStatus;

    /**
     * 房间详情编号
     */
    private String roomDetail;

    /**
     * 状态非rs0时存放
     */
    private String orderNo;

    /**
     * ps0->停用，ps1
     */
    private String phyStatus;

    /**
     * 备用
     */
    private String feature;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
