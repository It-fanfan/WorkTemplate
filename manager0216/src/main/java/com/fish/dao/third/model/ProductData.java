package com.fish.dao.third.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author
 * @pragram: ProductData
 * @description: fc数据对应展示实体类
 * @create:
 */
public class ProductData {
    private String wxAppid;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date wxDate;
    private Integer programType;

    /*** 产品数量*/
    private Integer productCount;
    /*** 总收入*/
    private BigDecimal revenueCount;
    private String wxRegJson;

    private String productName;
    /*** 充值收入*/
    private BigDecimal recharge;
    /*** 广告收入*/
    private BigDecimal adRevenue;

    private BigDecimal activeUp;
    private Integer wxRegOther;

    private MinitjWx minitjWx;

    private Integer wxNew;

    private Integer wxActive;

    private Integer wxVisit;

    private BigDecimal wxAvgLogin;

    private BigDecimal wxAvgOnline;

    private BigDecimal wxRemain2;

    private Integer wxVideoShow;

    private BigDecimal wxVideoClickrate;

    private BigDecimal wxVideoIncome;

    private BigDecimal videoECPM;

    private Integer wxBannerShow;

    private BigDecimal wxBannerClickrate;

    private BigDecimal wxBannerIncome;
    private BigDecimal bannerECPM;

    private Integer wxRegAd;

    private Integer wxRegJump;

    private Integer wxRegSearch;

    private Integer wxRegApp;

    private Integer wxRegCode;

    private Integer wxRegSession;

    private BigDecimal wxActiveWomen;

    private Integer wxShareUser;

    private Integer wxShareCount;

    private BigDecimal wxShareRate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date insertTime;
    /**
     * 展示数据-买量支出
     */
    private BigDecimal buyCost;
    /**
     * 展示数据-买量单价
     */
    private BigDecimal buyClickPrice;

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     * 查询开始和结束时间
     */
    private String beginTime;
    private String endTime;

    public Integer getProductCount() {
        return productCount;
    }

    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
    }

    public Integer getProgramType() {
        return programType;
    }

    public void setProgramType(Integer programType) {
        this.programType = programType;
    }

    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }

    public String getWxAppid() {
        return wxAppid;
    }

    public void setWxAppid(String wxAppid) {
        this.wxAppid = wxAppid;
    }

    public String getWxRegJson() {
        return wxRegJson;
    }

    public void setWxRegJson(String wxRegJson) {
        this.wxRegJson = wxRegJson;
    }

    public Integer getWxNew() {
        return wxNew;
    }

    public void setWxNew(Integer wxNew) {
        this.wxNew = wxNew;
    }

    public Integer getWxActive() {
        return wxActive;
    }

    public void setWxActive(Integer wxActive) {
        this.wxActive = wxActive;
    }

    public Integer getWxVisit() {
        return wxVisit;
    }

    public void setWxVisit(Integer wxVisit) {
        this.wxVisit = wxVisit;
    }

    public BigDecimal getWxAvgLogin() {
        return wxAvgLogin;
    }

    public void setWxAvgLogin(BigDecimal wxAvgLogin) {
        this.wxAvgLogin = wxAvgLogin;
    }

    public BigDecimal getWxAvgOnline() {
        return wxAvgOnline;
    }

    public void setWxAvgOnline(BigDecimal wxAvgOnline) {
        this.wxAvgOnline = wxAvgOnline;
    }

    public BigDecimal getWxRemain2() {
        return wxRemain2;
    }

    public void setWxRemain2(BigDecimal wxRemain2) {
        this.wxRemain2 = wxRemain2;
    }

    public Integer getWxVideoShow() {
        return wxVideoShow;
    }

    public void setWxVideoShow(Integer wxVideoShow) {
        this.wxVideoShow = wxVideoShow;
    }

    public BigDecimal getWxVideoClickrate() {
        return wxVideoClickrate;
    }

    public void setWxVideoClickrate(BigDecimal wxVideoClickrate) {
        this.wxVideoClickrate = wxVideoClickrate;
    }

    public BigDecimal getWxVideoIncome() {
        return wxVideoIncome;
    }

    public void setWxVideoIncome(BigDecimal wxVideoIncome) {
        this.wxVideoIncome = wxVideoIncome;
    }

    public Integer getWxBannerShow() {
        return wxBannerShow;
    }

    public void setWxBannerShow(Integer wxBannerShow) {
        this.wxBannerShow = wxBannerShow;
    }

    public BigDecimal getWxBannerClickrate() {
        return wxBannerClickrate;
    }

    public void setWxBannerClickrate(BigDecimal wxBannerClickrate) {
        this.wxBannerClickrate = wxBannerClickrate;
    }

    public BigDecimal getWxBannerIncome() {
        return wxBannerIncome;
    }

    public void setWxBannerIncome(BigDecimal wxBannerIncome) {
        this.wxBannerIncome = wxBannerIncome;
    }

    public Integer getWxRegAd() {
        return wxRegAd;
    }

    public void setWxRegAd(Integer wxRegAd) {
        this.wxRegAd = wxRegAd;
    }

    public Integer getWxRegJump() {
        return wxRegJump;
    }

    public void setWxRegJump(Integer wxRegJump) {
        this.wxRegJump = wxRegJump;
    }

    public Integer getWxRegSearch() {
        return wxRegSearch;
    }

    public void setWxRegSearch(Integer wxRegSearch) {
        this.wxRegSearch = wxRegSearch;
    }

    public Integer getWxRegApp() {
        return wxRegApp;
    }

    public void setWxRegApp(Integer wxRegApp) {
        this.wxRegApp = wxRegApp;
    }

    public Integer getWxRegCode() {
        return wxRegCode;
    }

    public void setWxRegCode(Integer wxRegCode) {
        this.wxRegCode = wxRegCode;
    }

    public Integer getWxRegSession() {
        return wxRegSession;
    }

    public void setWxRegSession(Integer wxRegSession) {
        this.wxRegSession = wxRegSession;
    }

    public BigDecimal getWxActiveWomen() {
        return wxActiveWomen;
    }

    public void setWxActiveWomen(BigDecimal wxActiveWomen) {
        this.wxActiveWomen = wxActiveWomen;
    }

    public Integer getWxShareUser() {
        return wxShareUser;
    }

    public void setWxShareUser(Integer wxShareUser) {
        this.wxShareUser = wxShareUser;
    }

    public Integer getWxShareCount() {
        return wxShareCount;
    }

    public void setWxShareCount(Integer wxShareCount) {
        this.wxShareCount = wxShareCount;
    }

    public BigDecimal getWxShareRate() {
        return wxShareRate;
    }

    public void setWxShareRate(BigDecimal wxShareRate) {
        this.wxShareRate = wxShareRate;
    }

    public Date getWxDate() {
        return wxDate;
    }

    public void setWxDate(Date wxDate) {
        this.wxDate = wxDate;
    }

    public Integer getWxRegOther() {
        return wxRegOther;
    }

    public void setWxRegOther(Integer wxRegOther) {
        this.wxRegOther = wxRegOther;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getRecharge() {
        return recharge;
    }

    public void setRecharge(BigDecimal recharge) {
        this.recharge = recharge;
    }

    public BigDecimal getAdRevenue() {
        return adRevenue;
    }

    public void setAdRevenue(BigDecimal adRevenue) {
        this.adRevenue = adRevenue;
    }

    public BigDecimal getActiveUp() {
        return activeUp;
    }

    public void setActiveUp(BigDecimal activeUp) {
        this.activeUp = activeUp;
    }

    public MinitjWx getMinitjWx() {
        return minitjWx;
    }

    public void setMinitjWx(MinitjWx minitjWx) {
        this.minitjWx = minitjWx;
    }

    public BigDecimal getVideoECPM() {
        return videoECPM;
    }

    public void setVideoECPM(BigDecimal videoECPM) {
        this.videoECPM = videoECPM;
    }

    public BigDecimal getBannerECPM() {
        return bannerECPM;
    }

    public void setBannerECPM(BigDecimal bannerECPM) {
        this.bannerECPM = bannerECPM;
    }

    public BigDecimal getRevenueCount() {
        return revenueCount;
    }

    public void setRevenueCount(BigDecimal revenueCount) {
        this.revenueCount = revenueCount;
    }

    public BigDecimal getBuyCost() {
        return buyCost;
    }

    public void setBuyCost(BigDecimal buyCost) {
        this.buyCost = buyCost;
    }

    public BigDecimal getBuyClickPrice() {
        return buyClickPrice;
    }

    public void setBuyClickPrice(BigDecimal buyClickPrice) {
        this.buyClickPrice = buyClickPrice;
    }
}
