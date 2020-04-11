package com.fish.controller;

import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.service.RechargedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * 提现记录查询
 * RechargedController
 * @author
 * @date
 */
@RestController
@RequestMapping(value = "/manage")
public class RechargedController
{
    @Autowired
    RechargedService rechargedService;

    @GetMapping(value = "/recharged")
    public GetResult getCharged(GetParameter parameter)
    {
        return rechargedService.findAll(parameter);
    }


}
