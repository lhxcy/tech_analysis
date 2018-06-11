package com.tech.analysis.Dao;

import com.tech.analysis.entity.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by XCY on 2018/5/23.
 */
public class GenerateHashTable {
    public List<String> getKeyWords(String line){//获取关键字列表
        List<String> keywords = new ArrayList<String>();
//        String file = "E:\\PycharmCode\\Test\\paper.dat";

//        System.out.println(line);
        String[] strings = line.trim().split(",");
        if (strings.length > 1){
            for (int i = 1; i < strings.length; i++)
                keywords.add(strings[i]);
        }
//        for (String str : strings)
//            keywords.add(str);
//        System.out.println(keywords);

        return keywords;
    }
    public void generate(String file) {
        try {
            HashMap<String,Long> keywordTimes = new HashMap<String, Long>();
            HashMap<String, KeywordEntity> keywords = new HashMap<String, KeywordEntity>();
            HashMap<String, RelationshipEntity> relationships = new HashMap<String, RelationshipEntity>();
            long nodeId = 0l;
            long relationshipId = 0l;
//            in = utilRead.getBufferedReaderForJson(file);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line = "";
            while (true) {
                line = bufferedReader.readLine();
                if (line == null || line.trim().equals("")) {
                    break;
                }

//                获取keyword实体
                List<String> getKeywords = getKeyWords(line);
//                List<String> getKeywords = convertToNode.getKeyWords(objectLine);
                List<Long> keywordIdList = new ArrayList<Long>();
                for(String keyword: getKeywords) {
                    if (!keywordTimes.containsKey(keyword)){
                        keywordTimes.put(keyword,1l);
                    }else {
                        keywordTimes.put(keyword,keywordTimes.get(keyword)+1);
                    }
                    Long keywordId;
                    if (!keywords.containsKey(keyword)) {
                        KeywordEntity keywordEntity =
                                new KeywordEntity(keyword, nodeId);
                        keywordId = nodeId;
                        keywords.put(keyword, keywordEntity);
                        nodeId++;
                    } else {
                        keywordId = keywords.get(keyword).getId();
                    }
                    keywordIdList.add(keywordId);
                }


                //建立关键词之间的关系
                for (int i = 0; i < keywordIdList.size(); i++) {
                    Long keywordId1 = keywordIdList.get(i);
                    for (int j = i + 1; j < keywordIdList.size(); j++) {
                        Long keywordId2 = keywordIdList.get(j);
                        String keywordRelationshipHashKey1 = keywordId1.toString() + "_" + keywordId2.toString();
                        String keywordRelationshipHashKey2 = keywordId2.toString() + "_" + keywordId1.toString();
                        if (!relationships.containsKey(keywordRelationshipHashKey1) && !keywords.containsKey(keywordRelationshipHashKey2)) {
                            RelationshipEntity relationshipEntity =
                                    new RelationshipEntity(keywordId1, keywordId2, 1l, RelationshipTypes.similar);
                            relationships.put(keywordRelationshipHashKey1, relationshipEntity);
                            relationshipId++;
                        } else if (relationships.containsKey(keywordRelationshipHashKey1)) {
                            relationships.get(keywordRelationshipHashKey1).setTimes(relationships.get(keywordRelationshipHashKey1).getTimes() + 1l);
                        } else if (relationships.containsKey(keywordRelationshipHashKey2)) {
                            relationships.get(keywordRelationshipHashKey2).setTimes(relationships.get(keywordRelationshipHashKey2).getTimes() + 1l);
                        }
                    }
                }
            }
            System.out.println("keywordTime size : " + keywordTimes.size());
            System.out.println("keywords size : " + keywords.size());
            System.out.println("relationships size : " + relationships.size());
            //写入文件中
            UtilWrite.WriteKeywordFile(keywords);
            UtilWrite.WriteRelationFile(relationships);
//            UtilWrite.WriteKeywordTimesFile(keywordTimes);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generate1() {
        try {
            //载入数据
            HashMap<String,LinkedList<String>> authorsCooperate = UtilRead.readExpertCooperateObject();
            HashMap<String,LinkedList<String>> enterpriseAndExpert = UtilRead.readEnterpriseAndExpertObject();
            HashMap<String,String> name2Enterprise = UtilRead.readName2EnterpriseObject();
            HashMap<String,LinkedList<String>> paperInstitution = UtilRead.readPaperInstitutionObject();

            //定义所需构造对象
            HashMap<String, AuthorEntity> authors = new HashMap<String, AuthorEntity>();
            HashMap<String, InstitutionEntity> institutions = new HashMap<String, InstitutionEntity>();
            HashMap<String, RelationshipEntity> relationships = new HashMap<String, RelationshipEntity>();
////            HashMap<String, JournalEntity> journals = new HashMap<String, JournalEntity>();
//            HashMap<String, KeywordEntity> keywords = new HashMap<String, KeywordEntity>();
//            HashMap<String, PaperEntity> papers = new HashMap<String, PaperEntity>();
            long nodeId = 0l;
            long relationshipId = 0l;

            for (String uid : authorsCooperate.keySet()){
                //构建机构关系
                //存储要建立关系的institution实体Id
                List<Long> institutionIdList = new ArrayList<Long>();
                for (String institutionOfThisPaper : paperInstitution.get(uid)){
                    ////往institution表中添加元素
                    if (!institutions.containsKey(institutionOfThisPaper)){
                        InstitutionEntity institutionEntity = new InstitutionEntity(institutionOfThisPaper,nodeId);
                        institutionIdList.add(nodeId);
                        institutions.put(institutionOfThisPaper,institutionEntity);
                        ++nodeId;
                    }else {
                        Long insititionId = institutions.get(institutionOfThisPaper).getId();
                        institutionIdList.add(insititionId);
                    }
                }

                //建立institution之间的关系 权重为合作次数
                for (int i = 0; i < institutionIdList.size(); i++) {
                    Long institutionId1 = institutionIdList.get(i);
                    for (int j = i + 1; j < institutionIdList.size(); j++) {
                        Long institutionId2 = institutionIdList.get(j);
                        String relationshipHashKey1 = institutionId1.toString() + "_" + institutionId2.toString();
                        String relationshipHashKey2 = institutionId2.toString() + "_" + institutionId1.toString();
                        if (!relationships.containsKey(relationshipHashKey1) && !relationships.containsKey(relationshipHashKey2)) {
                            RelationshipEntity relationshipEntity = new RelationshipEntity(institutionId1, institutionId2, 1l, RelationshipTypes.cooperate);
                            relationships.put(relationshipHashKey1, relationshipEntity);
                            relationshipId++;
                        } else if (relationships.containsKey(relationshipHashKey1)) {
                            relationships.get(relationshipHashKey1).setTimes(relationships.get(relationshipHashKey1).getTimes() + 1l);//长整型1次
                        } else if (relationships.containsKey(relationshipHashKey2)) {
                            relationships.get(relationshipHashKey2).setTimes(relationships.get(relationshipHashKey2).getTimes() + 1l);
                        }
                    }
                }

                /*构建作者关系*/
                //存储要建立关系的author实体Id
                List<Long> authorIdList = new ArrayList<Long>();
                for (String authorName : authorsCooperate.get(uid)){
                    String autherInstitution = name2Enterprise.get(authorName);
                    if (autherInstitution == null) continue;
                    String hashKey = authorName + autherInstitution;
                    Long authorId;
                    if (!authors.containsKey(hashKey)){
                        AuthorEntity authorEntity = new AuthorEntity(authorName,autherInstitution,nodeId);
                        authorId = nodeId;
                        authorIdList.add(authorId);
                        authors.put(hashKey,authorEntity);
                        ++nodeId;
                    }else {
                        authorId = authors.get(hashKey).getId();
                        authorIdList.add(authorId);
                    }
                    //建立作者机构关系 权重为1
                    String authorInstitutionRelationshipHashKey = authorId.toString()+"_"+institutions.get(autherInstitution).getId().toString();
                    if(!relationships.containsKey(authorInstitutionRelationshipHashKey)){
                        RelationshipEntity relationshipEntity =
                                new RelationshipEntity(authorId, institutions.get(autherInstitution).getId(), 1l, RelationshipTypes.works_in);
                        relationships.put(authorInstitutionRelationshipHashKey, relationshipEntity);
                        relationshipId ++;
                    }
                }



                //建立作者合作关系，权重为合作次数
                for (int i = 0; i < authorIdList.size(); ++i){
                    Long authorId1 = authorIdList.get(i);
                    for (int j = i + 1; j < authorIdList.size(); ++j){
                        Long authorId2 = authorIdList.get(j);
                        String relationshipHashKey1 = authorId1.toString()+"_"+authorId2.toString();
                        String relationshipHashKey2 = authorId2.toString()+"_"+authorId1.toString();
                        if (!relationships.containsKey(relationshipHashKey1) && !relationships.containsKey(relationshipHashKey2)){
                            RelationshipEntity relationshipEntity = new RelationshipEntity(authorId1,authorId2,1L, RelationshipTypes.work_together);
                            relationships.put(relationshipHashKey1,relationshipEntity);
                            ++relationshipId;
                        }else if (relationships.containsKey(relationshipHashKey1)){
                            relationships.get(relationshipHashKey1).setTimes(relationships.get(relationshipHashKey1).getTimes()+1L);
                        }else if (relationships.containsKey(relationshipHashKey2)){
                            relationships.get(relationshipHashKey2).setTimes(relationships.get(relationshipHashKey2).getTimes()+1L);
                        }
                    }
                }

            }
            System.out.println(nodeId + "=====" + relationshipId);
            System.out.println("authors size : " + authors.size());
            System.out.println("institutions size : " + institutions.size());
            System.out.println("relationships size : " + relationships.size());


            //写入文件中
            UtilWrite.WriteAuthorsNeo4jObject(authors);
            UtilWrite.WriteInstitutionsNeo4jObject(institutions);
            UtilWrite.WriteRelationshipsNeo4jObject(relationships);

        }catch (Exception e){

        }
    }
}
