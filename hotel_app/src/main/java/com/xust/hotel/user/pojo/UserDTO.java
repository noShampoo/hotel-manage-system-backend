package com.xust.hotel.user.pojo;

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
public class UserDTO extends BasePojo {

    private static final long serialVersionUID = 1663044010643691958L;

    /**
     * 用户名
     */
    private String user;

    /**
     * 使用者姓名
     */
    private String name;

    /**
     * 账号密码
     */
    private String password;

    /**
     * t0->normal,t1->admin
     */
    private String type;

    /**
     * s0->not using,s1->using
     */
    private String status;

    /**
     * page
     */
    private Integer page;

    /**
     * size
     */
    private Integer pageSize;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
