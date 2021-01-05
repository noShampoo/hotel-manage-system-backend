package com.xust.hotel.acl_pojo.dbo;

import com.alibaba.fastjson.JSONObject;
import com.xust.hotel.common.dto.BasePojo;
import lombok.*;

/**
 * @author bhj
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CustomerInfoPojo extends BasePojo {
    private static final long serialVersionUID = 7079630151327563413L;

    /**
     * name
     */
    private String customerName;

    /**
     * phone
     */
    private String customerPhone;

    /**
     * identification type
     */
    private String customerIdenType;

    /**
     * identification num
     */
    private String customerIdenNum;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
