package com.fish.service;

import com.fish.dao.primary.mapper.ArcadeGamesMapper;
import com.fish.dao.primary.model.ArcadeGameSet;
import com.fish.dao.primary.model.ArcadeGames;
import com.fish.dao.second.mapper.WxConfigMapper;
import com.fish.dao.second.model.WxConfig;
import com.fish.protocols.GetParameter;
import com.fish.utils.HexToStringUtil;
import com.fish.utils.XwhTool;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class GamesSetService implements BaseService<ArcadeGameSet> {
    @Autowired
    com.fish.dao.primary.mapper.ArcadeGameSetMapper arcadeGameSetMapper;
    @Autowired
    ArcadeGamesMapper arcadeGamesMapper;
    @Autowired
    WxConfigMapper wxConfigMapper;

    @Override
    //查询展示所有产品信息
    public List<ArcadeGameSet> selectAll(GetParameter parameter) {
        List<ArcadeGameSet> arcadeGameSets = new ArrayList<>();
        if ("asc".equals(parameter.getOrder())) {
            arcadeGameSets = arcadeGameSetMapper.selectAllByAsc();
        } else if ("desc".equals(parameter.getOrder())) {
            arcadeGameSets = arcadeGameSetMapper.selectAllByDesc();
        } else {
            arcadeGameSets = arcadeGameSetMapper.selectAll();
        }
        for (ArcadeGameSet arcadeGameSet : arcadeGameSets) {
            String name = arcadeGameSet.getDdname();
            String desc = arcadeGameSet.getDddesc();
            String content = arcadeGameSet.getDdcontent512a();
            String ddContents = "";
            List<String> gameBox = new ArrayList<>();
            try {
                String[] split = content.split("#");
                for (int i = 0; i < split.length; i++) {
                    String ddCode = split[i];
                    if (StringUtils.isNotBlank(ddCode)) {

                        gameBox.add(ddCode);
                        ArcadeGames arcadeGames = arcadeGamesMapper.selectByPrimaryKey(Integer.valueOf(ddCode));
                        String ddname = StringUtils.isBlank(arcadeGames.getDdname()) ? "" :
                                arcadeGames.getDdname();
                        if (ddContents.length() > 0) {
                            ddContents = ddContents + ",";
                        }
                        ddContents = ddContents + ddname;

                    }
                    arcadeGameSet.setGameBox(gameBox);
                }

                arcadeGameSet.setDdname(name);
                if (desc != null) {
                    arcadeGameSet.setDddesc(desc);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            arcadeGameSet.setDdcontent512a(ddContents);
            String ddappid = arcadeGameSet.getDdappid();
            WxConfig wxConfigName = wxConfigMapper.selectByPrimaryKey(ddappid);
            if(wxConfigName !=null ){
                if(StringUtils.isNotBlank(wxConfigName.getProductName())){
                    arcadeGameSet.setDdappid(arcadeGameSet.getDdappid() + "-" + wxConfigName.getProductName());
                }
            }else {
                arcadeGameSet.setDdappid(arcadeGameSet.getDdappid());
            }
        }
        return arcadeGameSets;
    }

    //新增展示所有产品信息
    public int insert(ArcadeGameSet record) {
        //新增判断是否游戏编号重复
        ArcadeGameSet arcadeGameSet = arcadeGameSetMapper.selectByPrimaryKey(record.getDdcode());
        if (!org.springframework.util.StringUtils.isEmpty(arcadeGameSet)) {
            return 5;
        }
        List<String> gameBox = new ArrayList<>();
        String select = record.getSelect();
        String[] split = select.split(",");
        for (int i = 0; i < split.length; i++) {
            gameBox.add(split[i]);
        }
        String ddContents = "";
        String ddName = "";
        if (gameBox.size() >= 1) {
            for (String box : gameBox) {
                if (!"".equals(box)) {
                    ArcadeGames arcadeGames = arcadeGamesMapper.selectByPrimaryKey(Integer.valueOf(box));
                  //  String ddName128u = HexToStringUtil.getStringFromHex(arcadeGames.getDdname());
                    String ddname = arcadeGames.getDdname();
                    if (ddName.length() > 0) {
                        ddName = ddName + ",";
                    }
                    ddName = ddName + ddname;
                    if (ddContents.length() > 0) {
                        ddContents = ddContents + "#";
                    }
                    ddContents = ddContents + box;
                }
            }
        }
        record.setDdcontent512a(ddContents);
       // record.setDdname128u(HexToStringUtil.getStringToHex(record.getDdname()));
        if (ddName != null) {
            record.setDddesc512u(HexToStringUtil.getStringToHex(ddName));
            record.setDddesc(ddName);
        }
        if (StringUtils.isNotBlank(record.getJumpDirect())) {
            String jumpDirect = record.getJumpDirect();
            String[] split1 = jumpDirect.split("-");
            record.setDdappid(split1[0]);
            String s = split1[0];
            WxConfig wxConfig = wxConfigMapper.selectByPrimaryKey(s);
            record.setDdappid(s);
            if (StringUtils.isNotBlank(wxConfig.getProductName())) {
                String productName = wxConfig.getProductName();
                record.setDdname(productName);
            }
        }
        System.out.println("我是插入合集内容 :" + XwhTool.getJSONByFastJSON(record));
        return arcadeGameSetMapper.insert(record);
    }

    //更新产品信息
    public int updateByPrimaryKeySelective(ArcadeGameSet record) {
        //新增判断是否游戏编号重复
//        ArcadeGameSet  arcadeGameSet= arcadeGameSetMapper.selectByPrimaryKey(record.getDdcode());
//        if (!org.springframework.util.StringUtils.isEmpty(arcadeGameSet)) {
//            return 5;
//        }
        List<ArcadeGameSet> arcadeGameSets = arcadeGameSetMapper.selectAll();
        List<ArcadeGameSet> arcadeGameSetOthers = new ArrayList<>();
        for (ArcadeGameSet arcadeGameSetSingle : arcadeGameSets) {
            if (record.getId() != arcadeGameSetSingle.getId())
                arcadeGameSetOthers.add(arcadeGameSetSingle);
        }
        for (ArcadeGameSet arcadeGameSetOther : arcadeGameSetOthers) {
            if (record.getDdcode().equals(arcadeGameSetOther.getDdcode()))
                return 5;
        }
        List<String> gameBox = new ArrayList<>();
        String select = record.getSelect();

        String[] split = select.split(",");
        for (int i = 0; i < split.length; i++) {
            gameBox.add(split[i]);
        }
        String ddContents = "";
        String ddName = "";
        if (gameBox.size() >= 1) {
            for (String box : gameBox) {
                if (!"".equals(box)) {
                    ArcadeGames arcadeGames = arcadeGamesMapper.selectByPrimaryKey(Integer.valueOf(box));
                   // String ddName128u = HexToStringUtil.getStringFromHex(arcadeGames.getDdname128u());
                    String ddname = arcadeGames.getDdname();
                    if (ddName.length() > 0) {
                        ddName = ddName + ",";
                    }
                    ddName = ddName + ddname;
                    if (ddContents.length() > 0) {
                        ddContents = ddContents + "#";
                    }
                    ddContents = ddContents + box;
                }
            }
        }
        System.out.println("我是修改ddContents :" + XwhTool.getJSONByFastJSON(record));

        record.setDdcontent512a(ddContents);
        //record.setDdname128u(HexToStringUtil.getStringToHex(record.getDdname128u()));
        if (ddName != null) {
            //record.setDddesc512u(HexToStringUtil.getStringToHex(ddName));
            record.setDddesc(ddName);
        }
        if (StringUtils.isNotBlank(record.getJumpDirect())) {
            String jumpDirect = record.getJumpDirect();
            String[] split1 = jumpDirect.split("-");
            String s = split1[0];
            WxConfig wxConfig = wxConfigMapper.selectByPrimaryKey(s);
            record.setDdappid(s);
        }else {
            record.setDdappid("");
        }
        return arcadeGameSetMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public void setDefaultSort(GetParameter parameter) {
        if (parameter.getOrder() != null)
            return;
        parameter.setOrder("desc");
        parameter.setSort("ddcode");
    }

    @Override
    public Class<ArcadeGameSet> getClassInfo() {
        return null;
    }

    @Override
    public boolean removeIf(ArcadeGameSet arcadeGameSet, Map<String, String> searchData) {


        if (existValueFalse(searchData.get("gameId"), arcadeGameSet.getDdcode())) {
            return true;
        }


        return (existValueFalse(searchData.get("gameName"), arcadeGameSet.getDdname()));


    }

}
