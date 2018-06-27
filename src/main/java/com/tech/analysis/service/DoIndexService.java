package com.tech.analysis.service;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.util.Version;
import org.springframework.stereotype.Service;
import org.wltea.analyzer.lucene.IKAnalyzer;
///home/zhzy/Downloads/xcy/Main_Tech/tech_analysis/IKAnalyzer3.2.5Stable.jar

@Service
public class DoIndexService {
    private static String basePath = DoIndexService.class.getClassLoader().getResource("/").getPath();
//        private static String basePath = System.getProperty("user.dir");
    public  static String creatIndex()
    {
        final String cfn = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        final  String url = "jdbc:sqlserver://localhost:1433;DatabaseName=TEST";
        String status="建立索引成功";
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet res = null;
        System.out.println("读取数据库数据并索引"+res);
        long start = System.currentTimeMillis();
        int i=0;
//        File indexDir =   new  File( System.getProperty("user.dir")+System.getProperty("file.separator")+"expert_index" );
        File indexDir =   new  File( basePath+System.getProperty("file.separator")+"expert_index" );
//            File file = new File(delpath);
        try {
            Class.forName(cfn);
            con = DriverManager.getConnection(url,"sa","ilyxjin405405");
            String sql = "SELECT\n" +
                    "\tPersons.name,\n" +
                    "\tPersons.enterprisename,\n" +
                    "\tPersons.education,\n" +
                    "\tPersons.functionname,\n" +
                    "\tPersons.type,\n" +
                    "\tPersons.profield,\n" +
                    "\tPersons.sex,\n" +
                    "\tPersons.study_result,\n" +
                    "\tPersons.study_dir,\n" +
                    "\tPersons.keywords,\n" +
                    "\tD.address\n" +
                    "FROM\n" +
                    "    D\n" +
                    "INNER JOIN Persons ON (Persons.enterprisename = D.name)";//查询test表
            statement = con.prepareStatement(sql);
            res = statement.executeQuery();
            System.out.println(res);
            Directory dir=FSDirectory.open(indexDir);
            IndexWriterConfig  iwc = new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35));
            IndexWriter writer=null;
            try
            {
                writer = new IndexWriter(dir,iwc);
            }catch (Exception e)
            {
                System.out.println("创建IndexWriter时发生错误!");
            }
            while(res.next()){
                i=i+1;
                System.out.println(i);
                String title = res.getString("name");//获取test_name列的元素                                                                                                                                                    ;
                System.out.println("姓名："+title);
                Document doc= getDocument(res);
                writer.addDocument(doc);
                System.out.println("索引创建完毕");
                System.out.print(System.currentTimeMillis() - start);
                System.out.println(" total milliseconds");
            }
            writer.close();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            System.out.println(e);
            status="建立索引失败";
        }finally{
            try {
                if(res != null) res.close();
                if(statement != null) statement.close();
                if(con != null) con.close();
            } catch (Exception e2) {
                // TODO: handle exception
                e2.printStackTrace();
            }
        }
        return  status;
    }
    public  static String creat_yangqiIndex()
    {
        final String cfn = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        final  String url = "jdbc:sqlserver://10.168.103.8:1433;DatabaseName=STIMSTEST";
        String status="建立索引成功";
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet res = null;
        System.out.println("读取数据库数据并索引"+res);
        long start = System.currentTimeMillis();
        int i=0;
        String Enterid="0";
//        Enterid=getId(System.getProperty("user.dir")+System.getProperty("file.separator")+"flag/EnterpriseInfo.txt" ,Enterid);
        Enterid=getId(basePath+System.getProperty("file.separator")+"flag/EnterpriseInfo.txt" ,Enterid);
//        File indexDir =   new  File( System.getProperty("user.dir")+System.getProperty("file.separator")+"yangqi_index" );
        File indexDir =   new  File( basePath+System.getProperty("file.separator")+"yangqi_index" );
        try {
            Class.forName(cfn);
            con = DriverManager.getConnection(url,"sa","1q2w3e4r5t!");
            String sql = "SELECT  [name]\n" +
                    "      ,[address]\n" +
                    "      ,[keywords]\n" +
                    "  FROM [STIMSTEST].[dbo].[EnterpriseInfo] where keywords is not null and id>"+Enterid;//查询test表
            statement = con.prepareStatement(sql);
            res = statement.executeQuery();
            System.out.println(res);
            Directory dir=FSDirectory.open(indexDir);
            IndexWriterConfig  iwc = new IndexWriterConfig(Version.LUCENE_35, new IKAnalyzer());
            IndexWriter writer=null;
            try
            {
                writer = new IndexWriter(dir,iwc);
            }catch (Exception e)
            {
                System.out.println("创建IndexWriter时发生错误!");
            }
            while(res.next()){
                i=i+1;
                System.out.println(i);
                String title = res.getString("name");//获取test_name列的元素                                                                                                                                                    ;
                System.out.println("企业名字是："+title);
                Document doc= getyangqiDocument(res);
                writer.addDocument(doc);
                System.out.println("索引创建完毕");
                System.out.print(System.currentTimeMillis() - start);
                System.out.println(" total milliseconds");
            }
            sql="select max(id) as id from EnterpriseInfo";
            statement = con.prepareStatement(sql);

            res = statement.executeQuery();
            while (res.next())
            {
                Enterid = res.getString("id");//获取test_name列的元素
                System.out.println(Enterid);
            }
//            setId(System.getProperty("user.dir")+System.getProperty("file.separator")+"flag/EnterpriseInfo.txt",Enterid);
            setId(basePath+System.getProperty("file.separator")+"flag/EnterpriseInfo.txt",Enterid);
            writer.close();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            System.out.println(e);
            status="建立索引失败";
        }finally{
            try {
                if(res != null) res.close();
                if(statement != null) statement.close();
                if(con != null) con.close();
            } catch (Exception e2) {
                // TODO: handle exception
                e2.printStackTrace();
            }
        }
        return  status;
    }

    public  static String creat_paperIndex()
    {
        final String cfn = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        final  String url = "jdbc:sqlserver://10.168.103.8:1433;DatabaseName=STIMSTEST";
        String status="建立索引成功";
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet res = null;
        System.out.println("读取数据库数据并索引"+res);
        long start = System.currentTimeMillis();
        int i=0;
        String patentid="0";
        String priceid="0";
        String paperUID="";
//        patentid=getId(System.getProperty("user.dir")+System.getProperty("file.separator")+"flag/patent.txt" ,patentid);
//        priceid=getId(System.getProperty("user.dir")+System.getProperty("file.separator")+"flag/price.txt",priceid);
//        File indexDir =   new  File( System.getProperty("user.dir")+System.getProperty("file.separator")+"paper_index" );
        patentid=getId(basePath+System.getProperty("file.separator")+"flag/patent.txt" ,patentid);
        priceid=getId(basePath+System.getProperty("file.separator")+"flag/price.txt",priceid);
        File indexDir =   new  File( basePath+System.getProperty("file.separator")+"paper_index" );
        try {
            Class.forName(cfn);
            con = DriverManager.getConnection(url,"sa","1q2w3e4r5t!");
            String sql = "select UID,keywords_cn  as name,pubyear from Paper where keywords_cn is not null and (has_keywords='10' or has_keywords='1')\n" +
                    "union\n" +
                    "select code as UID,name,year as pubyear  from Prize where id>"+priceid+"\n" +
                    "union \n" +
                    "select patentnumber as UID,cast(keywords as nvarchar)as name,convert(varchar(4),success_date,120)as pubyear from Patent where id >"+patentid;//查询test表
            statement = con.prepareStatement(sql);
            res = statement.executeQuery();
            System.out.println(res);
            Directory dir=FSDirectory.open(indexDir);
            IndexWriterConfig  iwc = new IndexWriterConfig(Version.LUCENE_35, new IKAnalyzer());
            IndexWriter writer=null;
            try
            {
                writer = new IndexWriter(dir,iwc);
            }catch (Exception e)
            {
                System.out.println("创建IndexWriter时发生错误!");
            }
            while(res.next()){
                i=i+1;
                System.out.println(i);
                String title = res.getString("name");//获取test_name列的元素                                                                                                                                                    ;
                System.out.println("文献关键字："+title);
                Document doc= getpaperDocument(res);
                writer.addDocument(doc);
                System.out.println("索引创建完毕");
                System.out.print(System.currentTimeMillis() - start);
                System.out.println(" total milliseconds");
            }
            //更新paper表格，建立所以以后把10变成11
            sql="update paper set has_keywords='11' where has_keywords='10'";
            statement=con.prepareStatement(sql);
            statement.executeUpdate();
            //把Prize的id读入文件
            sql="select max(id) as id from Prize";
            statement = con.prepareStatement(sql);
            res = statement.executeQuery();
            while (res.next())
            {
                priceid = res.getString("id");//获取test_name列的元素
                System.out.println(priceid);
            }
//            setId(System.getProperty("user.dir")+System.getProperty("file.separator")+"flag/price.txt",priceid);
            setId(basePath+System.getProperty("file.separator")+"flag/price.txt",priceid);
            //把Patent的id读入文件
            sql="select max(id) as id from Patent";
            statement = con.prepareStatement(sql);
            res = statement.executeQuery();
            while (res.next())
            {
                patentid =res.getString("id");//获取test_name列的元素
                System.out.println(patentid);
            }
//            setId(System.getProperty("user.dir")+System.getProperty("file.separator")+"flag/patent.txt",patentid);
            setId(basePath+System.getProperty("file.separator")+"flag/patent.txt",patentid);
            System.out.println("索引创建完毕");
            System.out.print((System.currentTimeMillis() - start)/1000);
            System.out.println(" total milliseconds");
            writer.close();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            System.out.println(e);
            status="建立索引失败";
        }finally{
            try {
                if(res != null) res.close();
                if(statement != null) statement.close();
                if(con != null) con.close();
            } catch (Exception e2) {
                // TODO: handle exception
                e2.printStackTrace();
            }
        }
        return  status;
    }

    public  static String creat_yangqipaperIndex()
    {
        final String cfn = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        final  String url = "jdbc:sqlserver://10.168.103.8:1433;DatabaseName=STIMSTEST";
        String status="建立索引成功";
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet res = null;
        System.out.println("读取数据库数据并索引"+res);
        long start = System.currentTimeMillis();
        int i=0;
        String patentid="0";
        String priceid="0";
        String paperUID="";
//        patentid=getId(System.getProperty("user.dir")+System.getProperty("file.separator")+"flag/yangqipatent.txt" ,patentid);
//        priceid=getId(System.getProperty("user.dir")+System.getProperty("file.separator")+"flag/yangqiprice.txt",priceid);
//        File indexDir =   new  File( System.getProperty("user.dir")+System.getProperty("file.separator")+"yangqipaper_index" );
        patentid=getId(basePath+System.getProperty("file.separator")+"flag/yangqipatent.txt" ,patentid);
        priceid=getId(basePath+System.getProperty("file.separator")+"flag/yangqiprice.txt",priceid);
        File indexDir =   new  File( basePath+System.getProperty("file.separator")+"yangqipaper_index" );
        try {
            Class.forName(cfn);
            con = DriverManager.getConnection(url,"sa","1q2w3e4r5t!");
            String sql = "select UID,keywords_cn  as name,pubyear from Paper where keywords_cn is not null and (has_keywords='10' or has_keywords='1') and UID in (select distinct uid from (SELECT id\n" +
                    "  FROM EnterpriseInfo where enterprisetype = 0) a inner join Address b on a.id = b.companyid)\n" +
                    "union\n" +
                    "select code as UID,name,year as pubyear  from Prize where id>"+priceid+"and code in (select distinct prizecode from (SELECT id\n" +
                    "  FROM EnterpriseInfo where enterprisetype = 0) a inner join Enterprise2Prize b on a.id = b.enterpriseid)\n" +
                    "union \n" +
                    "select patentnumber as UID,cast(keywords as nvarchar)as name,convert(varchar(4),success_date,120)as pubyear from Patent where id >"+patentid+"and id in (select distinct patentid from (SELECT id\n" +
                    "  FROM EnterpriseInfo where enterprisetype = 0) a inner join Patent2Enterprise b on a.id = b.enterpriseid)";//查询test表
            statement = con.prepareStatement(sql);
            res = statement.executeQuery();
            System.out.println(res);
            Directory dir=FSDirectory.open(indexDir);
            IndexWriterConfig  iwc = new IndexWriterConfig(Version.LUCENE_35, new IKAnalyzer());
            IndexWriter writer=null;
            try
            {
                writer = new IndexWriter(dir,iwc);
            }catch (Exception e)
            {
                System.out.println("创建IndexWriter时发生错误!");
            }
            while(res.next()){
                i=i+1;
                System.out.println(i);
                String title = res.getString("name");//获取test_name列的元素                                                                                                                                                    ;
                System.out.println("文献关键字："+title);
                Document doc= getpaperDocument(res);
                writer.addDocument(doc);
                System.out.println("索引创建完毕");
                System.out.print(System.currentTimeMillis() - start);
                System.out.println(" total milliseconds");
            }
            //更新paper表格，建立所以以后把10变成11
            sql="update paper set has_keywords='11' where has_keywords='10'";
            statement=con.prepareStatement(sql);
            statement.executeUpdate();
            //把Prize的id读入文件
            sql="select max(id) as id from Prize";
            statement = con.prepareStatement(sql);
            res = statement.executeQuery();
            while (res.next())
            {
                priceid = res.getString("id");//获取test_name列的元素
                System.out.println(priceid);
            }
//            setId(System.getProperty("user.dir")+System.getProperty("file.separator")+"flag/yangqiprice.txt",priceid);
            setId(basePath+System.getProperty("file.separator")+"flag/yangqiprice.txt",priceid);
            //把Patent的id读入文件
            sql="select max(id) as id from Patent";
            statement = con.prepareStatement(sql);
            res = statement.executeQuery();
            while (res.next())
            {
                patentid =res.getString("id");//获取test_name列的元素
                System.out.println(patentid);
            }
//            setId(System.getProperty("user.dir")+System.getProperty("file.separator")+"flag/yangqipatent.txt",patentid);
            setId(basePath+System.getProperty("file.separator")+"flag/yangqipatent.txt",patentid);
            System.out.println("索引创建完毕");
            System.out.print((System.currentTimeMillis() - start)/1000);
            System.out.println(" total milliseconds");
            writer.close();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            System.out.println(e);
            status="建立索引失败";
        }finally{
            try {
                if(res != null) res.close();
                if(statement != null) statement.close();
                if(con != null) con.close();
            } catch (Exception e2) {
                // TODO: handle exception
                e2.printStackTrace();
            }
        }
        return  status;
    }
    public static Document getDocument(ResultSet res)throws Exception {
        Document doc=new Document();
        try{
            doc.add(new Field("name", res.getString("name"),Field.Store.YES,Index.NOT_ANALYZED));
            doc.add(new Field("enterprisename", res.getString("enterprisename"),Field.Store.YES,Index.NOT_ANALYZED));
            doc.add(new Field("education", res.getString("education"),Field.Store.YES,Index.NOT_ANALYZED));
            doc.add(new Field("functionname", res.getString("functionname"),Field.Store.YES,Index.NOT_ANALYZED));
            doc.add(new Field("zhucedi", res.getString("address"),Field.Store.YES,Index.NOT_ANALYZED));
            doc.add(new Field("study_dir", res.getString("study_dir"),Field.Store.YES,Index.ANALYZED));
            doc.add(new Field("tech_area", res.getString("keywords"),Field.Store.YES,Index.ANALYZED));
            doc.add(new Field("type", res.getString("type"),Field.Store.YES,Index.NOT_ANALYZED));
            doc.add(new Field("profield", res.getString("profield"),Field.Store.YES,Index.NOT_ANALYZED));
            doc.add(new Field("sex", res.getString("sex"),Field.Store.YES,Index.NOT_ANALYZED));
            doc.add(new Field("study_result", res.getString("study_result"),Field.Store.YES,Index.ANALYZED));
            System.out.println(("22222222"));
        }catch (Exception e)
        {
            System.out.println(e);
        }
        return doc;
    }
    //给央企添加索引
    public static Document getyangqiDocument(ResultSet res)throws Exception {
        Document doc=new Document();
        try{
            doc.add(new Field("yangqikeywords", res.getString("keywords"),Field.Store.YES,Index.ANALYZED));
            doc.add(new Field("yangyiname", res.getString("name"),Field.Store.YES,Index.NOT_ANALYZED));
            doc.add(new Field("yangqiaddress", res.getString("address"),Field.Store.YES,Index.NOT_ANALYZED));
        }catch (Exception e)
        {
            System.out.println(e);
        }
        return doc;
    }
    //给paper添加索引
    public static Document getpaperDocument(ResultSet res)throws Exception {
        Document doc=new Document();
        try{
            doc.add(new Field("paperkeywords", res.getString("name"),Field.Store.YES,Index.ANALYZED));
            doc.add(new Field("pubyear", res.getString("pubyear"),Field.Store.YES,Index.NOT_ANALYZED));
            doc.add(new Field("label", res.getString("UID"),Field.Store.YES,Index.NOT_ANALYZED));
        }catch (Exception e)
        {
            System.out.println(e);
        }
        return doc;
    }


    public static String deleteIndex()
    {
        try {
//            String delpath=System.getProperty("user.dir")+System.getProperty("file.separator")+"expert_index";
            String delpath=basePath+System.getProperty("file.separator")+"expert_index";
//            File file = new File(delpath);
            File file = new File(delpath);
            // 当且仅当此抽象路径名表示的文件存在且 是一个目录时，返回 true
            if (!file.isDirectory()) {
                file.delete();
            } else if (file.isDirectory()) {
                String[] filelist = file.list();
                for (int i = 0; i < filelist.length; i++) {
                    File delfile = new File(delpath + "\\" + filelist[i]);
                    if (!delfile.isDirectory()) {
                        delfile.delete();
                        System.out.println(delfile.getAbsolutePath() + "删除文件成功");
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("deletefile() Exception:" + e.getMessage());
        }
        return "删除索引成功";
    }

    public static String deleteyangqiIndex()
    {
        try {
//            String delpath=System.getProperty("user.dir")+System.getProperty("file.separator")+"yangqi_index";
            String delpath=basePath+System.getProperty("file.separator")+"yangqi_index";
//            File file = new File(delpath);
            File file = new File(delpath);
            // 当且仅当此抽象路径名表示的文件存在且 是一个目录时，返回 true
            if (!file.isDirectory()) {
                file.delete();
            } else if (file.isDirectory()) {
                String[] filelist = file.list();
                for (int i = 0; i < filelist.length; i++) {
                    File delfile = new File(delpath + "\\" + filelist[i]);
                    if (!delfile.isDirectory()) {
                        delfile.delete();
                        System.out.println(delfile.getAbsolutePath() + "删除文件成功");
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("deletefile() Exception:" + e.getMessage());
        }
        return "删除索引成功";
    }

    public static String deletepaperIndex()
    {
        try {
//            String delpath=System.getProperty("user.dir")+System.getProperty("file.separator")+"yangqi_index";
            String delpath=basePath+System.getProperty("file.separator")+"yangqi_index";
//            File file = new File(delpath);
            File file = new File(delpath);
            // 当且仅当此抽象路径名表示的文件存在且 是一个目录时，返回 true
            if (!file.isDirectory()) {
                file.delete();
            } else if (file.isDirectory()) {
                String[] filelist = file.list();
                for (int i = 0; i < filelist.length; i++) {
                    File delfile = new File(delpath + "\\" + filelist[i]);
                    if (!delfile.isDirectory()) {
                        delfile.delete();
                        System.out.println(delfile.getAbsolutePath() + "删除文件成功");
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("deletefile() Exception:" + e.getMessage());
        }
        return "删除索引成功";
    }
    public static String getId(String pathname,String id)
    {

        try { // 防止文件建立或读取失败，用catch捕捉错误并打印，也可以throw

        /* 读入TXT文件 */
//        String pathname = "/home/yxj/IdeaProjects/tech_analysis/flag/flag.txt"; // 绝对路径或相对路径都可以，这里是绝对路径，写入文件时演示相对路径
        File filename = new File(pathname); // 要读取以上路径的input。txt文件
        InputStreamReader reader = new InputStreamReader(
                new FileInputStream(filename)); // 建立一个输入流对象reader
        BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
        String line = "";
        id = br.readLine();

            } catch (Exception e) {
        e.printStackTrace();
        }
    return id;
    }
    public static void setId(String pathname ,String id)
    {
        try { // 防止文件建立或读取失败，用catch捕捉错误并打印，也可以throw


            /* 写入Txt文件 */
            File writename = new File(pathname); // 相对路径，如果没有则要建立一个新的output。txt文件
            writename.createNewFile(); // 创建新文件
            BufferedWriter out = new BufferedWriter(new FileWriter(writename));
            out.write(id); // \r\n即为换行
            out.flush(); // 把缓存区内容压入文件
//        out.close(); // 最后记得关闭文件

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
