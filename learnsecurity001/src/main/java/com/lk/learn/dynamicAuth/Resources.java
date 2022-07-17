package com.lk.learn.dynamicAuth;

import lombok.Data;

import java.util.List;

@Data
public class Resources {

    private Integer id;

    /**
     * url端点，可以认为是菜单、按钮；
     *
     * 其实是一个 ant表达式，一般应该是 / 开头的绝对路径。
     */
    private String pattern;

    /**
     * 访问当前资源需要的角色
     */
    private List<Role> roles;

    /**
     * todo  资源有上下级关系， 比如 /admin/** 应该是包含 /admin/cfg/** 的； 应该在数据库建立这种直接上下级关系
     */
    private Integer parentId;
}
