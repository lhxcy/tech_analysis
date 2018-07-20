package com.tech.analysis.Dao;

import com.tech.analysis.entity.*;
import org.apache.lucene.analysis.Analyzer;
import org.neo4j.graphdb.index.IndexManager;
import org.neo4j.graphdb.schema.IndexCreator;
import org.neo4j.helpers.collection.MapUtil;
import org.neo4j.index.lucene.unsafe.batchinsert.LuceneBatchInserterIndexProvider;
import org.neo4j.storageengine.api.schema.IndexReader;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import org.neo4j.unsafe.batchinsert.BatchInserterIndex;
import org.neo4j.unsafe.batchinsert.BatchInserterIndexProvider;
import org.neo4j.unsafe.batchinsert.BatchInserters;
import org.wltea.analyzer.lucene.IKAnalyzer;
import com.tech.analysis.entity.KeywordEntity;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by XCY on 2018/5/28.
 */
public class EdgeAndNodeImport {
    HashMap<String, AuthorEntity> authors;
    HashMap<String, InstitutionEntity> institutions;
    HashMap<String, RelationshipEntity> relationships;

    BatchInserter inserter;
    BatchInserterIndexProvider indexProvider;
    BatchInserterIndex author_index, institution_index;
//    private String basePath = System.getProperty("user.dir");
    private static String basePath = UtilWrite.class.getClassLoader().getResource("/").getPath();


    private final String AUTHOR_INDEX = "author";
    private final String INSTITUTION_INDEX = "institution";


    //构造函数
    public EdgeAndNodeImport(File filePath) throws Exception{
        InitializeInserter(filePath);
    }

    public void ReadHash() {
        FileInputStream fAuthor;

        FileInputStream fInstitution;

        FileInputStream fRelationship;
        authors = new HashMap<String, AuthorEntity>();
        institutions = new HashMap<String, InstitutionEntity>();

        relationships = new HashMap<String, RelationshipEntity>();
        try {

            fAuthor = new FileInputStream(basePath+File.separator+"py"+File.separator+"model"+File.separator+"HauthorEntity.dat");////改
            ObjectInputStream authorInputStream = new ObjectInputStream(fAuthor);
            authors = (HashMap<String, AuthorEntity>) authorInputStream.readObject();
            System.out.println("authors size: "+authors.size());

//
            fInstitution = new FileInputStream(basePath+File.separator+"py"+File.separator+"model"+File.separator+"institutionEntity.dat");
            ObjectInputStream institutionInputStream = new ObjectInputStream(fInstitution);
            institutions = (HashMap<String, InstitutionEntity>) institutionInputStream.readObject();
            System.out.println("institutions size: "+institutions.size());

            fRelationship = new FileInputStream(basePath+File.separator+"py"+File.separator+"model"+File.separator+"relationshipEntity.dat");
            ObjectInputStream relationshipInputStream = new ObjectInputStream(fRelationship);
            relationships = (HashMap<String, RelationshipEntity>) relationshipInputStream.readObject();

            System.out.println("relationships size: "+relationships.size());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void InitializeInserter(File filePath) throws Exception {
        inserter = BatchInserters.inserter(filePath);//使用BatchInserter分批导入大量数据
        System.out.println("ppppppppp");
        indexProvider = new LuceneBatchInserterIndexProvider(inserter);
        System.out.println("hhhhhhhhhh");
        //批插入过程中的索引使用BatchInserterIndexProvider提供的BatchInserterIndex完成

        Map<String, String> exactConfig = new HashMap<String, String>();
        exactConfig.put("type", "exact");
        Map<String, String> fulltextConfig = new HashMap<String, String>();
        fulltextConfig.put("type", "fulltext");//全文索引
        fulltextConfig.put("analyzer", "org.wltea.analyzer.lucene.IKAnalyzer");//分词器

        author_index = indexProvider.nodeIndex(AUTHOR_INDEX, fulltextConfig);
        author_index.setCacheCapacity("name", 10000);
        institution_index = indexProvider.nodeIndex(INSTITUTION_INDEX, fulltextConfig);
    }

    public void InitializeInserter1(File filePath) throws Exception {
//        FileInputStream fKeyword1;
//        keywords = new HashMap<String, KeywordEntity>();
////        fKeyword1 = new FileInputStream("F:/keywordEntity.dat");
//        fKeyword1 = new FileInputStream("E:/PycharmCode/Test/keywordEntity.dat");
//        ObjectInputStream keywordInputStream = new ObjectInputStream(fKeyword1);
//        keywords = (HashMap<String, KeywordEntity>) keywordInputStream.readObject();
//        System.out.println(keywords.size());

//        ins = BatchInserters.inserter(filePath);
        System.out.println(filePath.getAbsolutePath());
        System.out.println("qqqqqqqqqqqqqqqqq");
        inserter = BatchInserters.inserter(filePath);//使用BatchInserter分批导入大量数据
        System.out.println("hhhhhhhhhhhhhhhhhhh");

//        IndexCreator a = inserter.createDeferredSchemaIndex(Labels.Author);
//        a.on("name").create();
        indexProvider = new LuceneBatchInserterIndexProvider(inserter);
        //批插入过程中的索引使用BatchInserterIndexProvider提供的BatchInserterIndex完成

        Map<String, String> exactConfig = new HashMap<String, String>();
        exactConfig.put("type", "exact");
        Map<String, String> fulltextConfig = new HashMap<String, String>();
        fulltextConfig.put("type", "fulltext");//全文索引
        fulltextConfig.put("analyzer", "org.wltea.analyzer.lucene.IKAnalyzer");//分词器

//        author_index = indexProvider.nodeIndex(AUTHOR_INDEX, fulltextConfig);
//        author_index.setCacheCapacity("name", 10000);
//        institution_index = indexProvider.nodeIndex(INSTITUTION_INDEX, fulltextConfig);
//        institution_index.setCacheCapacity("name", 10000);
//        journal_index = indexProvider.nodeIndex(JOURNAL_INDEX, exactConfig);
//        keyword_index = indexProvider.nodeIndex(KEYWORD_INDEX, fulltextConfig);
//        keyword_index.setCacheCapacity("name", 10000);
        //paper_index = indexProvider.nodeIndex(PAPER_INDEX, fulltextConfig);
//        paper_index.setCacheCapacity("title", 10000);
    }
    public static Map<String, String> config() throws IOException{//配置
        String filePath = "./src/main/resources/batchinserter.properties";
        FileReader file = new FileReader(new File(filePath).getAbsoluteFile());
        Map<String, String> config = MapUtil.load(file);//从输入流中读取属性列表（键和元素对）并返回
//        for(Map.Entry<String, String> map:config.entrySet()){
//            System.out.println(map.getKey() + " " + map.getValue());
//        }
        return config;
    }

    public void importNode() {

        for (Map.Entry<String, AuthorEntity> map : authors.entrySet()) {//改动///////////////
            AuthorEntity authorEntity = map.getValue();
            Map<String, Object> author = new HashMap<String, Object>();
            author.put("name", authorEntity.getName());
            author.put("institution", authorEntity.getInstitution());
            inserter.createNode(authorEntity.getId(), author, Labels.Author);
            author_index.add(authorEntity.getId(), MapUtil.map("name", authorEntity.getName(), "institution", authorEntity.getInstitution()));
//            author_index.add(authorEntity.getId(), MapUtil.map());
        }

        for(Map.Entry<String, InstitutionEntity> map: institutions.entrySet()){//////////////
            InstitutionEntity institutionEntity = map.getValue();
            Map<String, Object> institution = new HashMap<String, Object>();
            institution.put("name", institutionEntity.getName());
            institution.put("location", institutionEntity.getLocation());
            inserter.createNode(institutionEntity.getId(), institution, Labels.Institution);
            institution_index.add(institutionEntity.getId(), MapUtil.map("name", institutionEntity.getName()));
        }

    }

    public void importRelationship(){
        for(Map.Entry<String, RelationshipEntity> map: relationships.entrySet()){
            RelationshipEntity relationshipEntity = map.getValue();
            Map<String, Object> relationship = new HashMap<String, Object>();
//            relationship.put("weight", relationshipEntity.getTimes());
            relationship.put("cotimes", relationshipEntity.getTimes());
            inserter.createRelationship(relationshipEntity.getSource(), relationshipEntity.getTarget(),
                    relationshipEntity.getType(), relationship);
        }
    }

    public void flushIndex(){//改动
        author_index.flush();
        institution_index.flush();
    }

    public void shutDownIndex(){
        indexProvider.shutdown();
    }
    public void shutDownNeo4j(){
        inserter.shutdown();
    }
}
