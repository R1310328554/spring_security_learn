package com.lk.learn.dynamicAuth;

import lombok.Data;

import java.io.Serializable;

@Data
public class Role implements Serializable {
    private static final long serialVersionUID = 825384782616737527L;

    private Integer id;

    private String name;

    private String description;

    /**
     * todo  角色可能有上下级关系， 比如 .. ； 是否在数据库建立这种直接上下级关系？
     */
    private Integer parentId;
}
