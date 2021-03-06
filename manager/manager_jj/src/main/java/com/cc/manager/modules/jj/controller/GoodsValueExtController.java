package com.cc.manager.modules.jj.controller;


import com.alibaba.fastjson.JSONObject;
import com.cc.manager.common.mvc.BaseCrudController;
import com.cc.manager.common.result.CrudObjectResult;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.result.CrudPageResult;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.modules.jj.service.GoodsValueExtService;
import com.cc.manager.modules.jj.utils.PersieServerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 全局配置
 *
 * @author cf
 * @since 2020-05-09
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/jj/goodsValueExt")
public class GoodsValueExtController implements BaseCrudController {

    private GoodsValueExtService goodsValueExtService;
    private PersieServerUtils persieServerUtils;

    @Override
    @GetMapping(value = "/id/{id}")
    public CrudObjectResult getObjectById(@PathVariable String id) {
        return this.goodsValueExtService.getObjectById(id);
    }

    @Override
    @GetMapping(value = "/getObject/{getObjectParam}")
    public CrudObjectResult getObject(@PathVariable String getObjectParam) {
        return this.goodsValueExtService.getObject(getObjectParam);
    }

    @Override
    @GetMapping(value = "/getPage")
    public CrudPageResult getPage(CrudPageParam crudPageParam) {
        return this.goodsValueExtService.getPage(crudPageParam);
    }

    @Override
    @PostMapping
    public PostResult post(@RequestBody String requestParam) {
        PostResult postResult = this.goodsValueExtService.post(requestParam);
        if (postResult.getCode() == 1) {
            this.persieServerUtils.refreshTable("goods_value_ext");
            postResult = this.persieServerUtils.refreshPublicTable("goods_value_ext");
        }
        return postResult;
    }

    @Override
    @PutMapping
    public PostResult put(@RequestBody String requestParam) {

        PostResult putResult = this.goodsValueExtService.put(requestParam);
        if (putResult.getCode() == 1) {
            this.persieServerUtils.refreshTable("goods_value_ext");
            putResult = this.persieServerUtils.refreshPublicTable("goods_value_ext");
        }
        return putResult;
    }

    @Override
    @DeleteMapping
    public PostResult delete(@RequestBody String requestParam) {

        PostResult deleteResult = this.goodsValueExtService.delete(requestParam);
        if (deleteResult.getCode() == 1) {
            this.persieServerUtils.refreshTable("goods_value_ext");
            deleteResult = this.persieServerUtils.refreshPublicTable("goods_value_ext");
        }
        return deleteResult;
    }

    @Override
    public JSONObject getSelectArray(String requestParam) {
        return null;
    }

    @Autowired
    public void setGoodsValueExtService(GoodsValueExtService goodsValueExtService) {
        this.goodsValueExtService = goodsValueExtService;
    }

    @Autowired
    public void setPersieServerUtils(PersieServerUtils persieServerUtils) {
        this.persieServerUtils = persieServerUtils;
    }

}

