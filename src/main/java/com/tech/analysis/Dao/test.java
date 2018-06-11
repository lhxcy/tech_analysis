package com.tech.analysis.Dao;

import com.tech.analysis.entity.QueryEntity;
import org.neo4j.cypher.internal.frontend.v2_3.ast.functions.Str;
import org.neo4j.driver.v1.*;
//import org.python.core.Py;
//import org.python.core.PyFunction;
//import org.python.core.PyInteger;
//import org.python.core.PyObject;
//import org.python.util.PythonInterpreter;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.Thread;
import java.util.*;

import static org.neo4j.driver.v1.Values.parameters;


//import org
public class test {
    public void test(){
        try {
            System.out.println("Starting .....");
//            String[] parm = new String[] { "D:\\Python35\\python.exe",
//                    "E:\\tech_analysis\\py\\model\\Line_Prediction.py"};
//            String[] parm = new String[] { "/usr/bin/python3",
//                    "/home/zhzy/Downloads/xcy/tech_analysis/py/model/t.py"};
            String[] deleteGraph_db = new String[] { "rm", "-rf",
                    "/usr/neo4j3.1.0/data/databases/paperData1"};
            String[] unzipGraph_db = new String[] { "unzip",
                    "/home/zhzy/Downloads/xcy/temp/test.txt.zip"};
            String[] keyImport = new String[] { "bash","/home/zhzy/Downloads/xcy/batch-import-tool/import.sh",
                    "/home/zhzy/Downloads/xcy/batch-import-tool/file/keywordsKey.csv", "/home/zhzy/Downloads/xcy/batch-import-tool/file/relationshipKey.csv"};
//            unzip /usr/neo4j3.1.0/data/databases/paperData1.zip
//            String[] parm = new String[] { "/usr/neo4j3.1.0/bin/neo4j",
//                    "start"};
//            String[] parm = new String[] { "rm",
//                    "/home/zhzy/Downloads/xcy/tech_analysis/py/model/rm.txt"};
//            String[] parm = new String[] { "unzip",
//                    "-d", "/home/zhzy/Downloads/xcy/temp/", "/home/zhzy/Downloads/xcy/test.txt.zip"};

            String str = "/bin/bash /home/zhzy/Downloads/xcy/Main_Tech/tech_analysis/import.sh " +
                    "/usr/neo4j3.1.0/data/databases/paperData1 " +
                    "/home/zhzy/Downloads/xcy/Main_Tech/tech_analysis/file/yearKeywordsKey.csv " +
                    "/home/zhzy/Downloads/xcy/Main_Tech/tech_analysis/file/yearRelationshipKey.csv";

            String parm = "/usr/neo4j3.1.0/bin/neo4j start";
            String parm1 = "/usr/neo4j3.1.0/bin/neo4j stop";
//            Process pr = Runtime.getRuntime().exec(parm1);
            Process pr = Runtime.getRuntime().exec(parm);
//            System.out.println(str);
            pr.waitFor();
            BufferedReader in = new BufferedReader(new
                    InputStreamReader(pr.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            pr.waitFor();
            System.out.println("End .....");
        }catch (Exception e){
            System.out.println("调用LearnLinerModel训练模型失败！");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception{
//        HashMap<String, double[]> wordMap = UtilRead.readModel();

//        String basePath = System.getProperty("user.dir");
//        System.out.println(basePath);
//        KeywordsPrediction keywordsPrediction = new KeywordsPrediction();
//        keywordsPrediction.getPredictionKeyword();
//        test t = new test();
//        t.test();
        //论文的合著专家
//        HashMap<String,LinkedList<String>> expertCooperate = UtilRead.readExpertCooperateObject();
//        //企业下对应的专家
//        HashMap<String,LinkedList<String>> enterpriseAndExpert = UtilRead.readEnterpriseAndExpertObject();
//        //专家和所属企业
//        HashMap<String,String> name2Enterprise = UtilRead.readName2EnterpriseObject();
//        //论文的合作机构
//        HashMap<String,LinkedList<String>> paperInstitution = UtilRead.readPaperInstitutionObject();
//        GetEnterpriseAndExpertData getEnterpriseAndExpertData = new GetEnterpriseAndExpertData();
//        System.out.println("expertCooperate size: "+expertCooperate.size());
//        System.out.println("enterpriseAndExpert size: "+enterpriseAndExpert.size());
//        System.out.println("name2Enterprise size: "+name2Enterprise.size());
//        System.out.println("paperInstitution size: "+paperInstitution.size());
//        getEnterpriseAndExpertData.writeName2Enterprise(name2Enterprise, "E:\\tech_analysis_my\\tech_analysis\\py\\model","name2EnterpriseForImport");
//        getEnterpriseAndExpertData.writeEnterpriseAndExpert(enterpriseAndExpert, "E:\\tech_analysis_my\\tech_analysis\\py\\model","enterpriseAndExpertForImport");
//        getEnterpriseAndExpertData.writePaperInstitution(paperInstitution,"E:\\tech_analysis_my\\tech_analysis\\py\\model","paperInstitutionForImport");
//        getEnterpriseAndExpertData.writeExpertCooperate(expertCooperate, "E:\\tech_analysis_my\\tech_analysis\\py\\model","expertCooperateForImport");

//        String str = "黄飞鸿，huanggeihong";
//        str = str.replaceAll("[a-zA-Z]","" );
//        System.out.println(str);
////        com.dataimport.GenerateHashTable generateHashTable = new com.dataimport.GenerateHashTable();
////        String file = "E:\\PycharmCode\\Test\\paper.dat";
////        generateHashTable.generate(file);
//        File dbPath = new File("E:\\PycharmCode\\Neo4j\\paperData");
//        com.dataimport.EdgeAndNodeImport edgeAndNodeImport = new com.dataimport.EdgeAndNodeImport(dbPath);
//        edgeAndNodeImport.ReadHash();
//        edgeAndNodeImport.importNode();
//        edgeAndNodeImport.importRelationship();
//        edgeAndNodeImport.flushIndex();
//        edgeAndNodeImport.shutDownIndex();
//        edgeAndNodeImport.shutDownNeo4j();

//        edgeAndNodeImport.ReadHash();
//        edgeAndNodeImport.importNode();
//        edgeAndNodeImport.importRelationship();
//        edgeAndNodeImport.flushIndex();
//        edgeAndNodeImport.shutDownIndex();
//        edgeAndNodeImport.shutDownNeo4j();
//        LoadWordAndVector loadWordAndVector = new LoadWordAndVector();
//        loadWordAndVector.buildKeyDict();
//        loadWordAndVector.buildModel();
//        QueryEntity queryEntity = new QueryEntity();
//        queryEntity.method();
//        List<String> list = UtilRead.readQuery();
//        System.out.println(list);
//        String str = "asdagfg+\n";
//        System.out.println(str.trim());
//        System.out.println("kkkkkkk");
//        String string = "hghgfjdjfghgl";
//        if (string.contains("hg"))
//            System.out.println("kkkkkkkkkk");
//        test t = new test();
//        t.test();
//        System.out.println("gffhgfh");
//        KeywordsPrediction keywordsPrediction = new KeywordsPrediction();
//        keywordsPrediction.predict();

//        HashMap<String, Long> yearKeywordTimes = new HashMap<String, Long>();
//        yearKeywordTimes = UtilRead.readYearKeywordTimes();
//        System.out.print(yearKeywordTimes.size());


//        String string = "卫星通信2011";
//        System.out.println(string.substring(0,string.length()-4));
        WordModel wordModel = new WordModel();
        System.out.println(wordModel.wordMap.size());
//        List<String> list = wordModel.distance("卫星通信");
//        List<String> list = wordModel.distance("人工智能");
//        List<String> list = wordModel.distance("机器学习");
        List<String> list = wordModel.distance("量子计算");
        if (list != null){
            for (String string : list)
                System.out.println(string);
        }
//        LoadWordAndVector loadWordAndVector = new LoadWordAndVector();
//        loadWordAndVector.buildModel();
//        System.out.println("start");
//        try {
//
////            Process pr = Runtime.getRuntime().exec(
////                    "D:\\Python35\\python.exe ProjectAssist\\\\Word2VectorBasedGensim.py");
//            String[] parm = new String[] { "D:\\Python35\\python.exe",
//                    "E:\\PycharmCode\\ProjectAssist\\Word2VectorBasedGensim.py", "卫星通信" };
////            String[] parm = new String[] { "D:\\Python35\\python.exe",
////                    "helloword.py", "卫星通信","paper.dat" };
////            Process pr = Runtime.getRuntime().exec(
////                    "D:\\Python35\\python.exe helloword.py mmmmm pppp");
//            long time = System.currentTimeMillis();
//            Process pr = Runtime.getRuntime().exec(parm);
////            Thread.sleep(100);
//            pr.waitFor();
////            System.out.println(pr.isAlive());
//            BufferedReader in = new BufferedReader(new
//                    InputStreamReader(pr.getInputStream()));
//            String line;
//            while ((line = in.readLine()) != null) {
////                System.out.println("kkkkkk");
//                System.out.println(line);
////                System.out.println( new String(line.getBytes(),"GBK"));
////                System.out.println( new String(line.getBytes("UTF-8"),"UTF-8"));
//            }
//            System.out.println(System.currentTimeMillis()-time);
//            in.close();
//            pr.waitFor();
//            System.out.println("end");
//        }catch (Exception e){
//
//        }
//        //测试Java调用python
//        Properties props = new Properties();
//        props.put("python.import.site", "false");
//        Properties preprops = System.getProperties();
//        PythonInterpreter.initialize(preprops, props, new String[0]);
//        // 1. Python面向函数式编程: 在Java中调用Python函数
//        System.out.println("kkkkkkk");
//        String pythonFunc = "ProjectAssist\\Word2VectorBasedGensim.py";
//        PythonInterpreter pi1 = new PythonInterpreter();
//        System.out.println("mmmmmmmmmmmm");
//
//        // 加载python程序
//        pi1.execfile(pythonFunc);
//        System.out.println("nnnnnnnnnnn");
//        // 调用Python程序中的函数
//        PyFunction pymodel = pi1.get("loadModel", PyFunction.class);
//        PyObject model = pymodel.__call__();
//        String query = "卫星通讯";
//        System.out.println(query);
//        PyFunction pypre = pi1.get("predict", PyFunction.class);
//        PyObject ans = pypre.__call__(model,Py.newString(query));
//        System.out.println(ans);
//        pi1.cleanup();
//        pi1.close();
//        PythonInterpreter interpreter = new PythonInterpreter();
//        interpreter.exec("print(\"hello code\")");
//        interpreter.execfile("helloword.py");
//        PyFunction pyFunction = interpreter.get("hello", PyFunction.class);
//        // 第一个参数为期望获得的函数（变量）的名字，第二个参数为期望返回的对象类型
//        PyObject pyObject = pyFunction.__call__(); // 调用函数
//
//        System.out.println(pyObject);

//         org.neo4j.driver.v1.Driver driver = GraphDatabase.driver( "bolt://10.168.103.21:7687",
//                AuthTokens.basic( "neo4j", "123456" ));//配置驱动
//         Session session = driver.session();

//        System.out.println("test");
//        KeywordsDao keywordsDao = new KeywordsDao();
//        HashMap<String,ArrayList<Integer>> data = keywordsDao.getData("");
//        String str =keywordsDao.formatJsonString(keywordsDao.getCommunity(data));
//        System.out.println(str);

//        System.out.println("test generateCSV");
//        String filename = "paper.dat";
//        GenerateCSV generateCSV = new GenerateCSV();
//        generateCSV.dealCSV();

//        new ImportNeo4j().importCSV();

//        String string = "\"One Belt One Road\"";
//        System.out.println(string);
//        System.out.println(string.replace("\""," "));

//        String temp = "石英制品拉丝机控制系统!2004!光纤!拉丝机!石墨炉!过程控制总线!控制网络!1";
//        String[] str = temp.split("!");
//        System.out.println(str.length);
//        for (String string:str) {
//            System.out.print(string + " ");
//        }

        //测试年图
//        List<String> list = new LinkedList<>();
//        list = new CreatGraphAboutYear().creatAll();
//        System.out.println(list.size());


//        StatementResult result = session.run(
//                "MATCH (n:yearNewKeyword) WHERE n.year = \"2011\" RETURN n.name AS name,n.year AS year," +
//                        "n.partitionKey AS partitionKey,n.partitionKey1 AS partitionKey1",
//                parameters( "", "" ));
//
//        int count = 0;
//        int good = 0;
//        int bad = 0;
//        while ( result.hasNext() )
//        {
//            ++count;
//            Record record = result.next();
//            try {
//                ArrayList<Integer> templist = new ArrayList<Integer>();
//                System.out.println(record.get("partitionKey").asInt());
//                System.out.println(record.get("partitionKey1").asInt());
//                System.out.println(record.get( "name" ).asString());
////                templist.add(record.get("partitionKey").asInt());
////                templist.add(record.get("partitionKey1").asInt());
////                data.put(record.get( "name" ).asString(),templist);
//                ++good;
//            }catch (Exception e){
//                ++bad;
//                continue;
//            }
//        }
//        System.out.println("year: "+ 2011 + "总数： "+count);
//        System.out.println("year: "+ 2011 + "bad总数： "+bad);
//        System.out.println("year: "+ 2011 + "good总数： "+good);
    }


}
