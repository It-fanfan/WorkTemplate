package com.fish.controller;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.primary.model.ArcadeGames;
import com.fish.dao.primary.model.CheckBoxData;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.protocols.PostResult;
import com.fish.service.GamesService;
import com.fish.utils.BaseConfig;
import com.fish.utils.ReadJsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 游戏列表 gameList
 * GamesController
 *
 * @author
 * @date
 */
@RestController
@RequestMapping(value = "/manage")
public class GamesController {

    @Autowired
    GamesService gamesService;
    @Autowired
    BaseConfig baseConfig;

    /**
     * 查询展示所有游戏信息
     *
     * @param parameter
     * @return
     */
    @GetMapping(value = "/games")
    public GetResult getGames(GetParameter parameter) {
        return gamesService.findAll(parameter);
    }

    /**
     * 查询games下拉框信息
     *
     * @param parameter
     * @return
     */
    @GetMapping(value = "/games/selectBox")
    public List<CheckBoxData> getGamesBox(GetParameter parameter) {
        List<ArcadeGames> arcadeGames = gamesService.selectAll(parameter);
        List<CheckBoxData> checkBoxDatas = new ArrayList<>();
        for (ArcadeGames arcadeGame : arcadeGames) {
            CheckBoxData checkBoxData = new CheckBoxData();
            String name = arcadeGame.getDdname();
            Integer code = arcadeGame.getDdcode();
            checkBoxData.setTitle(name + "(" + code + ")");
            checkBoxData.setValue(code);
            checkBoxDatas.add(checkBoxData);
        }
        return checkBoxDatas;
    }

    /**
     * 获取资源图
     *
     * @param parameter
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/games/flushgames")
    public PostResult getGamesResources(@RequestBody JSONObject parameter) {
        PostResult result = new PostResult();
        int i = gamesService.flushGamesResources(parameter);
        if (i != 0) {
            //刷新业务表结构
            String res = ReadJsonUtil.flushTable("games", baseConfig.getFlushCache());
        } else {
            result.setSuccessed(false);
            result.setMsg("操作失败，请联系管理员");
            return result;
        }
        return result;
    }

    /**
     * 新增游戏信息
     *
     * @param productInfo
     * @return
     */
    @PostMapping(value = "/games/new")
    public PostResult insertGames(@RequestBody ArcadeGames productInfo) {
        PostResult result = new PostResult();
        int count = gamesService.insert(productInfo);
        if (count == 1) {
            //刷新业务表结构
            String res = ReadJsonUtil.flushTable("games", baseConfig.getFlushCache());
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
    @PostMapping(value = "/games/edit")
    public PostResult modifyGames(@RequestBody ArcadeGames productInfo) {
        PostResult result = new PostResult();
        int count = gamesService.updateByPrimaryKeySelective(productInfo);
        if (count != 0) {
            //刷新业务表结构
            String res = ReadJsonUtil.flushTable("games", baseConfig.getFlushCache());
        } else {
            result.setSuccessed(false);
            result.setMsg("操作失败，请联系管理员");
        }
        return result;
    }

}
