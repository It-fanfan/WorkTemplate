package com.cc.manager.modules.jj.controller;


import com.cc.manager.common.mvc.BaseStatsController;
import com.cc.manager.common.result.StatsListParam;
import com.cc.manager.common.result.StatsListResult;
import com.cc.manager.modules.jj.service.OnlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cf
 * @since 2020-06-09
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/jj/online")
public class OnlineController implements BaseStatsController {

    private OnlineService onlineService;

    @Override
    @GetMapping(value = "/getPage")
    public StatsListResult getPage(StatsListParam statsListParam) {
        return this.onlineService.getPage(statsListParam);
    }

    @Autowired
    public void setOnlineService(OnlineService onlineService) {
        this.onlineService = onlineService;
    }

}

