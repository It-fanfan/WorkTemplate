package com.cc.manager.common.result;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;

import java.io.Serializable;

/**
 * CRUD操作中的分页查询响应结果
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-04-28 17:51
 */
@Data
public class CrudPageResult implements Serializable {
    /**
     * 查询结果，0-成功，1-失败
     */
    private int code = 0;
    /**
     * 查询信息
     */
    private String msg = "查询成功！";
    /**
     * 数据总量
     */
    private long count;
    /**
     * 查询结果
     */
    private JSONArray data;
}
