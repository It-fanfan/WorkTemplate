package com.fish.controller;

import com.fish.dao.primary.model.RoundExt;
import com.fish.dao.primary.model.RoundMatch;
import com.fish.dao.second.model.WxConfig;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.protocols.PostResult;
import com.fish.service.RoundMatchService;
import com.fish.utils.BaseConfig;
import com.fish.utils.ReadJsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 现金赛制
 * RoundMatchController
 *
 * @author
 * @date
 */
@RestController
@RequestMapping(value = "/manage")
public class RoundMatchController {

    @Autowired
    RoundMatchService roundMatchService;
    @Autowired
    BaseConfig baseConfig;

    /**
     * 查询小程序赛制列表
     *
     * @param parameter
     * @return
     */
    @GetMapping(value = "/roundmatch")
    public GetResult getRecharge(GetParameter parameter) {
        return roundMatchService.findAll(parameter);
    }

    @ResponseBody
    @GetMapping(value = "/roundmatch/rounds")
    public List<RoundExt> getRoundExt(GetParameter parameter) {
        return roundMatchService.selectAllG();
    }

    /**
     * 异步查询所有赛制
     *
     * @param parameter
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/roundmatch/roundall")
    public List<RoundExt> getRound(GetParameter parameter) {
        return roundMatchService.selectAllRound();
    }

    /**
     * 异步查询小程序赛制名称
     *
     * @param parameter
     * @return
     */
    @GetMapping(value = "/roundmatch/appname")
    public List<WxConfig> getAppName(GetParameter parameter) {
        return roundMatchService.getAppName();
    }

    @PostMapping(value = "/roundmatch/delete")
    public PostResult deleteRecharge(@RequestBody RoundMatch productInfo) {
        PostResult result = new PostResult();
        int count = 1;
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
    @PostMapping(value = "/roundmatch/new")
    public PostResult insertRoundExt(@RequestBody RoundMatch productInfo) {
        PostResult result = new PostResult();
        int count = roundMatchService.insert(productInfo);
        if (count == 1) {
            //刷新业务表结构
            String res = ReadJsonUtil.flushTable("round_match", baseConfig.getFlushCache());
            result.setMsg("操作成功" + res);
            return result;
        } else {
            result.setSuccessed(false);
            result.setMsg("操作失败，请联系管理员");
            return result;
        }
    }

    /**
     * 修改小程序赛制信息
     *
     * @param productInfo
     * @return
     */
    @PostMapping(value = "/roundmatch/edit")
    public PostResult modifyRoundExt(@RequestBody RoundMatch productInfo) {
        PostResult result = new PostResult();
        int count = roundMatchService.updateByPrimaryKeySelective(productInfo);
        if (count != 0) {
            //刷新业务表结构
            String res = ReadJsonUtil.flushTable("round_match", baseConfig.getFlushCache());
            result.setMsg("操作成功" + res);
            return result;
        } else {
            result.setSuccessed(false);
            result.setMsg("操作失败，请联系管理员");
            return result;
        }
    }
}
