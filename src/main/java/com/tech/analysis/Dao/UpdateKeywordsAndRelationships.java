package com.tech.analysis.Dao;

import com.tech.analysis.entity.KeywordEntity;
import com.tech.analysis.entity.RelationshipEntity;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

/**
 * Created by XCY on 2018/4/25.
 */
//@Repository
public class UpdateKeywordsAndRelationships {
    static {
        //存储不带年份的keywordTimes

        HashMap<String, Long> keywordTimes = UtilRead.readKeywordTimes();
        //存储不带年份的keywords
        HashMap<String, KeywordEntity> keywords = UtilRead.readKeywords();
        HashMap<String, RelationshipEntity> relationships = UtilRead.readRelationships();
        //keywords和relationships的ID

        //存储带年份的yearKeywordTimes
        HashMap<String, Long> yearKeywordTimes = UtilRead.readYearKeywordTimes();
        //存储带年份的yearKeywords
        HashMap<String, KeywordEntity> yearKeywords = UtilRead.readYearKeywords();
        HashMap<String, RelationshipEntity> yearRelationships = UtilRead.readYearRelationships();
    }


    public void Update(){//更新关键词和关系

    }

}
