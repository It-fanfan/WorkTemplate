package com.fish.controller;

import com.fish.dao.primary.model.RoundExt;
import com.fish.dao.second.model.Recharge;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.protocols.PostResult;
import com.fish.service.RoundExtService;
import com.fish.utils.BaseConfig;
import com.fish.utils.ReadJsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 赛制配置
 * RoundExtController
 *
 * @author
 * @date
 */
@RestController
@RequestMapping(value = "/manage")
public class RoundExtController {

    @Autowired
    RoundExtService roundExtService;

    @Autowired
    BaseConfig baseConfig;

    /**
     * 查询赛制
     *
     * @param parameter
     * @return
     */
    @GetMapping(value = "/roundext")
    public GetResult getRecharge(GetParameter parameter) {
        return roundExtService.findAll(parameter);
    }

    @PostMapping(value = "/roundext/delete")
    public PostResult deleteRecharge(@RequestBody Recharge productInfo) {
        PostResult result = new PostResult();
        result.setMsg("操作成功");
        return result;
    }

    /**
     * 新增赛制配置信息
     *
     * @param productInfo
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/roundext/new")
    public PostResult insertRoundExt(@RequestBody RoundExt productInfo) {
        PostResult result = new PostResult();
        int count = roundExtService.insert(productInfo);
        //int count =1;
        if (count != 0) {
            //刷新业务表结构
            String res = ReadJsonUtil.flushTable("round_ext", baseConfig.getFlushCache());
            result.setMsg("操作成功" + res);
        } else {
            result.setSuccessed(false);
            result.setMsg("操作失败，请联系管理员");
        }
        return result;
    }

    /**
     * 修改游戏信息
     *
     * @param productInfo
     * @return
     */
    @PostMapping(value = "/roundext/edit")
    public PostResult modifyRoundExt(@RequestBody RoundExt productInfo) {
        PostResult result = new PostResult();
        int count = roundExtService.updateByPrimaryKeySelective(productInfo);
        if (count != 0) {
            //刷新业务表结构
            String res = ReadJsonUtil.flushTable("round_ext", baseConfig.getFlushCache());
            result.setMsg("操作成功" + res);
        } else {
            result.setSuccessed(false);
            result.setMsg("操作失败，请联系管理员");
        }
        return result;
    }
}
