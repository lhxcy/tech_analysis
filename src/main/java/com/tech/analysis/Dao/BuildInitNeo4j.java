//package com.tech.analysis.Dao;
//
//import com.tech.analysis.entity.KeywordEntity;
//import com.tech.analysis.util.Runner;
//import org.springframework.stereotype.Repository;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.stereotype.Repository;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.ObjectInputStream;
//import java.util.HashMap;
//
///**
// * Created by XCY on 2018/5/28.
// */
//@Repository
//public class BuildInitNeo4j {
//    private String basePath = System.getProperty("user.dir");
//    String test = basePath+File.separator+"py"+File.separator+"model"+File.separator;
////    @Autowired
////    private GetEnterpriseAndExpertData getEnterpriseAndExpertData;
////    public static void main(String[] args) throws Exception{
////        GenerateHashTable gen = new GenerateHashTable();
//////        String file = "E:\\PycharmCode\\Test\\paper.dat";
////////        String file = "/home/zhzy/Downloads/paper_clean .dat";
////        String file = basePath+File.separator+"py"+File.separator+"model"+File.separator+"paper_clean .dat";
////        gen.generate(file);
//////        HashMap<String, KeywordEntity> keywords;
//////        FileInputStream fKeyword1;
//////        keywords = new HashMap<String, KeywordEntity>();
//////        fKeyword1 = new FileInputStream("E:\\tech_analysis_my\\tech_analysis\\py\\model\\keywordEntity.dat");
//////        ObjectInputStream keywordInputStream = new ObjectInputStream(fKeyword1);
//////        keywords = (HashMap<String, KeywordEntity>) keywordInputStream.readObject();
//////        System.out.println(keywords.size());
////        new BuildInitNeo4j().buildInitNeo4j();
////    }
//    public void buildInitNeo4j(){
////        GetEnterpriseAndExpertData getEnterpriseAndExpertData = new GetEnterpriseAndExpertData();
//        GenerateHashTable generateHashTable = new GenerateHashTable();
//        try {
//            //得到所需数据
////            getEnterpriseAndExpertData.getAllData();
////            System.out.println("从sql数据库取出数据");
//            //生成hashtable
////            generateHashTable.general_Author_institutionObject();
////            System.out.println("生成hashtable，开始构建基础数据库");
//
//            //构建基础库
//            String filePath = basePath+File.separator+"py"+File.separator+"model"+File.separator+"testpaperData";
//            File dbPath = new File(filePath);
//            EdgeAndNodeImport edgeAndNodeImport = new EdgeAndNodeImport(dbPath);
//            edgeAndNodeImport.ReadHash();
//            edgeAndNodeImport.importNode();
//            edgeAndNodeImport.importRelationship();
//            edgeAndNodeImport.flushIndex();
//            edgeAndNodeImport.shutDownIndex();
//            edgeAndNodeImport.shutDownNeo4j();
//
//            System.out.println("构建基础数据库完毕");
//        }catch (Exception e){
//            e.printStackTrace();
//            System.out.println("构建初始数据库错误");
//        }
//    }
//
//    public static void main(String[] args) throws Exception{
//
//        ImportNeo4j.runConsoleOrder("java -jar import_json_to_neo4j_v2.0-1.0.0.jar");
//    }
//}
