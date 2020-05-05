package com.fish.dao.primary.mapper;

import com.fish.dao.primary.model.PublicCentre;

import java.util.List;

public interface PublicCentreMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PublicCentre record);

    PublicCentre selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PublicCentre record);

    List<PublicCentre> selectAll();

    List<PublicCentre> selectAllBanner();

    List<PublicCentre> selectAllGame();

    List<PublicCentre> selectAllRecommend();

    int updateShowId(PublicCentre publicCentre);
}