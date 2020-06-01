package com.cc.manager;

import com.cc.manager.common.utils.RedisUtil;
import com.cc.manager.modules.jj.entity.AllCost;
import com.cc.manager.modules.jj.mapper.UserAppMapper;
import com.cc.manager.modules.jj.service.AllCostService;
import com.cc.manager.modules.jj.service.RechargeService;
import com.cc.manager.modules.jj.service.RoundExtService;
import com.cc.manager.modules.jj.service.UserInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootTest
class ManagerJjApplicationTests {

    @Autowired
    UserAppMapper userAppMapper;
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    RechargeService RechargeService;
    @Autowired
    AllCostService allCostService;
    @Autowired
    RoundExtService roundExtService;
    @Test
    void contextLoads() {
    }

    @Test
    public void getString() {
//        QueryWrapper<UserApp> userAppQueryWrapper = new QueryWrapper<>();
//        userAppQueryWrapper.eq("ddUid", "6F8BE96F678D2BCAA5653BA58E20EB96").eq("ddAppId", 1110381534);
//        UserApp userApp = userAppMapper.selectOne(userAppQueryWrapper);
//        System.out.println(userApp.toString());
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime parse = LocalDateTime.parse("2020-02-15 19:40:32",df);
        AllCost allCost= allCostService.selectRemainAmount("oSn_Lw88gelVi7jYCijJUAArmMIs",parse);
//        BigDecimal i = RechargeService.selectUserCashOut("oSn_Lw88gelVi7jYCijJUAArmMIs","2020-02-05");
        System.out.println(allCost.toString());
       // Recharge recharge = RechargeService.selectById("oSn_Lw88gelVi7jYCijJUAArmMIs");
    }

    @Test
    public void getUserInfo() {

        Integer coin = (Integer) redisUtil.hashGet("user-oxDM75DNVEcHIT0eHnL-QYCMv8sY", "coin");
        System.out.println(coin.toString());
//        UserInfo userInfo = this.userInfoService.getById("oSn_Lw5rxqPXgC3b_M0heLW5wzbA");
//        System.out.println(userInfo.toString()

    }
    @Test
    public void getRecharge() {
//        List<Recharge> recharges = rechargeMapper.selectAllCharge(null,null);
//        for (Recharge recharge : recharges) {
//            System.out.println(recharge.toString());
//        }
        //SELECT  COUNT(*) FROM persie_deamon.round_ext  WHERE ddGroup = TRUE

       // int i = this.roundExtService.selectSMaxId();
      //  System.out.println(i);
    }

}
