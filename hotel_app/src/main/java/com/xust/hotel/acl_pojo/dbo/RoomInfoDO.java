package com.xust.hotel.acl_pojo.dbo;

import com.alibaba.fastjson.JSONObject;
import com.xust.hotel.common.dto.BasePojo;
import lombok.*;

/**
 * <p>
 * 客房详情单
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
public class RoomInfoDO extends BasePojo {
    private static final long serialVersionUID = -5800437871583314710L;

    /**
     * 房间详情编号
     */
    private String roomTypeKey;

    /**
     * 房间类型
     */
    private String roomType;

    /**
     * 房间价格
     */
    private Double roomPrice;

    /**
     * 货币单位
     */
    private String priceUnit;

    /**
     * 备用
     */
    private String feature;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
