package com.fish.service;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fish.dao.primary.mapper.*;
import com.fish.dao.primary.model.*;
import com.fish.dao.second.mapper.UserInfoMapper;
import com.fish.dao.second.mapper.WxConfigMapper;
import com.fish.dao.second.model.WxConfig;
import com.fish.protocols.GetParameter;
import com.fish.service.cache.CacheService;
import com.fish.utils.BaseConfig;
import com.fish.utils.ExportResult;
import com.fish.utils.RedisUtil;
import com.fish.utils.XwhTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 充值订单
 * Service
 *
 * @author
 * @date
 */
@Service
public class ProgramRankingService implements BaseService<ShowRanking> {
    @Autowired
    RankingMapper rankingMapper;
    @Autowired
    RankingRecordMapper rankingRecordMapper;
    @Autowired
    GameRoundMapper gameRoundMapper;
    @Autowired
    ArcadeGamesMapper arcadeGamesMapper;
    @Autowired
    RoundsMapper roundsMapper;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    UserInfoMapper userInfoMapper;
    @Autowired
    RoundExtMapper roundExtMapper;
    @Autowired
    RoundMatchMapper roundMatchMapper;
    @Autowired
    WxConfigMapper wxConfigMapper;

    @Autowired
    RoundRecordMapper roundRecordMapper;
    @Autowired
    BaseConfig baseConfig;
    @Autowired
    CacheService cacheService;

    /**
     * 查询排名信息
     *
     * @param parameter
     * @return
     */
    @Override
    public List<ShowRanking> selectAll(GetParameter parameter) {
        List<RoundRecord> roundRecords;
        JSONObject search = getSearchData(parameter.getSearchData());
        //判断是否包含搜索条件
        if (search == null || search.getString("times").isEmpty()) {
            roundRecords = roundRecordMapper.selectGRank();
        } else {
            Date[] parse = XwhTool.parseDate(search.getString("times"));
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            roundRecords = roundRecordMapper.selectGRankTime(format.format(parse[0]), format.format(parse[1]));
        }
        //获奖记录处理
        ArrayList<ShowRanking> shows = roundRecordsDeal(roundRecords);
        return shows;
    }

    /**
     * 获奖记录处理
     *
     * @param roundRecords
     * @return
     */
    private ArrayList<ShowRanking> roundRecordsDeal(List<RoundRecord> roundRecords) {
        ArrayList<ShowRanking> shows = new ArrayList<>();
        ShowRanking showRanking;
        for (RoundRecord roundRecord : roundRecords) {
            Date ddSubmit = roundRecord.getDdsubmit();
            Date ddStart = roundRecord.getDdstart();
            Integer ddGame = roundRecord.getDdgame();
            String ddRound = roundRecord.getDdround();
            showRanking = new ShowRanking();
            Integer ddcode = roundRecord.getDdcode();
            RoundMatch roundMatch = roundMatchMapper.selectByPrimaryKey(ddcode);
            //赛制名称
            String roundName = roundMatch.getDdname();

            RoundExt roundExt = cacheService.getRoundExt(ddRound);
            //赛制时长
            String tip = roundExt.getTip();
            String ddAppid = roundMatch.getDdappid();
            WxConfig wxConfig = cacheService.getWxConfig(ddAppid);
            //产品名称
            String productName = wxConfig.getProductName();
            ArcadeGames arcadeGames = cacheService.getGames(ddGame);
            //游戏名称
            String gameName = arcadeGames.getDdname();
            showRanking.setDdGroup(true);
            showRanking.setDdCode(ddcode);
            showRanking.setRoundName(roundName);
            showRanking.setRoundLength(tip);
            showRanking.setAppName(productName);
            showRanking.setAppId(wxConfig.getDdappid());
            showRanking.setGamesName(gameName);
            showRanking.setGamesCode(arcadeGames.getDdcode());
            showRanking.setStartTime(ddStart);
            showRanking.setEndTime(ddSubmit);
            showRanking.setRoundCode(ddRound);
            showRanking.setDdIndex(roundRecord.getDdindex());
            showRanking.setDdNumber(roundRecord.getDdresult());
            shows.add(showRanking);
        }
        return shows;
    }

    /**
     * 导出小程序比赛结果
     *
     * @param productInfo
     * @return
     */
    public List<ExportResult> selectResult(ShowRanking productInfo) {
        List<ExportResult> exportResults = new ArrayList<>();
        Integer ddCode = productInfo.getDdCode();
        Boolean ddGroup = productInfo.getDdGroup();
        Integer ddNumber = productInfo.getDdNumber();
        Date matchdate = productInfo.getEndTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //赛制结束时间
        String format = sdf.format(matchdate);
        //赛制编号
        String roundCode = productInfo.getRoundCode();
        //赛制名称
        String roundName = productInfo.getRoundName();
        //赛制时长
        String roundLength = productInfo.getRoundLength();
        int group = 0;
        int num = 0;
        //判断类型 小程序or小游戏
        if (ddGroup) {
            group = 1;
        } else {
            group = 0;
        }
        if (ddNumber != null) {
            int numbers = ddNumber;
            if (numbers <= 100) {
                num = 0;
            } else {
                num = numbers / 100;
            }
        }
        Integer ddIndex = productInfo.getDdIndex();
        String matchRes = baseConfig.getMatchRes();
        JSONArray allResult = new JSONArray();
        for (int i = 0; i <= num; i++) {
            //获取比赛结果
            String obtainResultUrl = matchRes + "match-c" + ddCode + "-g" + group + "-i" + ddIndex + "-" + i + ".json";
            String result = HttpUtil.get(obtainResultUrl);
            JSONArray singleResult = JSONArray.parseArray(result);
            allResult.addAll(singleResult);
        }
        //比赛结果处理
        exportResults = allResultDeal(allResult, format, roundName, roundLength);

        return exportResults;
    }

    /**
     * 比赛结果处理
     *
     * @param allResult
     * @param format
     * @param roundName
     * @param roundLength
     * @return
     */
    private List<ExportResult> allResultDeal(JSONArray allResult, String format, String roundName, String roundLength) {
        List<ExportResult> exportResults = new ArrayList<>();
        for (Object object : allResult) {
            ExportResult exportResult = new ExportResult();
            JSONObject jsonObject = JSONObject.parseObject(object.toString());
            Object uid = jsonObject.get("uid");
            Object index = jsonObject.get("index");
            Object name = jsonObject.get("name");
            Object ddName = jsonObject.get("ddName");
            Object value = jsonObject.get("value");
            Object type = jsonObject.get("type");
            Object mark = jsonObject.get("mark");
            int reward = Integer.parseInt(value.toString());
            if (type.toString().equals("rmb")) {
                reward = reward / 100;
            }
            exportResult.setRoundName(roundName);
            exportResult.setRoundLength(roundLength);
            exportResult.setIndex(Integer.parseInt(index.toString()));
            if (name != null) {
                exportResult.setName(name.toString());
            } else {
                if (ddName != null) {
                    exportResult.setName(ddName.toString());
                } else {
                    exportResult.setName("");
                }
            }
            exportResult.setUid(uid.toString());
            exportResult.setValue(reward);
            exportResult.setType(type.toString());
            exportResult.setMark(Integer.parseInt(mark.toString()));
            exportResult.setRoundName(roundName);
            exportResult.setRoundLength(roundLength);
            exportResult.setMatchdate(format);
            exportResults.add(exportResult);
        }
        return exportResults;
    }

    @Override
    public void setDefaultSort(GetParameter parameter) {
        if (parameter.getOrder() != null) {
            return;
        }
        parameter.setSort("endTime");
        parameter.setOrder("desc");
    }

    @Override
    public Class<ShowRanking> getClassInfo() {
        return ShowRanking.class;
    }

    @Override
    public boolean removeIf(ShowRanking record, JSONObject searchData) {

        if (existValueFalse(searchData.getString("appName"), record.getAppId())) {
            return true;
        }

        if (existValueFalse(searchData.getString("gameName"), record.getGamesCode())) {
            return true;
        }
        String roundName = searchData.getString("roundName");
        if (roundName != null && roundName.contains("-")) {
            roundName = roundName.split("-")[0];
        }
        return (existValueFalse(roundName, record.getRoundCode()));
    }

}
