package com.cc.manager.modules.jj.controller;


import com.alibaba.fastjson.JSONObject;
import com.cc.manager.common.mvc.BaseCrudController;
import com.cc.manager.common.result.CrudObjectResult;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.result.CrudPageResult;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.modules.jj.entity.SupplementOrder;
import com.cc.manager.modules.jj.service.SupplementOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 手动补单
 *
 * @author cf
 * @since 2020-05-09
 */
@CrossOrigin
@RestController
@RequestMapping("/jj/supplementOrder")
public class SupplementOrderController implements BaseCrudController {

    private SupplementOrderService supplementOrderService;

    @Override
    @GetMapping(value = "/id/{id}")
    public CrudObjectResult getObjectById(@PathVariable String id) {
        return this.supplementOrderService.getObjectById(id);
    }

    @Override
    @GetMapping(value = "/getObject/{getObjectParam}")
    public CrudObjectResult getObject(@PathVariable String getObjectParam) {
        return this.supplementOrderService.getObject(getObjectParam);
    }

    @Override
    @GetMapping(value = "/getPage")
    public CrudPageResult getPage(CrudPageParam crudPageParam) {
        return this.supplementOrderService.getPage(crudPageParam);
    }

    /**
     * 查询用户当前金币
     */
    @ResponseBody
    @GetMapping(value = "/queryUserInfo")
    public SupplementOrder selectCurrentCoinByUid(@RequestParam("uid") String uid) {
        return supplementOrderService.selectCurrentCoin(uid);
    }

    @Override
    @PostMapping
    public PostResult post(@RequestBody String requestParam) {
        return this.supplementOrderService.post(requestParam);
    }

    @Override
    @PutMapping
    public PostResult put(@RequestBody String requestParam) {
        return this.supplementOrderService.put(requestParam);
    }

    @Override
    @DeleteMapping
    public PostResult delete(@RequestBody String requestParam) {
        return this.supplementOrderService.delete(requestParam);
    }

    @Override
    public JSONObject getSelectArray(String requestParam) {
        return null;
    }

    @Autowired
    public void setSupplementOrderService(SupplementOrderService supplementOrderService) {
        this.supplementOrderService = supplementOrderService;
    }

}

