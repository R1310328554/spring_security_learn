package com.lk.learn.dynamicAuth;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ResourceMapperDao {
    /**
     * @Author dw
     * @Description 获取所有的资源
     * @Date 2020/4/15 11:16
     * @Param
     * @return
     */
    public List<Resources> getAllResources();

    /**
     * 通过具体资源url，查找匹配的资源
     * @param requestUrl
     * @return
     */
    public List<Resources> getAllResources2(String requestUrl);
}
