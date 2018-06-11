package com.tech.analysis.service;
import com.tech.analysis.Dao.*;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by XCY on 2018/3/26.
 */
@Service

public class KeywordsService {
    @Autowired
    private GetCommunity getCommunity;

    @Autowired
    private CreatGraphAboutYear creatGraphAboutYear;
    @Autowired
    KeywordsPrediction keywordsPrediction;
    /**
     * @param target 精查找的词语
     * @return 查找到的json字符串
     */
    public String getTargetDependKeywords(String target){
        return getCommunity.getJsonStringCommunity(target);
    }

    /**
     *
     * @return 返回5年的图谱
     */
    public String getYearGraph(){
        return creatGraphAboutYear.creatAll();
    }

    /**
     * 返回json数组 [{name：人工智能,size：884},.....]
     */
    public String getKeyAndTimes(){
        return getCommunity.getJsonArrayForKeywordsAndTimes();
    }

    public String predictYear(){
        return keywordsPrediction.getPredictionKeyword();
    }
}
