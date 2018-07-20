package com.tech.analysis.Dao;

import com.tech.analysis.util.PropertyUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import com.tech.analysis.Dao.UtilRead;
import static org.neo4j.driver.v1.Values.parameters;

/**
 * 该类的主要功能是向neo4j中插入数据 importCSV适合1--100000数据量   batch_import适合大数据
 */
@Repository
public class ImportNeo4j {
    private static String neo4jdatabasepath = PropertyUtil.getProperty("neo4jdatabasepath");
    private static String neo4jpath = PropertyUtil.getProperty("neo4jpath");
//    private static String basePath = System.getProperty("user.dir");
    private static String basePath = ImportNeo4j.class.getClassLoader().getResource("/").getPath()+File.separator+"import";
    /**
     * neo4j导入数据
     *
     */
//    public void importCSV(){
//        String keywoeds ="LOAD CSV WITH HEADERS  FROM \"file:///keywords.csv\" AS line"+
//                " MERGE (p:keyNode{id:line.id,name:line.name})";
//        String relationship ="LOAD CSV WITH HEADERS FROM \"file:///relationship.csv\" AS line"+
//                " match (from:keyNode{id:line.from_id}),(to:keyNode{id:line.to_id})"+
//                " merge (from)-[r:wordsSimilar{times:line.times}]->(to)";
//        String yearKeywoeds ="LOAD CSV WITH HEADERS  FROM \"file:///yearKeywords.csv\" AS line"+
//                " MERGE (p:yearKeyNode{id:line.id,name:line.name,year:line.year})";
//        String yearrRelationship ="LOAD CSV WITH HEADERS FROM \"file:///yearRelationship.csv\" AS line"+
//                " match (from:yearKeyNode{id:line.from_id}),(to:yearKeyNode{id:line.to_id})"+
//                " merge (from)-[r:yearWordsSimilar{times:line.times}]->(to)";
////        System.out.println("执行keywoeds中。。。");
////        boolean flagkeywords = neo4jExcute(keywoeds);
////        System.out.println("keywoeds: "+flagkeywords);
//        System.out.println("执行relationship中。。。");
//        boolean flagrelationship = neo4jExcute(relationship);
//        System.out.println("relationship:  "+flagrelationship);
//        System.out.println("执行yearKeywoeds中。。。");
//        boolean flagyearKeywords = neo4jExcute(yearKeywoeds);
//        System.out.println("yearKeywoeds:  "+flagyearKeywords);
//        System.out.println("执行yearrRelationship中。。。");
//        boolean flagyearRelationship = neo4jExcute(yearrRelationship);
//        System.out.println("yearrRelationship:  "+flagyearRelationship);
//
//    }

    /**
     * 连接数据库执行操作
     * @param order 执行命令
     * @return
     */
//    public boolean neo4jExcute(String order){
//        boolean flag = true;
//        ConnectAndOperNeo4j connect = new ConnectAndOperNeo4j();
//        try {
//            connect.excute(order,parameters( "", "" ));
//        }catch (Exception e){
//            flag =false;
//            System.out.println("import order error: " + order);
//            e.printStackTrace();
//        }finally {
//            connect.closeConnect();
//        }
//        return flag;
//    }


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
//        MyUnzip myUnzip = new MyUnzip();
        String import_dir = basePath+File.separator;
        String csv_dir = basePath+File.separator+"file"+File.separator+"test"+File.separator;
        try {
            stopNeo4j();
//            String deleteGraph_db =  "rm -rf /usr/neo4j3.1.0/data/databases/paperData1";
            String deleteGraph_db =  "rm -rf "+ neo4jdatabasepath;
            System.err.println(deleteGraph_db);
//            java -jar buildNeo4jDatabase.jar /home/zhzy/Downloads/xcy/Main_Tech/tech_analysis /home/zhzy/Downloads/xcy/Main_Tech/paperData4

            String buildBaseNeo4j = "java -jar "+basePath+File.separator+"buildNeo4jDatabase.jar "+
                    basePath+" "+neo4jdatabasepath;
            System.err.println(buildBaseNeo4j);

//            String buildBaseNeo4j= "java -jar buildNeo4jDatabase.jar /home/zhzy/Downloads/xcy/Main_Tech/tech_analysis /home/zhzy/Downloads/xcy/Main_Tech/paperData7";

            String keyImport_no_plate = "/bin/bash " + import_dir+"import.sh " +
                    neo4jdatabasepath+" " +
//                    csv_dir+"keywords.csv " +
                    csv_dir+"keywordsKey.csv " +
                    csv_dir+"relationshipKey.csv";
//                    csv_dir+"relationship.csv";
//            /bin/bash /home/zhzy/Downloads/xcy/batch-import-tool/import.sh /home/zhzy/Downloads/neo4j-community-3.4.1/data/databases/graph.db /home/zhzy/Downloads/xcy/Main_Tech/tech_analysis/file/test/keywords.csv /home/zhzy/Downloads/xcy/Main_Tech/tech_analysis/file/test/relationship.csv
            System.err.println(keyImport_no_plate);

//            String keyImport_no_plate = "/bin/bash /home/zhzy/Downloads/xcy/Main_Tech/tech_analysis/import.sh /home/zhzy/Downloads/neo4j-community-3.4.1/data/databases/graph.db /home/zhzy/Downloads/xcy/Main_Tech/tech_analysis/file/test/keywords.csv /home/zhzy/Downloads/xcy/Main_Tech/tech_analysis/file/test/relationship.csv";

//            String keyImport = "/bin/bash /home/zhzy/Downloads/xcy/Main_Tech/tech_analysis/import.sh " +
//                    "/usr/neo4j3.1.0/data/databases/paperData1 " +
//                    "/home/zhzy/Downloads/xcy/Main_Tech/tech_analysis/file/keywordsKey.csv " +
//                    "/home/zhzy/Downloads/xcy/Main_Tech/tech_analysis/file/relationshipKey.csv";

//            java -jar buildNeo4jDatabase.jar /home/zhzy/Downloads/xcy/Main_Tech/tech_analysis /home/zhzy/Downloads/xcy/Main_Tech/paperData6
//
//              String[] yearKeyImport = new String[] { "/bin/bash",
//                    "/home/zhzy/Downloads/xcy/batch-import-tool/import.sh",
//                    "/usr/neo4j3.1.0/data/databases/paperData1",
//                    "/home/zhzy/Downloads/xcy/batch-import-tool/file/yearKeywordsKey.csv",
//                    "/home/zhzy/Downloads/xcy/batch-import-tool/file/yearRelationshipKey.csv"};

            String yearKeyImport_no_plate = "/bin/bash " + import_dir+"import.sh " +
                    neo4jdatabasepath+" "  +
                    csv_dir+"yearKeywordsKey.csv " +
                    csv_dir+"yearRelationshipKey.csv";
            System.err.println(yearKeyImport_no_plate);

//            String yearKeyImport = "/bin/bash /home/zhzy/Downloads/xcy/Main_Tech/tech_analysis/import.sh " +
//                        "/usr/neo4j3.1.0/data/databases/paperData1 " +
//                    "/home/zhzy/Downloads/xcy/Main_Tech/tech_analysis/file/yearKeywordsKey.csv " +
//                    "/home/zhzy/Downloads/xcy/Main_Tech/tech_analysis/file/yearRelationshipKey.csv";

            runConsoleOrder(deleteGraph_db);
            System.out.println("删除成功");
            runConsoleOrder(buildBaseNeo4j);

//            myUnzip.unZipFiles(new File(neo4jdatabasepath+".zip"), neo4jdatabasepath+"/");

//            myUnzip.unZipFiles(new File("/usr/neo4j3.1.0/data/databases/paperData1.zip"), "/usr/neo4j3.1.0/data/databases/");

//            System.out.println("解压成功");
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
    public static void runConsoleOrder(String parm){
        try {
            System.out.println("Starting .....");
            System.out.println(parm);
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

    public static void main(String[] args) {
        String import_dir = basePath+File.separator;
        String csv_dir = basePath+File.separator+"file"+File.separator;
        System.out.println(import_dir);
        System.out.println(csv_dir);
//        String parm = neo4jpath+" stop";
//        runConsoleOrder(parm);
        String keyImport_no_plate = "/bin/bash " + import_dir+"import.sh " +
                "/usr/neo4j3.1.0/data/databases/Data2 " +
                csv_dir+"yearKeywordsKey.csv " +
                csv_dir+"yearRelationshipKey.csv";
        runConsoleOrder(keyImport_no_plate);
//        new ImportNeo4j().batch_import();
    }
}
