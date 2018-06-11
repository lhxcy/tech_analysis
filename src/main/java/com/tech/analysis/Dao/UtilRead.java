package com.tech.analysis.Dao;

import com.tech.analysis.entity.AuthorEntity;
import com.tech.analysis.entity.InstitutionEntity;
import com.tech.analysis.entity.KeywordEntity;
import com.tech.analysis.entity.RelationshipEntity;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by XCY on 2018/4/18.
 */
public class UtilRead {
    private static String basePath = System.getProperty("user.dir");
    /**
     * 读出模型
     * @return
     */
    public static HashMap<String, double[]> readModel(){
        FileInputStream freader;
        HashMap<String, double[]> wordMap  = new HashMap<String, double[]>();
        try {
//            String filePath = "E:\\tech_analysis_my\\tech_analysis\\py\\model\\model.dat";
//            String filePath = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/model.dat";
//            String filePath = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/model520286.dat";
//            String filePath = basePath+File.separator+"py"+File.separator+"model"+File.separator+"model.dat";
            String filePath = basePath+File.separator+"py"+File.separator+"model"+File.separator+"model_more.dat";
            freader = new FileInputStream(filePath);
            ObjectInputStream objectInputStream = new ObjectInputStream(freader);
//            HashMap<String, double[]> wordMap = new HashMap<String, double[]>();
            wordMap = (HashMap<String, double[]>)objectInputStream.readObject();
            System.out.println("载入模型成功！");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("载入模型失败！");
        }
        return wordMap;
    }

    /**
     * 读出存储的keywords对象，用于加载新的数据时更新使用
     * @return
     */
    public static HashMap<String, KeywordEntity> readKeywords(){
        FileInputStream freader;
        HashMap<String, KeywordEntity> keywords = new HashMap<String, KeywordEntity>();
        try {
//            String filePath = "E:\\tech_analysis\\py\\model\\KeywordsObject.dat";
//            String filePath = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/KeywordsObject.dat";
//            String filePath = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/KeywordsObjectKey.dat";
            String filePath = basePath+File.separator+"py"+File.separator+"model"+File.separator+"KeywordsObjectKey.dat";
            freader = new FileInputStream(filePath);
            ObjectInputStream objectInputStream = new ObjectInputStream(freader);
//            HashMap<String, double[]> wordMap = new HashMap<String, double[]>();
            keywords = (HashMap<String, KeywordEntity>)objectInputStream.readObject();
            System.out.println("载入keywords成功！");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("载入keywords失败！");
        }
        return keywords;
    }

    /**
     * 加载yearKeywords
     * @return
     */
    public static HashMap<String, KeywordEntity> readYearKeywords(){
        FileInputStream freader;
        HashMap<String, KeywordEntity> yearKeywords = new HashMap<String, KeywordEntity>();
        try {
//            String filePath = "E:\\tech_analysis\\py\\model\\YearKeywordsObject.dat";
//            String filePath = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/YearKeywordsObject.dat";
//            String filePath = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/YearKeywordsObjectKey.dat";
            String filePath = basePath+File.separator+"py"+File.separator+"model"+File.separator+"YearKeywordsObjectKey.dat";
            freader = new FileInputStream(filePath);
            ObjectInputStream objectInputStream = new ObjectInputStream(freader);
//            HashMap<String, double[]> wordMap = new HashMap<String, double[]>();
            yearKeywords = (HashMap<String, KeywordEntity>)objectInputStream.readObject();
            System.out.println("载入yearKeywords成功！");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("载入yearKeywords失败！");
        }
        return yearKeywords;
    }

    /**
     * 加载关系relationships
     * @return
     */
    public static HashMap<String, RelationshipEntity> readRelationships(){
        FileInputStream freader;
        HashMap<String, RelationshipEntity> relationships = new HashMap<String, RelationshipEntity>();
        try {
//            String filePath = "E:\\tech_analysis\\py\\model\\RelationshipsObject.dat";
//            String filePath = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/RelationshipsObject.dat";
//            String filePath = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/RelationshipsObjectKey.dat";
            String filePath = basePath+File.separator+"py"+File.separator+"model"+File.separator+"RelationshipsObjectKey.dat";
            freader = new FileInputStream(filePath);
            ObjectInputStream objectInputStream = new ObjectInputStream(freader);
//            HashMap<String, double[]> wordMap = new HashMap<String, double[]>();
            relationships = (HashMap<String, RelationshipEntity>)objectInputStream.readObject();
            System.out.println("载入relationships成功！");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("载入relationships失败！");
        }
        return relationships;
    }

    /**
     * 加载yearRelationships
     * @return
     */
    public static HashMap<String, RelationshipEntity> readYearRelationships(){
        FileInputStream freader;
        HashMap<String, RelationshipEntity> yearRelationships = new HashMap<String, RelationshipEntity>();
        try {
//            String filePath = "E:\\tech_analysis\\py\\model\\YearRelationshipsObject.dat";
//            String filePath = "E/home/zhzy/Downloads/xcy/tech_analysis/py/model/YearRelationshipsObject.dat";
//            String filePath = "E/home/zhzy/Downloads/xcy/tech_analysis/py/model/YearRelationshipsObjectKey.dat";
            String filePath = basePath+File.separator+"py"+File.separator+"model"+File.separator+"YearRelationshipsObjectKey.dat";
            freader = new FileInputStream(filePath);
            ObjectInputStream objectInputStream = new ObjectInputStream(freader);
//            HashMap<String, double[]> wordMap = new HashMap<String, double[]>();
            yearRelationships = (HashMap<String, RelationshipEntity>)objectInputStream.readObject();
            System.out.println("载入yearRelationships成功！");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("载入yearRelationships失败！");
        }
        return yearRelationships;
    }

    /**
     * 加载keywordTimes
     * @return
     */
    public static HashMap<String, Long> readKeywordTimes(){
        FileInputStream freader;
        HashMap<String, Long> keywordTimes = new HashMap<String, Long>();
        try {
//            String filePath = "E:\\tech_analysis\\py\\model\\KeywordsTimesObject.dat";
//            String filePath = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/KeywordsTimesObject.dat";
//            String filePath = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/KeywordsTimesObjectKey.dat";
            String filePath = basePath+File.separator+"py"+File.separator+"model"+File.separator+"KeywordsTimesObjectKey.dat";
            freader = new FileInputStream(filePath);
            ObjectInputStream objectInputStream = new ObjectInputStream(freader);
//            HashMap<String, double[]> wordMap = new HashMap<String, double[]>();
            keywordTimes = (HashMap<String, Long>)objectInputStream.readObject();
            System.out.println("载入keywordTimes成功！");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("载入keywordTimes失败！");
        }
        return keywordTimes;
    }

    /**
     * 加载yearKeywordTimes
     * @return
     */
    public static HashMap<String, Long> readYearKeywordTimes(){
        FileInputStream freader;
        HashMap<String, Long> yearKeywordTimes = new HashMap<String, Long>();
        try {
//            String filePath = "E:\\tech_analysis\\py\\model\\YearKeywordsTimesObject.dat";
//            String filePath = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/YearKeywordsTimesObject.dat";
//            String filePath = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/YearKeywordsTimesObjectKey.dat";
            String filePath = basePath+File.separator+"py"+File.separator+"model"+File.separator+"YearKeywordsTimesObjectKey.dat";
            freader = new FileInputStream(filePath);
            ObjectInputStream objectInputStream = new ObjectInputStream(freader);
//            HashMap<String, double[]> wordMap = new HashMap<String, double[]>();
            yearKeywordTimes = (HashMap<String, Long>)objectInputStream.readObject();
            System.out.println("载入yearKeywordTimes成功！");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("载入yearKeywordTimes失败！");
        }
        return yearKeywordTimes;
    }

    /**
     * 读取预测数据构建对象
     * @return
     */
    public static HashMap<String, Long> readPredictionKeyword() {
//        String prefilePath = "E:\\tech_analysis_my\\tech_analysis\\py\\model\\linearModelPrediction.dat";
//        String prefilePath = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/linearModelPrediction.dat";
//        String prefilePath = "/home/zhzy/Downloads/xcy/Main_tech/tech_analysis/py/model/linearModelPrediction.dat";
        String prefilePath = basePath+File.separator+"py"+File.separator+"model"+File.separator+"linearModelPrediction.dat";
        HashMap<String, Long> prediction = new HashMap<String, Long>();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(prefilePath));
            String line = "";
            while (true) {
                line = bufferedReader.readLine();
                if (line == null || line.trim().equals("")) break;
                String[] temps = line.split(",");
                if (Long.parseLong(temps[1].trim()) != 0)
                    prediction.put(temps[0].trim(),Long.parseLong(temps[1].trim()));
            }
            System.out.println("读取预测数据成功");
        }catch (Exception e){
            System.out.println("读取预测数据失败");
        }
        return prediction;
    }

    /**
     * 读出需要去除的Keywords Map
     * @return
     */
    public static HashMap<String, Long> readRmKeyMap(){
        FileInputStream freader;
        HashMap<String, Long> rmKeyMap = new HashMap<String, Long>();
        try {
//            String filePath = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/rmKeyMapObjectKey.dat";
            String filePath = basePath+File.separator+"py"+File.separator+"model"+File.separator+"rmKeyMapObjectKey.dat";
            freader = new FileInputStream(filePath);
            ObjectInputStream objectInputStream = new ObjectInputStream(freader);
            rmKeyMap = (HashMap<String, Long>)objectInputStream.readObject();
            System.out.println("载入rmKeyMap成功！");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("载入rmKeyMap失败！");
        }
        return rmKeyMap;
    }

    /**
     * 读出需要去除的yearKeywords Map
     * @return
     */
    public static HashMap<String, Long> readRmYearKeyMap(){
        FileInputStream freader;
        HashMap<String, Long> rmKeyMap = new HashMap<String, Long>();
        try {
//            String filePath = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/rmYearKeyMapObjectKey.dat";
            String filePath = basePath+File.separator+"py"+File.separator+"model"+File.separator+"rmYearKeyMapObjectKey.dat";
            freader = new FileInputStream(filePath);
            ObjectInputStream objectInputStream = new ObjectInputStream(freader);
            rmKeyMap = (HashMap<String, Long>)objectInputStream.readObject();
            System.out.println("载入rmYearKeyMap成功！");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("载入rmYearKeyMap失败！");
        }
        return rmKeyMap;
    }


    /**
     * 得到社区二级标题
     * @return
     */
    public static List<String> readQuery(){
        FileInputStream freader;
        List<String> query = new ArrayList<>();
        try {
//            String filePath = "E:\\tech_analysis_my\\tech_analysis\\py\\model\\queryEntity.dat";
//            String filePath = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/queryEntity.dat";
            String filePath = basePath+File.separator+"py"+File.separator+"model"+File.separator+"queryEntity.dat";
            freader = new FileInputStream(filePath);
            ObjectInputStream objectInputStream = new ObjectInputStream(freader);
            query = (List<String>)objectInputStream.readObject();
            System.out.println("载入queryEntity对象成功！");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("载入queryEntity对象失败！");
        }
        return query;
    }

    /**
     *载入专家合作关系expertCooperate对象
     * @return
     */
    public static HashMap<String,LinkedList<String>> readExpertCooperateObject(){
        FileInputStream freader;
        HashMap<String,LinkedList<String>> expertCooperate = new HashMap<>();
        try {
//            String filePath = "E:\\tech_analysis_my\\tech_analysis\\py\\model\\expertCooperateObject.dat";
//        String filePath = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/expertCooperateObject.dat";
        String filePath = basePath+File.separator+"py"+File.separator+"model"+File.separator+"expertCooperateObject.dat";
            freader = new FileInputStream(filePath);
            ObjectInputStream objectInputStream = new ObjectInputStream(freader);
            expertCooperate = (HashMap<String,LinkedList<String>>)objectInputStream.readObject();
            System.out.println("载入expertCooperate对象成功！");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("载入expertCooperate对象失败！");
        }
        return expertCooperate;
    }


    /**
     * 载入专家所属机构和专家同事enterpriseAndExpert对象
     * @return
     */
    public static HashMap<String,LinkedList<String>> readEnterpriseAndExpertObject(){
        FileInputStream freader;
        HashMap<String,LinkedList<String>> enterpriseAndExpert = new HashMap<>();
        try {
//            String filePath = "E:\\tech_analysis_my\\tech_analysis\\py\\model\\enterpriseAndExpertObject.dat";
//        String filePath = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/enterpriseAndExpertObject.dat";
        String filePath = basePath+File.separator+"py"+File.separator+"model"+File.separator+"enterpriseAndExpertObject.dat";
            freader = new FileInputStream(filePath);
            ObjectInputStream objectInputStream = new ObjectInputStream(freader);
            enterpriseAndExpert = (HashMap<String,LinkedList<String>>)objectInputStream.readObject();
            System.out.println("载入enterpriseAndExpert对象成功！");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("载入enterpriseAndExpert对象失败！");
        }
        return enterpriseAndExpert;
    }


    /**
     * 论文共同发表的机构的paperInstitution对象
     * @return
     */
    public static HashMap<String,LinkedList<String>> readPaperInstitutionObject(){
        FileInputStream freader;
        HashMap<String,LinkedList<String>> paperInstitution = new HashMap<>();
        try {
//            String filePath = "E:\\tech_analysis_my\\tech_analysis\\py\\model\\paperInstitutionObject.dat";
//        String filePath = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/paperInstitutionObject.dat";
        String filePath = basePath+File.separator+"py"+File.separator+"model"+File.separator+"paperInstitutionObject.dat";
            freader = new FileInputStream(filePath);
            ObjectInputStream objectInputStream = new ObjectInputStream(freader);
            paperInstitution = (HashMap<String,LinkedList<String>>)objectInputStream.readObject();
            System.out.println("载入paperInstitution对象成功！");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("载入paperInstitution对象失败！");
        }
        return paperInstitution;
    }

    /**
     * 作者所属机构的map对象
     * @return
     */
    public static HashMap<String,String> readName2EnterpriseObject(){
        FileInputStream freader;
        HashMap<String,String> name2Enterprise = new HashMap<>();
        try {
//            String filePath = "E:\\tech_analysis_my\\tech_analysis\\py\\model\\name2EnterpriseObject.dat";
//        String filePath = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/name2EnterpriseObject.dat";
        String filePath = basePath+File.separator+"py"+File.separator+"model"+File.separator+"name2EnterpriseObject.dat";
            freader = new FileInputStream(filePath);
            ObjectInputStream objectInputStream = new ObjectInputStream(freader);
            name2Enterprise = ( HashMap<String,String>)objectInputStream.readObject();
            System.out.println("载入name2Enterprise对象成功！");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("载入name2Enterprise对象失败！");
        }
        return name2Enterprise;
    }

    /**
     * 读出构建neo4j数据库的authorsNeo4j对象
     * @return
     */
    public static HashMap<String, AuthorEntity> readAuthorsNeo4jObject(){
        FileInputStream freader;
        HashMap<String, AuthorEntity> authorsNeo4j = new HashMap<>();
        try {
//            String filePath = "E:\\tech_analysis_my\\tech_analysis\\py\\model\\authorsNeo4jObject.dat";
//        String filePath = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/authorsNeo4jObject.dat";
        String filePath = basePath+File.separator+"py"+File.separator+"model"+File.separator+"authorsNeo4jObject.dat";
            freader = new FileInputStream(filePath);
            ObjectInputStream objectInputStream = new ObjectInputStream(freader);
            authorsNeo4j = (HashMap<String, AuthorEntity>)objectInputStream.readObject();
            System.out.println("载入authorsNeo4j对象成功！");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("载入authorsNeo4j对象失败！");
        }
        return authorsNeo4j;
    }

    /**
     * 读出构建neo4j数据库的institutionsNeo4j对象
     * @return
     */
    public static HashMap<String, InstitutionEntity> readInstitutionsNeo4jObject(){
        FileInputStream freader;
        HashMap<String, InstitutionEntity> institutionsNeo4j = new HashMap<>();
        try {
//            String filePath = "E:\\tech_analysis_my\\tech_analysis\\py\\model\\institutionsNeo4jObject.dat";
//        String filePath = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/institutionsNeo4jObject.dat";
        String filePath = basePath+File.separator+"py"+File.separator+"model"+File.separator+"institutionsNeo4jObject.dat";
            freader = new FileInputStream(filePath);
            ObjectInputStream objectInputStream = new ObjectInputStream(freader);
            institutionsNeo4j = (HashMap<String, InstitutionEntity>)objectInputStream.readObject();
            System.out.println("载入institutionsNeo4j对象成功！");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("载入institutionsNeo4j对象失败！");
        }
        return institutionsNeo4j;
    }

    /**
     * 读出构建neo4j数据库的relationshipsNeo4j对象
     * @return
     */
    public static HashMap<String, RelationshipEntity> readRelationshipsNeo4jObject(){
        FileInputStream freader;
        HashMap<String, RelationshipEntity> relationshipsNeo4j = new HashMap<>();
        try {
//            String filePath = "E:\\tech_analysis_my\\tech_analysis\\py\\model\\relationshipsNeo4jObject.dat";
//        String filePath = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/relationshipsNeo4jObject.dat";
        String filePath = basePath+File.separator+"py"+File.separator+"model"+File.separator+"relationshipsNeo4jObject.dat";
            freader = new FileInputStream(filePath);
            ObjectInputStream objectInputStream = new ObjectInputStream(freader);
            relationshipsNeo4j = (HashMap<String, RelationshipEntity>)objectInputStream.readObject();
            System.out.println("载入relationshipsNeo4j对象成功！");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("载入relationshipsNeo4j对象失败！");
        }
        return relationshipsNeo4j;
    }
}
