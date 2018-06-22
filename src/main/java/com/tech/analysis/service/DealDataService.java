package com.tech.analysis.service;

import com.tech.analysis.Dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * 该类用于后台处理数据
 * Created by XCY on 2018/4/19.
 */

@Service

public class DealDataService {
    @Autowired
    private LoadWordAndVector loadWordAndVector;
    @Autowired
    private GenerateCSV generateCSV;
    @Autowired
    private KeywordsPrediction keywordsPrediction;
    @Autowired
    private BuildAuthorAndInstitution buildAuthorAndInstitution;
    @Autowired
    private ImportNeo4j importNeo4j;
    @Autowired
    private GetEnterpriseAndExpertData getEnterpriseAndExpertData;
//    @Autowired
//    private BuildInitNeo4j buildInitNeo4j;


    /**
     * 生成OriginalData.dat  内容是摘要和标题
     *
     */
    public void buildSimilarModel(){
        loadWordAndVector.buildModel();
    }

    /**
     * 生成关键字和关系的csv
     */
    public void getKeyWordsAndRelationCSV(){
        generateCSV.buildKeyAndRelationCSV();
    }

    /**
     * 生成预测关键字linearModelPrediction.dat
     */
    public void getPredictionKeywords(){
        keywordsPrediction.predict();
    }

    /**
     * 得到生成企业和人的关系图所需要的数据
     */
    public void buildNeo4j(){
        buildAuthorAndInstitution.getDataAndBuildCSV();
    }

    /**
     * 更新数据库
     */
    public void dealNeo4j(){
        generateCSV.buildKeyAndRelationCSV();
        importNeo4j.batch_import();
    }

    /**
     * 得到构建neo4j的基础数据
     */
//    public void getNeo4jDataBase(){
//        generateCSV.buildKeyAndRelationCSV();
//        System.out.println("得到csv数据");
//        getEnterpriseAndExpertData.getAllData();
//        System.out.println("从sql数据库取出数据");
//        importNeo4j.batch_import();
//        System.out.println("数据库构建成功！");
////        buildInitNeo4j.buildInitNeo4j();
//    }

//    public void build(){
//        buildInitNeo4j.buildInitNeo4j();
//    }

}
