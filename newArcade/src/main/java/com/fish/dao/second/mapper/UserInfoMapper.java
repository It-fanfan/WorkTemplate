package com.fish.dao.second.mapper;

import com.fish.dao.second.model.UserInfo;
import com.fish.protocols.PlayUserVO;
import com.fish.protocols.UserPayVO;

import java.util.List;

public interface UserInfoMapper
{
    int deleteByPrimaryKey(Long userid);

    int insert(UserInfo record);

    int insertSelective(UserInfo record);

    UserInfo selectByPrimaryKey(Long userid);

    int updateByPrimaryKeySelective(UserInfo record);

    int updateByPrimaryKey(UserInfo record);

    List<PlayUserVO> selectPlayUser();

    List<UserPayVO> selectMonitorUser();
}