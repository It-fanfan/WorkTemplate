package com.fish.controller;

import com.fish.dao.second.model.WxConfig;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.protocols.PostResult;
import com.fish.service.WxAddShowService;
import com.fish.utils.BaseConfig;
import com.fish.utils.ReadJsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 微信广告数据明细
 *
 * @author
 * @date
 */
@Controller
@RequestMapping(value = "/manage")
public class WxAddShowController {
    @Autowired
    WxAddShowService wxAddShowService;
    @Autowired
    BaseConfig baseConfig;

    /**
     * 查询展示所有wxConfig信息
     *
     * @param parameter
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/wxadd")
    public GetResult getWxWxAddShow(GetParameter parameter) {
        return wxAddShowService.findAll(parameter);
    }

    /**
     * 修改游戏appid、secret信息
     *
     * @param productInfo
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/wxadd/edit")
    public PostResult modifyWxWxAddShow(@RequestBody WxConfig productInfo) {
        PostResult result = new PostResult();
        int count = wxAddShowService.updateByPrimaryKeySelective(productInfo);
        if (count == 1) {
            //刷新业务表结构
            String resWx = ReadJsonUtil.flushTable("wx_config", baseConfig.getFlushCache());
            String resApp = ReadJsonUtil.flushTable("app_config", baseConfig.getFlushCache());
            result.setMsg("操作成功");
        } else {
            result.setSuccessed(false);
            result.setMsg("操作失败，请联系管理员");
        }
        return result;
    }
}
