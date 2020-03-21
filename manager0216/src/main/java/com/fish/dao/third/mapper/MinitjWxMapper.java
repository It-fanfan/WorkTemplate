package com.fish.dao.third.mapper;

import com.fish.dao.third.model.MinitjWx;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MinitjWxMapper {
    int deleteByPrimaryKey(@Param("wxAppid") String wxAppid, @Param("wxDate") String wxDate);

    int insert(MinitjWx record);

    int insertSelective(MinitjWx record);

    MinitjWx selectByPrimaryKey(@Param("wxAppid") String wxAppid, @Param("wxDate") String wxDate);

    int updateByPrimaryKeySelective(MinitjWx record);

    int updateByPrimaryKeyWithBLOBs(MinitjWx record);

    int updateByPrimaryKey(MinitjWx record);

    List<MinitjWx> searchData(String sql);

    List<MinitjWx> searchDatas(@Param("wxAppid") String wxAppid, @Param("beginTime") String beginTime, @Param("endTime") String endTime);

    /**
     * �������ڲ�ѯС��Ϸ����
     * @param beginTime
     * @param endTime
     * @return
     */
    List<MinitjWx> queryMinitjWxByDate(@Param("beginTime") String beginTime, @Param("endTime") String endTime);

    List<String> dateCash();
}