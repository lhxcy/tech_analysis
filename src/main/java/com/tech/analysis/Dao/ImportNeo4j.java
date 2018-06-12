package com.tech.analysis.Dao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.neo4j.driver.v1.Values.parameters;

/**
 * 该类的主要功能是向neo4j中插入数据 importCSV适合1--100000数据量   batch_import适合大数据
 */
@Repository
public class ImportNeo4j {
    @Value("${neo4jdatapath}")
    private String neo4jdatapath;
    @Value("${neo4jpath}")
    private String neo4jpath;
    private String basePath = System.getProperty("user.dir");
    //        String test = basePath+File.separator+"py"+File.separator+"model"+File.separator;
    /**
     * neo4j导入数据
     *
     */
    public void importCSV(){
        String keywoeds ="LOAD CSV WITH HEADERS  FROM \"file:///keywords.csv\" AS line"+
                " MERGE (p:keyNode{id:line.id,name:line.name})";
        String relationship ="LOAD CSV WITH HEADERS FROM \"file:///relationship.csv\" AS line"+
                " match (from:keyNode{id:line.from_id}),(to:keyNode{id:line.to_id})"+
                " merge (from)-[r:wordsSimilar{times:line.times}]->(to)";
        String yearKeywoeds ="LOAD CSV WITH HEADERS  FROM \"file:///yearKeywords.csv\" AS line"+
                " MERGE (p:yearKeyNode{id:line.id,name:line.name,year:line.year})";
        String yearrRelationship ="LOAD CSV WITH HEADERS FROM \"file:///yearRelationship.csv\" AS line"+
                " match (from:yearKeyNode{id:line.from_id}),(to:yearKeyNode{id:line.to_id})"+
                " merge (from)-[r:yearWordsSimilar{times:line.times}]->(to)";
//        System.out.println("执行keywoeds中。。。");
//        boolean flagkeywords = neo4jExcute(keywoeds);
//        System.out.println("keywoeds: "+flagkeywords);
        System.out.println("执行relationship中。。。");
        boolean flagrelationship = neo4jExcute(relationship);
        System.out.println("relationship:  "+flagrelationship);
        System.out.println("执行yearKeywoeds中。。。");
        boolean flagyearKeywords = neo4jExcute(yearKeywoeds);
        System.out.println("yearKeywoeds:  "+flagyearKeywords);
        System.out.println("执行yearrRelationship中。。。");
        boolean flagyearRelationship = neo4jExcute(yearrRelationship);
        System.out.println("yearrRelationship:  "+flagyearRelationship);

    }

    /**
     * 连接数据库执行操作
     * @param order 执行命令
     * @return
     */
    public boolean neo4jExcute(String order){
        boolean flag = true;
        ConnectAndOperNeo4j connect = new ConnectAndOperNeo4j();
        try {
            connect.excute(order,parameters( "", "" ));
        }catch (Exception e){
            flag =false;
            System.out.println("import order error: " + order);
            e.printStackTrace();
        }finally {
            connect.closeConnect();
        }
        return flag;
    }


    /**
     * 开启neo4j数据库
     */
    public void startNeo4j(){
//        String[] parm = new String[] { "/usr/neo4j3.1.0/bin/neo4j", "start"};
//        String parm = "/usr/neo4j3.1.0/bin/neo4j start";
        String parm = neo4jpath+" start";
        runConsoleOrder(parm);
    }


    /**
     * 关闭neo4j数据库
     */
    public void stopNeo4j(){
//        String[] parm = new String[] { "/usr/neo4j3.1.0/bin/neo4j", "stop"};
//        String parm = "/usr/neo4j3.1.0/bin/neo4j stop";
        String parm = neo4jpath+" stop";
        runConsoleOrder(parm);
    }


    /**
     * 批量导入neo4j数据库
     */
    public void batch_import(){
        MyUnzip myUnzip = new MyUnzip();
        String import_dir = basePath+File.separator;
        String csv_dir = basePath+File.separator+"file"+File.separator;
        try {
            stopNeo4j();
//            String deleteGraph_db =  "rm -rf /usr/neo4j3.1.0/data/databases/paperData1";
            String deleteGraph_db =  "rm -rf "+ neo4jdatapath+"/paperData1";
//            String[] deleteGraph_db1 = new String[] { "rm", "-rf",
//                    "/usr/neo4j3.1.0/data/databases/paperData1"};

//            String[] keyImport = new String[] { "/bin/bash",
//                    "/home/zhzy/Downloads/xcy/batch-import-tool/import.sh",
//                    "/usr/neo4j3.1.0/data/databases/paperData1",
//                    "/home/zhzy/Downloads/xcy/batch-import-tool/file/keywordsKey.csv",
//                    "/home/zhzy/Downloads/xcy/batch-import-tool/file/relationshipKey.csv"};

            String keyImport_no_plate = "/bin/bash " + import_dir+"import.sh " +
                    neo4jdatapath+"/paperData1 " +
                    csv_dir+"keywordsKey.csv " +
                    csv_dir+"relationshipKey.csv";

//            String keyImport = "/bin/bash /home/zhzy/Downloads/xcy/Main_Tech/tech_analysis/import.sh " +
//                    "/usr/neo4j3.1.0/data/databases/paperData1 " +
//                    "/home/zhzy/Downloads/xcy/Main_Tech/tech_analysis/file/keywordsKey.csv " +
//                    "/home/zhzy/Downloads/xcy/Main_Tech/tech_analysis/file/relationshipKey.csv";

//            String[] yearKeyImport = new String[] { "/bin/bash",
//                    "/home/zhzy/Downloads/xcy/batch-import-tool/import.sh",
//                    "/usr/neo4j3.1.0/data/databases/paperData1",
//                    "/home/zhzy/Downloads/xcy/batch-import-tool/file/yearKeywordsKey.csv",
//                    "/home/zhzy/Downloads/xcy/batch-import-tool/file/yearRelationshipKey.csv"};

            String yearKeyImport_no_plate = "/bin/bash " + import_dir+"import.sh " +
                    neo4jdatapath+"/paperData1 "  +
                    csv_dir+"yearKeywordsKey.csv " +
                    csv_dir+"yearRelationshipKey.csv";

//            String yearKeyImport = "/bin/bash /home/zhzy/Downloads/xcy/Main_Tech/tech_analysis/import.sh " +
//                        "/usr/neo4j3.1.0/data/databases/paperData1 " +
//                    "/home/zhzy/Downloads/xcy/Main_Tech/tech_analysis/file/yearKeywordsKey.csv " +
//                    "/home/zhzy/Downloads/xcy/Main_Tech/tech_analysis/file/yearRelationshipKey.csv";

            runConsoleOrder(deleteGraph_db);
            System.out.println("删除成功");
            myUnzip.unZipFiles(new File(neo4jdatapath+"/paperData1.zip"), neo4jdatapath+"/");

            System.out.println("解压成功");
            runConsoleOrder(keyImport_no_plate);
            runConsoleOrder(yearKeyImport_no_plate);
//            runConsoleOrder(keyImport);
//            runConsoleOrder(yearKeyImport);
            startNeo4j();
        }catch (Exception e){
            System.out.println("批量导入csv文件出错");
            e.printStackTrace();
        }

    }

    /**
     * 执行命令行参数
     * @param parm 参数列表
     */
    public void runConsoleOrder(String parm){
        try {
            System.out.println("Starting .....");
            Process pr = Runtime.getRuntime().exec(parm);
//            System.out.println("test  ssssss");
            pr.waitFor();
//            System.out.println("test  eeeee");
            BufferedReader in = new BufferedReader(new
                    InputStreamReader(pr.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
//            pr.waitFor();
            System.out.println("End .....");
        }catch (Exception e){
            System.out.println("执行命令失败！");
            e.printStackTrace();
        }
    }
}
