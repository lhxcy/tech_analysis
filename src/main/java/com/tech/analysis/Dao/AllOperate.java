package com.tech.analysis.Dao;

/**
 * Created by XCY on 2018/6/4.
 */
public class AllOperate {
    /**
     * 预测年图
     */
    public void predictYearGraph(){
        KeywordsPrediction keywordsPrediction = new KeywordsPrediction();
        keywordsPrediction.predict();
    }


    /**
     * 训练模型
     */
    public void getSimilarModel(){
        LoadWordAndVector loadWordAndVector = new LoadWordAndVector();
        loadWordAndVector.buildModel();
    }


    /**
     *划分社区
     */
    public void  partitionCommunity(){
        KeywordsDao keywordsDao = new KeywordsDao();
        keywordsDao.keywordPartitionCommunity();
    }
}
