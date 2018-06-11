package com.tech.analysis.controller;

import com.tech.analysis.service.DealDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by XCY on 2018/4/19.
 */
@CrossOrigin
@RestController
@RequestMapping("/model")
public class DealDataController {
    @Autowired
    private DealDataService dealDataService;

    /**
     *
     * 构建计算相似度的模型aa
     */
    @RequestMapping("/similarModel")
    public void getSimilarModel(){
        dealDataService.buildSimilarModel();
    }


    /**
     * 构建导入neo4j的keywords和relationship 的 CSV文件
     */
    @RequestMapping("/buildCSV")
    public void getCSV(){
        dealDataService.getKeyWordsAndRelationCSV();
    }


    @RequestMapping("/buildNeo4j")
    public void getData(){
        dealDataService.buildNeo4j();
    }

    /**
     * 根据历史预测关键字
     */
    @RequestMapping("/PredictionKeywords")
    public void PredictionKeywords(){
        dealDataService.getPredictionKeywords();
    }

    /**
     * 得到构建neo4j的基础数据
     */
    @RequestMapping("/getNeo4jBaseData")
    public void getNeo4jBaseData(){
        dealDataService.getNeo4jBaseData();
    }

}
