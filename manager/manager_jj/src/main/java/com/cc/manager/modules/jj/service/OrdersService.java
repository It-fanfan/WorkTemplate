package com.cc.manager.modules.jj.service;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseStatsService;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.common.result.StatsListParam;
import com.cc.manager.common.result.StatsListResult;
import com.cc.manager.modules.jj.config.CmTool;
import com.cc.manager.modules.jj.config.JjConfig;
import com.cc.manager.modules.jj.config.SignatureAlgorithm;
import com.cc.manager.modules.jj.config.XMLHandler;
import com.cc.manager.modules.jj.entity.GoodsValueExt;
import com.cc.manager.modules.jj.entity.Orders;
import com.cc.manager.modules.jj.entity.UserInfo;
import com.cc.manager.modules.jj.entity.WxConfig;
import com.cc.manager.modules.jj.mapper.OrdersMapper;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cc.manager.modules.jj.config.CmTool.createNonceStr;


/**
 * @author cf
 * @since 2020-05-13
 */
@Service
@DS("jj")
public class OrdersService extends BaseStatsService<Orders, OrdersMapper> {

    // 签名类型
    private static final String SIGN_TYPE = "MD5";

    private JjConfig jjConfig;
    private WxConfigService wxConfigService;
    private GoodsValueExtService goodsValueExtService;

    private UserInfoService userInfoService;


    @Override
    protected void updateGetListWrapper(StatsListParam statsListParam, QueryWrapper<Orders> queryWrapper, StatsListResult statsListResult) {
        List<String> uidList = new ArrayList<>();
        String times = statsListParam.getQueryObject().getString("times");
        String tradeNumber = statsListParam.getQueryObject().getString("tradeNumber");
        String uid = statsListParam.getQueryObject().getString("uid");
        String appId = statsListParam.getQueryObject().getString("appId");
        String openId = statsListParam.getQueryObject().getString("openID");
        String payState = statsListParam.getQueryObject().getString("payState");
        String userName = statsListParam.getQueryObject().getString("userName");
        if (StringUtils.isNotBlank(times)) {
            String[] timeRangeArray = StringUtils.split(times, "~");
            queryWrapper.between("DATE(ddTime)", timeRangeArray[0].trim(), timeRangeArray[1].trim());
        }
        if (StringUtils.isNotBlank(userName)) {
            List<UserInfo> userInfos = this.userInfoService.getUserInfoListByUserName(userName);
            for (UserInfo userInfo : userInfos) {
                uidList.add(userInfo.getDdUid());
            }
            queryWrapper.in(uidList.size() > 1, "ddUid", uidList);
        }
        queryWrapper.eq(StringUtils.isNotBlank(appId), "ddAppId", appId);
        queryWrapper.like(StringUtils.isNotBlank(tradeNumber), "ddId", tradeNumber);
        queryWrapper.like(StringUtils.isNotBlank(uid), "ddUid", uid);
        queryWrapper.like(StringUtils.isNotBlank(openId), "ddOId", openId);
        queryWrapper.eq(StringUtils.isNotBlank(payState), "ddState", payState);
        queryWrapper.orderByDesc("ddTime");
    }

    /**
     * 查询充值信息
     *
     * @param appId     appId
     * @param beginDate beginDate
     * @param endDate   endDate
     * @return List
     */
    public List<Orders> list(String appId, String beginDate, String endDate) {
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("DATE(ddTrans) as ddTrans", "ddAppId", "SUM(ddPrice) as ddPrice");
        queryWrapper.eq(StringUtils.isNotBlank(appId), "ddAppId", appId).eq("ddState", 1);
        queryWrapper.between("DATE(ddTrans)", beginDate, endDate);
        queryWrapper.groupBy("DATE(ddTrans)", "ddAppId");
        return this.list(queryWrapper);
    }

    @Override
    protected JSONObject rebuildStatsListResult(StatsListParam statsListParam, List<Orders> entityList, StatsListResult statsListResult) {
        for (Orders order : entityList) {
            String ddAppId = order.getDdAppId();
            WxConfig wxConfig = this.wxConfigService.getCacheEntity(WxConfig.class, ddAppId);
            if (wxConfig != null) {
                order.setProductName(wxConfig.getProductName());
                order.setProductType(wxConfig.getProgramType());
                order.setOriginName(wxConfig.getOriginName());
            }
            UserInfo userInfo = this.userInfoService.getUserInfoByUuid(order.getDdUid());
            if (userInfo != null) {
                order.setUserName(userInfo.getDdName());
            }
            Integer goodId = order.getDdGId();
            GoodsValueExt goodsValueExt = this.goodsValueExtService.getCacheEntity(GoodsValueExt.class, goodId.toString());
            if (goodsValueExt != null) {
                String goodName = goodsValueExt.getDdName();
                String ddDesc = goodsValueExt.getDdDesc();
                order.setGoodsName(goodName);
                order.setDdDesc(ddDesc);
            }
        }
        return null;
    }

    @Autowired
    public void setWxConfigService(WxConfigService wxConfigService) {
        this.wxConfigService = wxConfigService;
    }

    @Autowired
    public void setUserInfoService(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @Autowired
    public void setGoodsValueExtService(GoodsValueExtService goodsValueExtService) {
        this.goodsValueExtService = goodsValueExtService;
    }

    @Autowired
    public void setJjConfig(JjConfig jjConfig) {
        this.jjConfig = jjConfig;
    }

    /**
     * 补发订单
     *
     * @param singleOrder singleOrder
     * @return int
     */
    public PostResult supplementOrders(JSONObject singleOrder) {
        PostResult postResult = new PostResult();
        String appId = singleOrder.getString("appId");
        String orderId = singleOrder.getString("orderId");
        //查询订单产品信息
        Orders order = this.getById(orderId);
        WxConfig wxConfig = this.wxConfigService.getCacheEntity(WxConfig.class, appId);
        Map<String, String> stringStringMap = searchPayOrder(appId, wxConfig.getDdMchId(), orderId);
        LOGGER.info("补单状态status :" + orderIsSuccess(stringStringMap) + "-appId :" + appId + "-orderId :" + orderId);
        if (orderIsSuccess(stringStringMap)) {
            Orders updateOrder = new Orders();
            updateOrder.setDdTrans(null);
            this.update(updateOrder, new UpdateWrapper<Orders>().eq("ddId", order.getId()));
            String supplementUrl = this.jjConfig.getSupplementUrl();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("version", "2.2.2");
            jsonObject.put("appId", appId);
            jsonObject.put("uid", singleOrder.getString("uid"));
            jsonObject.put("orderid", orderId);
            //验单结果
            String result = HttpUtil.post(supplementUrl, jsonObject.toString());
            //订单状态
            if (StringUtils.equals(JSONObject.parseObject(result).getString("result"), "success")) {
                String ddOrder = stringStringMap != null ? stringStringMap.get("transaction_id") : null;
                order.setDdState(1);
                order.setDdOrder(ddOrder);
                this.updateById(order);
            } else {
                postResult.setCode(2);
                postResult.setMsg("补单失败，数据处理异常！");
            }
        } else {
            postResult.setCode(2);
            postResult.setMsg("补单失败，此单未交易成功！");
        }
        return postResult;
    }

    /**
     * 查询微信支付订单
     *
     * @param orderId 订单号
     */
    private Map<String, String> searchPayOrder(String ddAppId, String ddMchId, String orderId) {
        // 查询订单在数据库中是否存在
        // 封装查询订单参数
        Map<String, String> searchOrderMap = new HashMap<>(16);
        searchOrderMap.put("appid", ddAppId);
        searchOrderMap.put("mch_id", ddMchId);
        searchOrderMap.put("out_trade_no", orderId);
        searchOrderMap.put("nonce_str", createNonceStr());
        searchOrderMap.put("sign_type", SIGN_TYPE);
        WxConfig wxConfig = this.wxConfigService.getCacheEntity(WxConfig.class, ddAppId);
        SignatureAlgorithm signatureAlgorithm = new SignatureAlgorithm(wxConfig.getDdKey(), searchOrderMap);
        String searchOrderXml = signatureAlgorithm.getSignXml();
        try {
            // 从微信平台里查询支付订单
            String searchResultXml = CmTool.sendHttps(searchOrderXml, this.jjConfig.getWxQueryUrl(), OrdersService.class.getResource("/").getPath() + "static/" + wxConfig.getDdP12(), wxConfig.getDdP12Password());
            XMLHandler parse = XMLHandler.parse(searchResultXml);
            return parse.getXmlMap();
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }
        return null;
    }

    /**
     * 检测是否匹配
     *
     * @param map 内容
     * @param key 查询参数
     * @return 是否匹配
     */
    private static boolean existResult(Map<String, String> map, String key) {
        String resultCode = map.get(key);
        return "success".equalsIgnoreCase(resultCode);
    }

    /**
     * 订单是否成功
     *
     * @param orderMap 订单回调内容
     * @return 结果
     */
    private boolean orderIsSuccess(Map<String, String> orderMap) {
        return existResult(orderMap, "result_code") && existResult(orderMap, "return_code") && existResult(orderMap, "trade_state");
    }

    /**
     * @return 获取小程序充值金额Map
     */
    public List<Orders> queryProgramReChargeCount() {
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("ddState", 1).groupBy("DATE(ddTrans)");
        List<String> selectList = Lists.newArrayList("DATE(ddTrans) as ddTrans", "SUM(ddPrice) as ddPrice");
        // 将查询字段和分组字段赋值给查询条件
        queryWrapper.select(selectList.toArray(new String[0]));
        return this.list(queryWrapper);
    }

    /**
     * 查询实时付费数据
     *
     * @param beginDate beginDate
     * @param endDate   endDate
     * @return List
     */
    public List<Orders> queryBuyStatistic(String beginDate, String endDate) {
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        queryWrapper.between(" DATE(ddTrans)", beginDate, endDate);
        queryWrapper.ne("ddOrder", "null").groupBy("DATE(ddTrans)", "ddAppId").orderByAsc("DATE(ddTrans)");
        List<String> selectList = Lists.newArrayList("DATE(ddTrans) AS ddTrans", "SUM(ddPrice) AS ddPrice",
                "COUNT(DISTINCT ddUid) AS payUsers", "ddAppId");
        // 将查询字段和分组字段赋值给查询条件
        queryWrapper.select(selectList.toArray(new String[0]));
        return this.list(queryWrapper);
    }

}
