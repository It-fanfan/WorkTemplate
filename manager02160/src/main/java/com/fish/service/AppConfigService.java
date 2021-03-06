package com.fish.service;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.primary.mapper.ArcadeGameSetMapper;
import com.fish.dao.primary.model.ArcadeGameSet;
import com.fish.dao.second.mapper.AppConfigMapper;
import com.fish.dao.second.mapper.WxConfigMapper;
import com.fish.dao.second.model.AppConfig;
import com.fish.dao.second.model.WxConfig;
import com.fish.protocols.GetParameter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

/**
 * 审核配置
 * AppConfigService
 *
 * @author
 * @date
 */
@Service
public class AppConfigService implements BaseService<AppConfig> {

    @Autowired
    AppConfigMapper appConfigMapper;
    @Autowired
    WxConfigMapper wxConfigMapper;
    @Autowired
    ArcadeGameSetMapper arcadeGameSetMapper;

    /**
     * 查询展示appConfig信息
     *
     * @param parameter
     * @return
     */
    @Override
    public List<AppConfig> selectAll(GetParameter parameter) {
        List<AppConfig> appConfigs = appConfigMapper.selectAll();
        for (AppConfig appConfig : appConfigs) {
            String ddappid = appConfig.getDdappid();
            Integer ddcode = appConfig.getDdcode();
            Integer ddcheckcode = appConfig.getDdcheckcode();
            if (ddcode != null) {
                ArcadeGameSet arcadeGameSet = arcadeGameSetMapper.selectByPrimaryKey(ddcode);
                //设置游戏名称
                appConfig.setCodename(arcadeGameSet != null ? arcadeGameSet.getDdname() : "");
            }
            if (ddcheckcode != null && ddcheckcode != 0) {
                ArcadeGameSet arcadeGameSet = arcadeGameSetMapper.selectByPrimaryKey(ddcheckcode);
                //设置合集名称
                appConfig.setCheckcodename(arcadeGameSet.getDdname());
            }
            WxConfig wxConfig = wxConfigMapper.selectByPrimaryKey(ddappid);
            if (wxConfig != null) {
                String productName = wxConfig.getProductName();
                //设置产品名称
                appConfig.setProductName(productName);
                if (StringUtils.isNotBlank(wxConfig.getOriginName())) {
                    appConfig.setOriginName(wxConfig.getOriginName());
                }
            }
        }
        return appConfigs;
    }

    /**
     * 新增appConfig信息
     *
     * @param record
     * @return
     */
    public int insert(AppConfig record) {
        return appConfigMapper.insert(record);
    }

    /**
     * 更新appConfig信息
     *
     * @param record
     * @return
     */
    public int updateByPrimaryKeySelective(AppConfig record) {
        record.setDdtime(new Timestamp(System.currentTimeMillis()));
        return appConfigMapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 默认排序
     *
     * @param parameter
     * @return
     */
    @Override
    public void setDefaultSort(GetParameter parameter) {
    }

    @Override
    public Class<AppConfig> getClassInfo() {
        return AppConfig.class;
    }

    /**
     * 筛选
     *
     * @param searchData
     * @return
     */
    @Override
    public boolean removeIf(AppConfig appConfig, JSONObject searchData) {
        if (existValueFalse(searchData.getString("appId"), appConfig.getDdappid())) {
            return true;
        }
        if (existValueFalse(searchData.getString("gameName"), appConfig.getDdappid())) {
            return true;
        }
        return existValueFalse(searchData.getString("gameCode"), appConfig.getDdcode());
    }

    /**
     * 审核合集下拉框
     *
     * @param AppId
     * @return
     */
    public AppConfig select(String AppId) {
        return appConfigMapper.selectByPrimaryKey(AppId);
    }
}
