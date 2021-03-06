package com.cc.manager.modules.fc.controller;


import com.alibaba.fastjson.JSONObject;
import com.cc.manager.common.mvc.BaseCrudController;
import com.cc.manager.common.result.CrudObjectResult;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.result.CrudPageResult;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.modules.fc.entity.MiniGame;
import com.cc.manager.modules.fc.service.MiniGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author cf
 * @since 2020-05-13
 */

@CrossOrigin
@RestController
@RequestMapping(value = "/fc/miniGame")
public class MiniGameController implements BaseCrudController {
    private MiniGameService miniGameService;

    @Override
    @GetMapping(value = "/id/{id}")
    public CrudObjectResult getObjectById(@PathVariable String id) {
        return this.miniGameService.getObjectById(id);
    }

    @Override
    @GetMapping(value = "/getObject/{getObjectParam}")
    public CrudObjectResult getObject(@PathVariable String getObjectParam) {
        return this.miniGameService.getObject(getObjectParam);
    }

    @Override
    @GetMapping(value = "/getPage")
    public CrudPageResult getPage(CrudPageParam crudPageParam) {
        return this.miniGameService.getPage(crudPageParam);
    }

    @Override
    @PostMapping
    public PostResult post(@RequestBody String requestParam) {
        return this.miniGameService.post(requestParam);
    }

    @Override
    @PutMapping
    public PostResult put(@RequestBody String requestParam) {
        return this.miniGameService.put(requestParam);
    }

    @Override
    @DeleteMapping
    public PostResult delete(@RequestBody String requestParam) {
        return this.miniGameService.delete(requestParam);
    }

    @Override
    @GetMapping(value = "/getSelectArray")
    public JSONObject getSelectArray(String requestParam) {
        return this.miniGameService.getSelectArray(MiniGame.class, requestParam);
    }

    @Autowired
    public void setMiniGameService(MiniGameService miniGameService) {
        this.miniGameService = miniGameService;
    }

}

