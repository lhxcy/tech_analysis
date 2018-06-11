//package com.tech.analysis.Dao;
//
//import com.tech.analysis.entity.KeywordEntity;
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
////@Repository
//public class BuildInitNeo4j {
//    private String basePath = System.getProperty("user.dir");
////    String test = basePath+File.separator+"py"+File.separator+"model"+File.separator;
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
//        try {
//            //得到所需数据
////            getEnterpriseAndExpertData.getAllData();
////            String filePath = "E:\\tech_analysis_my\\tech_analysis\\py\\model\\Neo4j\\testpaperData";
//            String filePath = basePath+File.separator+"py"+File.separator+"model"+File.separator+"testpaperData";
//                File dbPath = new File(filePath);
//            EdgeAndNodeImport edgeAndNodeImport = new EdgeAndNodeImport(dbPath);
////            edgeAndNodeImport.ReadHash();
//            edgeAndNodeImport.importNode();
//            edgeAndNodeImport.importRelationship();
//            edgeAndNodeImport.flushIndex();
//            edgeAndNodeImport.shutDownIndex();
//            edgeAndNodeImport.shutDownNeo4j();
//        }catch (Exception e){
//            e.printStackTrace();
//            System.out.println("构建初始数据库错误");
//        }
//    }
//}
