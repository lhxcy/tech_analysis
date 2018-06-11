package com.tech.analysis.controller;

import com.tech.analysis.entity.Enterprise;
import com.tech.analysis.service.KeywordsService;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by XCY on 2018/3/26.
 */
@CrossOrigin
@RestController
@RequestMapping("/graph")
public class KeywordsController {
    @Autowired
    private KeywordsService keywordsService;
//    private KeywordsService keywordsService = new KeywordsService();
    @RequestMapping("/getKeywords")
    //得到精确查找字符串
    public String getKeywords(@RequestParam String target){
        //String target = (String) map.get("target");//目标词汇

        System.out.println(target);
        return keywordsService.getTargetDependKeywords(target);
    }


    @RequestMapping("/getKeyAndTimes")
    public String getKeyAndTimes(){
        return keywordsService.getKeyAndTimes();
    }

    @RequestMapping("/yearKeywords")//年份
    //得到历年关键词图
    public String getKeywordsList(){

        return  keywordsService.getYearGraph();
    }
    @RequestMapping("/predict")
    //得到预测结果
    public String predict(){
        return keywordsService.predictYear();
    }


}
