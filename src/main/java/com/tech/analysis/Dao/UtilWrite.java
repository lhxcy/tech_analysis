package com.tech.analysis.Dao;

import com.tech.analysis.entity.AuthorEntity;
import com.tech.analysis.entity.InstitutionEntity;
import com.tech.analysis.entity.KeywordEntity;
import com.tech.analysis.entity.RelationshipEntity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by XCY on 2018/4/18.
 */
public class UtilWrite {
    private static String basePath = System.getProperty("user.dir");
    /**
     * 将HashMap<String, double[]> wordMap 写入文件
     * @param wordMap
     */
    public static void WriteModel(HashMap<String, double[]> wordMap) {
//        String filePath = "D:/Entity/institutionEntity.dat";
//        String filePath = "/home/zhzy/Documents/data/institutionEntity.dat";
//        String filePath = "E:\\tech_analysis\\py\\model\\model.dat";
//        String filePath = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/model.dat";
    //        String filePath = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/model520286.dat";
//        String filePath = basePath+File.separator+"py"+File.separator+"model"+File.separator+"model.dat";
        String filePath = basePath+File.separator+"py"+File.separator+"model"+File.separator+"model_more.dat";
        try {
            FileOutputStream outStream = new FileOutputStream(filePath);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    outStream);
            objectOutputStream.writeObject(wordMap);
            objectOutputStream.close();
            System.out.println("模型写入成功！");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("模型写入失败！");
        }
//        System.out.println("模型写入成功！");
    }

    /**
     * 将以前构建的关键字map对象存储下来，用于追加数据是构建新的keywords CSV
     * @param keywords
     */
    public static void WriteKeywords(HashMap<String, KeywordEntity> keywords) {
//        String filePath = "D:/Entity/institutionEntity.dat";
//        String filePath = "/home/zhzy/Documents/data/institutionEntity.dat";
//        String filePath = "E:\\tech_analysis\\py\\model\\KeywordsObject.dat";
//        String filePath = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/KeywordsObject.dat";
//        String filePath = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/KeywordsObjectKey.dat";
        String filePath = basePath+File.separator+"py"+File.separator+"model"+File.separator+"KeywordsObjectKey.dat";
        try {
            FileOutputStream outStream = new FileOutputStream(filePath);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    outStream);
            objectOutputStream.writeObject(keywords);
            objectOutputStream.close();
            System.out.println("关键字对象写入成功！");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("关键字对象写入失败！");
        }
//        System.out.println("关键字写入成功！");
    }

    /**
     * 将以前构建的包含年份的关键字map对象存储下来，用于追加数据是构建新的yearKeywords CSV
     * @param yearKeywords
     */
    public static void WriteYearKeywords(HashMap<String, KeywordEntity> yearKeywords) {
//        String filePath = "D:/Entity/institutionEntity.dat";
//        String filePath = "/home/zhzy/Documents/data/institutionEntity.dat";
//        String filePath = "E:\\tech_analysis\\py\\model\\YearKeywordsObject.dat";
//        String filePath = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/YearKeywordsObject.dat";
//        String filePath = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/YearKeywordsObjectKey.dat";
        String filePath = basePath+File.separator+"py"+File.separator+"model"+File.separator+"YearKeywordsObjectKey.dat";
        try {
            FileOutputStream outStream = new FileOutputStream(filePath);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    outStream);
            objectOutputStream.writeObject(yearKeywords);
            objectOutputStream.close();
            System.out.println("包含年份的关键字对象写入成功！");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("包含年份的关键字对象写入失败！");
        }
//        System.out.println("包含年份的关键字对象写入成功！");
    }

    /**
     * 将以前构建的关系的对象存起来
     * @param relationships
     */
    public static void WriteRelationships(HashMap<String, RelationshipEntity> relationships) {
//        String filePath = "D:/Entity/institutionEntity.dat";
//        String filePath = "/home/zhzy/Documents/data/institutionEntity.dat";
//        String filePath = "E:\\tech_analysis\\py\\model\\RelationshipsObject.dat";
//        String filePath = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/RelationshipsObject.dat";
//        String filePath = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/RelationshipsObjectKey.dat";
        String filePath = basePath+File.separator+"py"+File.separator+"model"+File.separator+"RelationshipsObjectKey.dat";
        try {
            FileOutputStream outStream = new FileOutputStream(filePath);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    outStream);
            objectOutputStream.writeObject(relationships);
            objectOutputStream.close();
            System.out.println("关系对象写入成功！");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("关系对象写入失败！");
        }
//        System.out.println("关系写入成功！");
    }

    /**
     * 将以前构建的包含年份的关系的对象存储起来
     * @param yearRelationships
     */
    public static void WriteYearRelationships(HashMap<String, RelationshipEntity> yearRelationships) {
//        String filePath = "D:/Entity/institutionEntity.dat";
//        String filePath = "/home/zhzy/Documents/data/institutionEntity.dat";
//        String filePath = "E:\\tech_analysis\\py\\model\\YearRelationshipsObject.dat";
//        String filePath = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/YearRelationshipsObject.dat";
//        String filePath = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/YearRelationshipsObjectKey.dat";
        String filePath = basePath+File.separator+"py"+File.separator+"model"+File.separator+"YearRelationshipsObjectKey.dat";
        try {
            FileOutputStream outStream = new FileOutputStream(filePath);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    outStream);
            objectOutputStream.writeObject(yearRelationships);
            objectOutputStream.close();
            System.out.println("包含年份的关系对象写入成功！");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("包含年份的关系对象写入失败！");
        }
//        System.out.println("包含年份的关系写入成功！");
    }

    /**
     * 将构建的关键字次数的对象存储起来
     * @param keywordTimes
     */
    public static void WriteKeywordsTimes(HashMap<String, Long> keywordTimes) {
//        String filePath = "D:/Entity/institutionEntity.dat";
//        String filePath = "/home/zhzy/Documents/data/institutionEntity.dat";
//        String filePath = "E:\\tech_analysis\\py\\model\\KeywordsTimesObject.dat";
//        String filePath = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/KeywordsTimesObject.dat";
//        String filePath = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/KeywordsTimesObjectKey.dat";
        String filePath = basePath+File.separator+"py"+File.separator+"model"+File.separator+"KeywordsTimesObjectKey.dat";
        try {
            FileOutputStream outStream = new FileOutputStream(filePath);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    outStream);
            objectOutputStream.writeObject(keywordTimes);
            objectOutputStream.close();
            System.out.println("关键字的次数对象写入成功！");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("关键字的次数对象写入失败！");
        }
//        System.out.println("关键字的次数写入成功！");
    }

    /**
     * 将构建的包含年份的关键字次数的对象存储起来
     * @param yearKeywordTimes
     */
    public static void WriteYearKeywordsTimes(HashMap<String, Long> yearKeywordTimes) {
//        String filePath = "D:/Entity/institutionEntity.dat";
//        String filePath = "/home/zhzy/Documents/data/institutionEntity.dat";
//        String filePath = "E:\\tech_analysis\\py\\model\\YearKeywordsTimesObject.dat";
//        String filePath = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/YearKeywordsTimesObject.dat";
//        String filePath = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/YearKeywordsTimesObjectKey.dat";
        String filePath = basePath+File.separator+"py"+File.separator+"model"+File.separator+"YearKeywordsTimesObjectKey.dat";
        try {
            FileOutputStream outStream = new FileOutputStream(filePath);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    outStream);
            objectOutputStream.writeObject(yearKeywordTimes);
            objectOutputStream.close();
            System.out.println("包含年份的关键字次数对象写入成功！");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("包含年份的关键字次数对象写入失败！");
        }
//        System.out.println("包含年份的关键字次数写入成功！");
    }


    /**
     * 将构建的不包含年份的待删除关键字对象存储起来
     * @param rmKeyMap
     */
    public static void WriteRmKeyMap(HashMap<String, Long> rmKeyMap) {
//        String filePath = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/rmKeyMapObjectKey.dat";
        String filePath = basePath+File.separator+"py"+File.separator+"model"+File.separator+"rmKeyMapObjectKey.dat";
        try {
            FileOutputStream outStream = new FileOutputStream(filePath);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    outStream);
            objectOutputStream.writeObject(rmKeyMap);
            objectOutputStream.close();
            System.out.println("需要去除的关键字对象Map写入成功！");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("需要去除的关键字对象Map写入失败！");
        }
    }


    /**
     * 写入需要去除的包含年份的关键字对象Map
     * @param rmKeyMap
     */
    public static void WriteRmYearKeyMap(HashMap<String, Long> rmKeyMap) {
//        String filePath = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/rmYearKeyMapObjectKey.dat";
        String filePath = basePath+File.separator+"py"+File.separator+"model"+File.separator+"rmYearKeyMapObjectKey.dat";
        try {
            FileOutputStream outStream = new FileOutputStream(filePath);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    outStream);
            objectOutputStream.writeObject(rmKeyMap);
            objectOutputStream.close();
            System.out.println("需要去除的包含年份的关键字对象Map写入成功！");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("需要去除的包含年份的关键字对象Map写入失败！");
        }
    }


    /**
     * 写下社区二级标题
     * @param list
     */
    public static void WriteQueryEntity(List<String> list){
//        String filePath = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/queryEntity.dat";
        String filePath = basePath+File.separator+"py"+File.separator+"model"+File.separator+"queryEntity.dat";
        try {
            FileOutputStream outStream = new FileOutputStream(filePath);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    outStream);
            objectOutputStream.writeObject(list);
            objectOutputStream.close();
            System.out.println("queryEntity对象写入成功！");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("queryEntity对象写入成功！");
        }

    }


    /**
     * 写下专家合作关系expertCooperate对象
     * @param expertCooperate
     */
    public static void WriteExpertCooperateObject(HashMap<String,LinkedList<String>> expertCooperate) {
//        String filePath = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/authorCooperateObject.dat";
//        String filePath = "E:\\tech_analysis_my\\tech_analysis\\py\\model\\expertCooperateObject.dat";
        String filePath = basePath+File.separator+"py"+File.separator+"model"+File.separator+"expertCooperateObject.dat";
        try {
            FileOutputStream outStream = new FileOutputStream(filePath);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    outStream);
            objectOutputStream.writeObject(expertCooperate);
            objectOutputStream.close();
            System.out.println("专家合作关系expertData对象写入成功！");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("专家合作关系expertData对象写入成功！");
        }
    }

    /**
     * 写下专家所属机构和专家同事enterpriseAndExpert对象
     * @param enterpriseAndExpert
     */
    public static void WriteEnterpriseAndExpertObject(HashMap<String,LinkedList<String>> enterpriseAndExpert) {
//        String filePath = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/enterpriseAndExpertObject.dat";
//        String filePath = "E:\\tech_analysis_my\\tech_analysis\\py\\model\\enterpriseAndExpertObject.dat";
        String filePath = basePath+File.separator+"py"+File.separator+"model"+File.separator+"enterpriseAndExpertObject.dat";
        try {
            FileOutputStream outStream = new FileOutputStream(filePath);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    outStream);
            objectOutputStream.writeObject(enterpriseAndExpert);
            objectOutputStream.close();
            System.out.println("专家所属机构和专家同事对象写入成功！");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("专家所属机构和专家同事对象写入失败！");
        }
    }

    /**
     * 论文共同发表的机构的对象
     * HashMap<String,LinkedList<String>>   uid   机构列表
     * @param paperInstitution
     */
    public static void WritePaperInstitutionObject(HashMap<String,LinkedList<String>> paperInstitution) {
//        String filePath = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/paperInstitutionObject.dat";
//        String filePath = "E:\\tech_analysis_my\\tech_analysis\\py\\model\\paperInstitutionObject.dat";
        String filePath = basePath+File.separator+"py"+File.separator+"model"+File.separator+"paperInstitutionObject.dat";
        try {
            FileOutputStream outStream = new FileOutputStream(filePath);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    outStream);
            objectOutputStream.writeObject(paperInstitution);
            objectOutputStream.close();
            System.out.println("论文共同发表的机构的对象写入成功！");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("论文共同发表的机构的对象写入失败！");
        }
    }

    /**
     * 写下作者到机构的map对象
     * @param name2Enterprise
     */
    public static void WriteName2EnterpriseObject(HashMap<String,String> name2Enterprise) {
//        String filePath = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/name2EnterpriseObject.dat";
//        String filePath = "E:\\tech_analysis_my\\tech_analysis\\py\\model\\name2EnterpriseObject.dat";
        String filePath = basePath+File.separator+"py"+File.separator+"model"+File.separator+"name2EnterpriseObject.dat";
        try {
            FileOutputStream outStream = new FileOutputStream(filePath);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    outStream);
            objectOutputStream.writeObject(name2Enterprise);
            objectOutputStream.close();
            System.out.println("作者到机构的map对象写入成功！");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("作者到机构的map对象写入失败！");
        }
    }

    /**
     * 构建Neo4j数据库的author对象
     * @param authorsNeo4j
     */
    public static void WriteAuthorsNeo4jObject(HashMap<String, AuthorEntity> authorsNeo4j) {
//        String filePath = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/authorsNeo4jObject.dat";
//        String filePath = "E:\\tech_analysis_my\\tech_analysis\\py\\model\\authorsNeo4jObject.dat";
        String filePath = basePath+File.separator+"py"+File.separator+"model"+File.separator+"authorsNeo4jObject.dat";
        try {
            FileOutputStream outStream = new FileOutputStream(filePath);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    outStream);
            objectOutputStream.writeObject(authorsNeo4j);
            objectOutputStream.close();
            System.out.println("Neo4j作者对象写入成功！");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Neo4j作者对象写入失败！");
        }
    }

    /**
     * 构建Neo4j数据库的institution对象
     * @param institutionsNeo4j
     */
    public static void WriteInstitutionsNeo4jObject(HashMap<String, InstitutionEntity> institutionsNeo4j) {
//        String filePath = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/institutionsNeo4jObject.dat";
//        String filePath = "E:\\tech_analysis_my\\tech_analysis\\py\\model\\institutionsNeo4jObject.dat";
        String filePath = basePath+File.separator+"py"+File.separator+"model"+File.separator+"institutionsNeo4jObject.dat";
        try {
            FileOutputStream outStream = new FileOutputStream(filePath);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    outStream);
            objectOutputStream.writeObject(institutionsNeo4j);
            objectOutputStream.close();
            System.out.println("Neo4j机构对象写入成功！");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Neo4j机构对象写入成功！");
        }
    }

    /**
     * 构建Neo4j数据库的relationship对象
     * @param relationshipsNeo4j
     */
    public static void WriteRelationshipsNeo4jObject(HashMap<String, RelationshipEntity> relationshipsNeo4j) {
//        String filePath = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/relationshipsNeo4jObject.dat";
//        String filePath = "E:\\tech_analysis_my\\tech_analysis\\py\\model\\relationshipsNeo4jObject.dat";
        String filePath = basePath+File.separator+"py"+File.separator+"model"+File.separator+"relationshipsNeo4jObject.dat";
        try {
            FileOutputStream outStream = new FileOutputStream(filePath);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    outStream);
            objectOutputStream.writeObject(relationshipsNeo4j);
            objectOutputStream.close();
            System.out.println("Neo4j关系对象写入成功！");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Neo4j关系对象写入失败！");
        }
    }


    public static void WriteKeywordFile(HashMap<String, KeywordEntity> hashmap) {
//        String filePath = "D:/Entity/keywordEntity.dat";
//        String filePath = "/home/zhzy/Documents/data/keywordEntity.dat";
//        String filePath = "F:/keywordEntity.dat";
//        String filePath = "E:\\tech_analysis_my\\tech_analysis\\py\\model\\keywordEntity.dat";
        String filePath = basePath+File.separator+"py"+File.separator+"model"+File.separator+"keywordEntity.dat";
        try {
//            FileOutputStream out = new FileOutputStream(filePath);
//            BufferedWriter bufw = new BufferedWriter(new OutputStreamWriter(out));
//            bufw.write(hashmap.toString());
//            bufw.close();
            FileOutputStream outStream = new FileOutputStream(filePath);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    outStream);
            objectOutputStream.writeObject(hashmap);
            objectOutputStream.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void WriteRelationFile(HashMap<String, RelationshipEntity> hashmap) {
//        String filePath = "D:/Entity/relationshipEntity.dat";
//        String filePath = "/home/zhzy/Documents/data/relationshipEntity.dat";
//        String filePath = "/home/zhzy/Documents/data/relationshipEntity.dat";
//        String filePath = "F:/relationshipEntity.dat";
//        String filePath = "E:\\tech_analysis_my\\tech_analysis\\py\\model\\relationshipEntity.dat";
        String filePath = basePath+File.separator+"py"+File.separator+"model"+File.separator+"relationshipEntity.dat";
        try {
            FileOutputStream outStream = new FileOutputStream(filePath);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    outStream);
            objectOutputStream.writeObject(hashmap);
            objectOutputStream.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
