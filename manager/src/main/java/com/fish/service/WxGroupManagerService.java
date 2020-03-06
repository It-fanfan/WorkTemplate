package com.fish.service;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.second.mapper.WxGroupManagerMapper;
import com.fish.dao.second.model.WxGroupManager;
import com.fish.protocols.GetParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class WxGroupManagerService implements BaseService<WxGroupManager>{

    @Autowired
    WxGroupManagerMapper wxGroupManagerMapper;

    @Override
    public List<WxGroupManager> selectAll(GetParameter parameter) {
        //long current = System.currentTimeMillis();
        List<WxGroupManager> recharges;
        String sql = "select id AS id, ddId AS ddId, `describe` AS `describe`,  ddStatus AS ddStatus, ddYes AS ddYes," +
                " ddNo AS ddNo,  updateTime AS updateTime, cdId AS cdId," +
                " wxGroupName AS wxGroupName, wxGroupManager AS wxGroupManager, wxNumber AS wxNumber, " +
                " createTime  AS createTime, updateQrCodeTime AS updateQrCodeTime" +
                " FROM config_confirm, wx_group_manager " +
                " WHERE cdId = ddId";
        recharges = wxGroupManagerMapper.selectAllConfigSQL(sql);
        return recharges;
    }

    public int updateWxGroupManager(WxGroupManager wxGroupManager){

        return wxGroupManagerMapper.updateWxGroupManager(wxGroupManager);
    }

    public int updateConfigConfirm(WxGroupManager wxGroupManager){

        return wxGroupManagerMapper.updateConfigConfirm(wxGroupManager);
    }

    /**
     *  判断是否更新上传时间
     * @param wxGroupManager
     * @return
     */
    public boolean isUpdateOperateTime(WxGroupManager wxGroupManager){
        boolean isUpdate = false;
        WxGroupManager wxGroupManager1 =  wxGroupManagerMapper.selectQrCodeByPrimaryKey(wxGroupManager);
        if(!wxGroupManager.getDdYes().equals(wxGroupManager1.getDdYes())
                || !wxGroupManager.getDdNo().equals(wxGroupManager1.getDdNo())){
            isUpdate = true;
        }
        return isUpdate;
    }

   /* public int updateByPrimaryKeySelective(WxGroupManager wxGroupManager){
        return wxGroupManagerMapper.updateByPrimaryKeySelective(wxGroupManager);
    }*/

    @Override
    public void setDefaultSort(GetParameter parameter) {
        if (parameter.getOrder() != null){
            return;
        }
        parameter.setOrder("desc");
        parameter.setSort("id");
    }

    @Override
    public Class<WxGroupManager> getClassInfo() {
        return WxGroupManager.class;
    }

    @Override
    public boolean removeIf(WxGroupManager wxGroupManager, JSONObject searchData) {
        return false;
    }
}
