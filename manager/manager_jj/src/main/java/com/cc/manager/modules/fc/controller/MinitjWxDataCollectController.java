package com.cc.manager.modules.fc.controller;


import com.cc.manager.common.mvc.BaseStatsController;
import com.cc.manager.common.result.StatsListParam;
import com.cc.manager.common.result.StatsListResult;
import com.cc.manager.modules.fc.service.MinitjWxDataCollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cf
 * @since 2020-05-22
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/fc/dataCollect")
public class MinitjWxDataCollectController implements BaseStatsController {

    private MinitjWxDataCollectService minitjWxDataCollectService;

    @Override
    @GetMapping(value = "/getPage")
    public StatsListResult getPage(StatsListParam statsListParam) {
        return this.minitjWxDataCollectService.getPage(statsListParam);
    }

    @Autowired
    public void setMinitjWxDataCollectService(MinitjWxDataCollectService minitjWxDataCollectService) {
        this.minitjWxDataCollectService = minitjWxDataCollectService;
    }

}

