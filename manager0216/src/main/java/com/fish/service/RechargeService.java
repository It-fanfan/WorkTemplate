package com.fish.service;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.second.mapper.*;
import com.fish.dao.second.model.AllCost;
import com.fish.dao.second.model.Recharge;
import com.fish.dao.second.model.UserApp;
import com.fish.protocols.GetParameter;
import com.fish.service.cache.CacheService;
import com.fish.utils.XwhTool;
import com.fish.utils.log4j.Log4j;
import com.fish.utils.tool.CmTool;
import com.fish.utils.tool.SignatureAlgorithm;
import com.fish.utils.tool.WxConfig;
import com.fish.utils.tool.XMLHandler;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.fish.utils.tool.CmTool.createNonceStr;

/**
 * 提现审核 Service
 * RechargeService
 *
 * @author
 * @date
 */
@Service
public class RechargeService implements BaseService<Recharge> {
    private static final Logger LOG = LoggerFactory.getLogger(RechargeService.class);
    @Autowired
    RechargeMapper rechargeMapper;
    @Autowired
    UserAppMapper userAppMapper;
    @Autowired
    WxConfigMapper wxConfigMapper;
    @Autowired
    UserInfoMapper userInfoMapper;
    @Autowired
    UserValueMapper userValueMapper;
    @Autowired
    CacheService cacheService;
    @Autowired
    AllCostMapper allCostMapper;

    /**
     * 查询提现审核
     *
     * @param parameter
     * @return
     */
    @Override
    public List<Recharge> selectAll(GetParameter parameter) {
        List<Recharge> recharges;
        JSONObject search = getSearchData(parameter.getSearchData());
        if (search == null || search.getString("times").isEmpty()) {
            recharges = rechargeMapper.selectAll();
        } else {
            Date[] parse = XwhTool.parseDate(search.getString("times"));
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            recharges = rechargeMapper.selectAll(format.format(parse[0]), format.format(parse[1]));
        }
        List<Recharge> rechargeList = new ArrayList<>();
        for (Recharge recharge : recharges) {
            String dduid = recharge.getDduid();
            Date times = recharge.getDdtimes();
            String ddTime = DateFormatUtils.format(times, "yyyy-MM-dd HH:mm:ss");
            AllCost allCost = allCostMapper.selectCurrentCoin(ddTime);
            if (allCost != null) {
                Long rmbCurrent = allCost.getDdcurrent();
                //剩余金额
                recharge.setRemainAmount(rmbCurrent.intValue() / 100);
            } else {
                recharge.setRemainAmount(0);
            }
            int cashOutCurrent = rechargeMapper.selectCashOut(dduid, ddTime);
            //已提现金额
            recharge.setDdrmbed(new BigDecimal(cashOutCurrent));
            Integer programType = recharge.getProgramType();
            if (programType == 1 || programType == 2) {
                rechargeList.add(recharge);
            }
        }
        return rechargeList;
    }

    /**
     * 默认排序
     *
     * @param parameter
     * @return
     */
    @Override
    public void setDefaultSort(GetParameter parameter) {
        if (parameter.getOrder() != null) {
            return;
        }
        parameter.setSort("ddtimes");
        parameter.setOrder("desc");
    }

    @Override
    public Class<Recharge> getClassInfo() {
        return Recharge.class;
    }

    /**
     * 筛选
     *
     * @param recharge
     * @return
     */
    @Override
    public boolean removeIf(Recharge recharge, JSONObject searchData) {
        if (existValueFalse(searchData.getString("uid"), recharge.getDduid())) {
            return true;
        }
        if (existValueFalse(searchData.getString("userName"), recharge.getUserName())) {
            return true;
        }
        if (existValueFalse(searchData.getString("productName"), recharge.getDdappid())) {
            return true;
        }
        return (existValueFalse(searchData.getString("ddStatus"), recharge.getDdstatus()));
    }

    /**
     * 提现相关接口
     * 企业付款到零钱
     *
     * @param orderid  订单编号
     * @param amount   提现金额
     * @param wxConfig 提现绑定appId
     * @param openid   提现用户
     * @param desc     提现描述
     * @return 提现结果
     */
    private Map<String, String> recharge(String orderid, int amount, com.fish.dao.second.model.WxConfig wxConfig, String openid, String desc, String ip) {
        Map<String, String> signMap = new HashMap<>();
        signMap.put("mch_appid", wxConfig.getDdappid());
        signMap.put("mchid", wxConfig.getDdmchid());
        if (!WxConfig.DEVICE_INFO.isEmpty()) {
            signMap.put("device_info", WxConfig.DEVICE_INFO);
        }
        signMap.put("nonce_str", createNonceStr());
        signMap.put("partner_trade_no", orderid);
        signMap.put("openid", openid);
        signMap.put("check_name", WxConfig.CHECK_NAME.name());
        signMap.put("re_user_name", "default");
        signMap.put("amount", String.valueOf(amount));
        if (desc != null) {
            signMap.put("desc", desc);
        } else {
            signMap.put("desc", WxConfig.DESC);
        }
        signMap.put("spbill_create_ip", ip);
        SignatureAlgorithm algorithm = new SignatureAlgorithm(wxConfig.getDdkey(), signMap);
        String xml = algorithm.getSignXml();
        try {
            String result = CmTool.sendHttps(xml, WxConfig.TRANSFERS_URL, RechargeService.class.getResource("/").getPath() + "static/" + wxConfig.getDdp12(), wxConfig.getDdp12password());
            System.out.println(result);
            XMLHandler handler = XMLHandler.parse(result);
            return handler.getXmlMap();
        } catch (Exception e) {
            LOG.error(Log4j.getExceptionInfo(e));
        }
        return null;
    }

    /**
     * 微信提现接口调用
     *
     * @param productInfo
     * @return
     */
    public int getCash(List<Recharge> productInfo) {
        int index = 0;
        int error = 0;
        int errorCount = 0;
        for (Recharge recharge : productInfo) {
            Integer programType = recharge.getProgramType();
            if (programType == 1) {
                String dduid = recharge.getDduid();
                String ddappid = recharge.getDdappid();
                UserApp userApp = userAppMapper.searchOppenId(dduid, ddappid);
                String oppenId = "";
                if (userApp != null) {
                    oppenId = userApp.getDdoid();
                }
                String orderId = recharge.getDdid();
                BigDecimal num100 = new BigDecimal("100");
                BigDecimal ddrmb = recharge.getDdrmb().multiply(num100);
                int rmb = ddrmb.intValue();
                com.fish.dao.second.model.WxConfig wxConfig = wxConfigMapper.selectByPrimaryKey(ddappid);
                LOG.info("当前订单提现金额：" + rmb / 100 + "元, 用户Uid ：" + dduid + " ,产品名 : " + wxConfig.getProductName());
                Map<String, String> backCharge = recharge(orderId, rmb, wxConfig, oppenId, wxConfig.getProductName() + "-赛事奖金提现" + rmb / 100 + "元", "129.211.119.249");
                String returnCode = backCharge != null ? backCharge.get("return_code") : null;
                if ("FAIL".equals(returnCode)) {
                    String returnMsg = backCharge.get("return_msg");
                    recharge.setDdtip(returnMsg);
                    recharge.setDdstatus(2);
                    recharge.setDdtrans(new Timestamp(System.currentTimeMillis()));
                    error = rechargeMapper.updateByPrimaryKeySelective(recharge);
                    errorCount = errorCount + error;
                    LOG.info("提现返回结果 :" + returnCode);
                } else if ("SUCCESS".equals(returnCode)) {
                    String resultCode = backCharge.get("result_code");
                    if ("SUCCESS".equals(resultCode)) {
                        recharge.setDdstatus(200);
                        recharge.setDdtip("");
                        recharge.setDdtrans(new Timestamp(System.currentTimeMillis()));
                        rechargeMapper.updateByPrimaryKeySelective(recharge);
                        index++;
                    } else if ("FAIL".equals(resultCode)) {
                        String errCode = backCharge.get("err_code");
                        recharge.setDdtip(errCode);
                        recharge.setDdstatus(2);
                        recharge.setDdtrans(new Timestamp(System.currentTimeMillis()));
                        error = rechargeMapper.updateByPrimaryKeySelective(recharge);
                        errorCount = errorCount + error;
                        LOG.info("提现返回结果 :" + returnCode);
                    }
                }
            }
        }
        return 200;
    }
}