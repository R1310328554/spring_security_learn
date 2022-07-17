package com.lk.learn.dynamicAuth;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UserMapperDao {

  public User loadUserByUsername(String userName);

  /**
   *
   * @param uid 用户id
   * @return
   */
  public List<Role> getUserRolesByUid(Integer uid);

  /**
   *
   * @param uid 用户id
   * @return
   */
  public List<Resources> getUserResourcesByUid(Integer uid);


  /**
   *
   * @param roleIds 用户的所有角色id
   * @return
   */
  public List<Resources> getUserResourcesByRoleIds(String roleIds);

}
