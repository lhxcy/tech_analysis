package com.tech.analysis.entity;

import com.tech.analysis.Dao.UtilWrite;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhzy on 18-5-21.
 */
public class QueryEntity {
    public void method(){
        List<String> list = new ArrayList<>();
        String[] strings = {"人工智能","机器学习","云计算","物联网","信息技术",
                "多媒体","云南","互联网+","故障诊断","数据挖掘","自动化","量子计算"};
        for (String string : strings)
            list.add(string);
        UtilWrite.WriteQueryEntity(list);
    }
}
