package com.tech.analysis.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.omg.PortableServer.LIFESPAN_POLICY_ID;
import org.wltea.analyzer.lucene.IKQueryParser;
import org.wltea.analyzer.lucene.IKSimilarity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.tech.analysis.Dao.WordModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SearchServices
{
    private static Logger logyangqi = LoggerFactory.getLogger("yxjyangqi");
    private static Logger logpaper = LoggerFactory.getLogger("yxjpaper");
    private  static Directory directory=null;
    private  static IndexReader reader=null;

    public static JsonObject searchYangqi(String[] keywords)
    {//
        List<String> list = new ArrayList<String>();
        JsonObject obj=new JsonObject();
        JsonArray array=new JsonArray();
        try {
            directory = FSDirectory.open(new  File( System.getProperty("user.dir")+System.getProperty("file.separator")+"yangqi_index" ));
            reader = IndexReader.open(directory);
        }catch (Exception e)
        {
            System.out.println(e);
        }
        int len;

        len=keywords.length;
//        len=2;

        for (String str:keywords)
        {
            list.add(str);
        }
        extendKeyword(len,keywords,list);
        logyangqi.info("新关键字长度"+list.size());
        int newlen=list.size();
        String[] newkeywords=new String[newlen];
        if(newlen!=0)
        {
            list.toArray(newkeywords);
            for (String y:newkeywords)
            {
                logyangqi.info("key新关键字遍历"+y);
            }
        }
        newlen=newkeywords.length;
        logyangqi.info("新的长度"+newlen);
        IndexSearcher indexSearcher = new IndexSearcher(reader);
        indexSearcher.setSimilarity(new IKSimilarity());
        TopDocs topDocs=null;
        if(newlen==1)
        {
            logyangqi.info("进入央企单字段查询");
            try {
                Query query1 = IKQueryParser.parse("yangqikeywords",keywords[0]);//不支持不分词的filed进行查询
                topDocs = indexSearcher.search(query1,null, 10);
            }catch (Exception e)
            {
                logyangqi.error("查找数据失败");
            }

        }
        else {
            logyangqi.info(""+newlen);
            logyangqi.info("进入央企多字段查询");
            BooleanClause.Occur[] clauses = new BooleanClause.Occur[newlen];
            for (int i=0;i<newlen;i++)
            {
                clauses[i]=BooleanClause.Occur.SHOULD;
            }
            try{
                String[] searchField=new String[newlen];
                for (int i=0;i<newlen;i++)
                    searchField[i]="yangqikeywords";
                logyangqi.info(""+searchField.length);
//                String[] q={"测量区域亮度","右眼通道图像"};
                String[] q=newkeywords;
                Query query1 = IKQueryParser.parseMultiField(searchField,q,clauses);//不支持不分词的filed进行查询
                topDocs = indexSearcher.search(query1,null, 10);
            }catch (Exception e)
            {
                logyangqi.error(e.toString());
                logyangqi.error("查找数据失败");
            }
        }
        try {
            logyangqi.info("结果总数"+topDocs.totalHits);
            for(ScoreDoc scoreDoc:topDocs.scoreDocs){
                JsonObject temp=new JsonObject();
                Document doc=indexSearcher.doc(scoreDoc.doc);
                temp.addProperty("央企名称",doc.get("yangyiname"));
                temp.addProperty("央企地址",doc.get("yangqiaddress"));
                array.add(temp);
//                log.info(array.toString());
                logyangqi.info("成功");
            }
            obj.add("result",array);
            logyangqi.info(obj.toString());
        }catch (Exception e)
        {
            logyangqi.error("数据封装JSON失败");
        }

        return obj;
    }



    public static JsonObject searchpaper(String[] keywords,int year)
    {
        logpaper.info("查找论文、专利、科技奖励");
        List<String> list = new ArrayList<String>();
        JsonObject obj=new JsonObject();
        int papernum=0,patentnum=0,prizenum=0;
        try {
            directory = FSDirectory.open(new  File( System.getProperty("user.dir")+System.getProperty("file.separator")+"paper_index" ));
            reader = IndexReader.open(directory);
        }catch (Exception e)
        {
            logpaper.error("查找索引失败");
        }
        int len;
        int currentyear=2018;
        int range=((currentyear-year)+1)*3;
        int[] sumnum=new int[(currentyear-year)+1];
        int[] num=new int[range];
        len=keywords.length;
        logpaper.info("长度"+len);
        for (String str:keywords)
        {
            list.add(str);
        }
        extendKeyword(len,keywords,list);
//        for(int i=0;i<len;i++)
//        {
//            List<String> temp=new ArrayList<String>();
//            try
//            {
//
//                String x=keywords[i].replace("\"","");
//                logpaper.info("导入字符串："+x);
//                temp=get_word.distance(x);
//            }catch (Exception e)
//            {
//                logpaper.info(e.toString());
//            }
//            if(!temp.isEmpty())
//            {
//                for(String x:temp)
//                {
//                    list.add(x);
//                }
//            }
//
//        }
        logpaper.info("新关键字长度"+list.size());
//        for (String x:list)
//        {
//            log.info("新关键字遍历"+x);
//        }
        int newlen=list.size();
        String[] newkeywords = new String[newlen];
        if(newlen!=0)
        {

            list.toArray(newkeywords);
            for (String y:newkeywords)
            {
                logpaper.info("key新关键字遍历"+y);
            }
        }
        newlen=newkeywords.length;
        logpaper.info("新的长度"+newlen);
        IndexSearcher indexSearcher = new IndexSearcher(reader);
        indexSearcher.setSimilarity(new IKSimilarity());
        TopDocs topDocs=null;
        if(newlen==1)
        {
            logpaper.info("进入单字段查询");
            try {
                Query query1 = IKQueryParser.parse("paperkeywords",keywords[0]);//不支持不分词的filed进行查询
                topDocs = indexSearcher.search(query1,null, 100);
                logpaper.info(""+topDocs.totalHits);
            }catch (Exception e)
            {
                logpaper.error("查找数据失败");
            }

        }
        else {
            logpaper.info("进入多字段查询");
//            BooleanClause.Occur[] clauses = { BooleanClause.Occur.MUST, BooleanClause.Occur.MUST };
            BooleanClause.Occur[] clauses = new BooleanClause.Occur[newlen];
            for (int i=0;i<newlen;i++)
            {
                clauses[i]=BooleanClause.Occur.SHOULD;
            }
            try{
                String[] searchField=new String[newlen];
                for (int i=0;i<newlen;i++)
                    searchField[i]="paperkeywords";
//                String[] q={"高精度","高质量"};
                String[] q=newkeywords;
                Query query1 = IKQueryParser.parseMultiField(searchField,q,clauses);//不支持不分词的filed进行查询
                topDocs = indexSearcher.search(query1,null, 100);
            }catch (Exception e)
            {
                logpaper.error("查找数据失败");
            }
        }
        try {
            logpaper.info("结果总数"+topDocs.totalHits);
            for(ScoreDoc scoreDoc:topDocs.scoreDocs){
                Document doc=indexSearcher.doc(scoreDoc.doc);
                logpaper.info(doc.get("paperkeywords"));
                logpaper.info(doc.get("pubyear"));
                logpaper.info(doc.get("label"));
                int pubyear=Integer.valueOf(doc.get("pubyear"));
                logpaper.info("年份"+pubyear);
                if(pubyear>=year&&pubyear<=currentyear)
                {
                    logpaper.info("查询年份"+pubyear);
                    logpaper.info("进入");
                    int temp=pubyear-year;
                    if(doc.get("label").charAt(0)=='C'||doc.get("label").charAt(0)=='f')
                        num[temp*3]=++num[temp*3];
                    if(doc.get("label").charAt(0)=='F'||doc.get("label").charAt(0)=='Z')
                        num[temp*3+1]=++num[temp*3+1];
                    if(doc.get("label").charAt(1)=='N')
                        num[temp*3+2]=++num[temp*3+2];
                }

            }
            int x=currentyear-year+1;
            JsonArray array=new JsonArray();
            for (int i=0;i<x;i++)
            {
                JsonObject temp=new JsonObject();
                int yearsum=0;
                temp.addProperty("year",i+year);
                temp.addProperty("papernum",num[i*3]);
                temp.addProperty("prizenum",num[i*3+1]);
                temp.addProperty("patentnum",num[i*3+2]);
                yearsum=num[i*3]+num[i*3+1]+num[i*3+2];
                sumnum[i]=yearsum;
                array.add(temp);
            }
            obj.add("data",array);
            String analysis;
            Boolean flag;
            flag=JadgeIncreseArrayWithRecursion(sumnum,0);
            logpaper.info(""+flag);
            if(flag==true)
            {
                analysis="该技术领域近年处于增长趋势，是当前的热门技术领域，提议企业应加紧招聘该领域相关技术人才并可以加大在该领域的投资力度，尽早拿下该技术领域的市场";
            }
            else
            {
                analysis="该技术领域近年处于下滑趋势，该领域已经渐渐的淡出人们的主流视野，提议企业投放部分人员与资金在该技术领域，投放更多精力去其他领域";
            }
            obj.addProperty("text",analysis);
            logpaper.info(obj.toString());
        }catch (Exception e)
        {
            logpaper.error("数据封装JSON失败");
        }

        return obj;
    }

    public static JsonObject searchyangqipaper(String[] keywords,int year)
    {
        logpaper.info("查找论文、专利、科技奖励");
        List<String> list = new ArrayList<String>();
        JsonObject obj=new JsonObject();
        int papernum=0,patentnum=0,prizenum=0;
        try {
            directory = FSDirectory.open(new  File( System.getProperty("user.dir")+System.getProperty("file.separator")+"yangqipaper_index" ));
            reader = IndexReader.open(directory);
        }catch (Exception e)
        {
            logpaper.error("查找索引失败");
        }
        int len;
        int currentyear=2018;
        int range=((currentyear-year)+1)*3;
        int[] sumnum=new int[(currentyear-year)+1];
        int[] num=new int[range];
        len=keywords.length;
        logpaper.info("长度"+len);
        for (String str:keywords)
        {
            list.add(str);
        }
        extendKeyword(len,keywords,list);
        logpaper.info("新关键字长度"+list.size());
//        for (String x:list)
//        {
//            log.info("新关键字遍历"+x);
//        }
        int newlen=list.size();
        String[] newkeywords = new String[newlen];
        if(newlen!=0)
        {

            list.toArray(newkeywords);
            for (String y:newkeywords)
            {
                logpaper.info("key新关键字遍历"+y);
            }
        }
        newlen=newkeywords.length;
        logpaper.info("新的长度"+newlen);
        IndexSearcher indexSearcher = new IndexSearcher(reader);
        indexSearcher.setSimilarity(new IKSimilarity());
        TopDocs topDocs=null;
        if(newlen==1)
        {
            logpaper.info("进入单字段查询");
            try {
                Query query1 = IKQueryParser.parse("paperkeywords",keywords[0]);//不支持不分词的filed进行查询
                topDocs = indexSearcher.search(query1,null, 100);
                logpaper.info(""+topDocs.totalHits);
            }catch (Exception e)
            {
                logpaper.error("查找数据失败");
            }

        }
        else {
            logpaper.info("进入多字段查询");
//            BooleanClause.Occur[] clauses = { BooleanClause.Occur.MUST, BooleanClause.Occur.MUST };
            BooleanClause.Occur[] clauses = new BooleanClause.Occur[newlen];
            for (int i=0;i<newlen;i++)
            {
                clauses[i]=BooleanClause.Occur.SHOULD;
            }
            try{
                String[] searchField=new String[newlen];
                for (int i=0;i<newlen;i++)
                    searchField[i]="paperkeywords";
//                String[] q={"高精度","高质量"};
                String[] q=newkeywords;
                Query query1 = IKQueryParser.parseMultiField(searchField,q,clauses);//不支持不分词的filed进行查询
                topDocs = indexSearcher.search(query1,null, 100);
            }catch (Exception e)
            {
                logpaper.error("查找数据失败");
            }
        }
        try {
            logpaper.info("结果总数"+topDocs.totalHits);
            for(ScoreDoc scoreDoc:topDocs.scoreDocs){
                Document doc=indexSearcher.doc(scoreDoc.doc);
                logpaper.info(doc.get("paperkeywords"));
                logpaper.info(doc.get("pubyear"));
                logpaper.info(doc.get("label"));
                int pubyear=Integer.valueOf(doc.get("pubyear"));
                logpaper.info("年份"+pubyear);
                if(pubyear>=year&&pubyear<=currentyear)
                {
                    logpaper.info("查询年份"+pubyear);
                    logpaper.info("进入");
                    int temp=pubyear-year;
                    if(doc.get("label").charAt(0)=='C'||doc.get("label").charAt(0)=='f')
                        num[temp*3]=++num[temp*3];
                    if(doc.get("label").charAt(0)=='F'||doc.get("label").charAt(0)=='Z')
                        num[temp*3+1]=++num[temp*3+1];
                    if(doc.get("label").charAt(1)=='N')
                        num[temp*3+2]=++num[temp*3+2];
                }

            }
            int x=currentyear-year+1;
            JsonArray array=new JsonArray();
            for (int i=0;i<x;i++)
            {
                JsonObject temp=new JsonObject();
                int yearsum=0;
                temp.addProperty("year",i+year);
                temp.addProperty("papernum",num[i*3]);
                temp.addProperty("prizenum",num[i*3+1]);
                temp.addProperty("patentnum",num[i*3+2]);
                yearsum=num[i*3]+num[i*3+1]+num[i*3+2];
                sumnum[i]=yearsum;
                array.add(temp);
            }
            obj.add("data",array);
            String analysis;
            Boolean flag;
            flag=JadgeIncreseArrayWithRecursion(sumnum,0);
            logpaper.info(""+flag);
            if(flag==true)
            {
                analysis="该技术领域近年处于增长趋势，是当前的热门技术领域，提议企业应加紧招聘该领域相关技术人才并可以加大在该领域的投资力度，尽早拿下该技术领域的市场";
            }
            else
            {
                analysis="该技术领域近年处于下滑趋势，该领域已经渐渐的淡出人们的主流视野，提议企业投放部分人员与资金在该技术领域，投放更多精力去其他领域";
            }
            obj.addProperty("text",analysis);
            logpaper.info(obj.toString());
        }catch (Exception e)
        {
            logpaper.error("数据封装JSON失败");
        }

        return obj;
    }
    public  static  void extendKeyword(int len,String[] keywords,List<String> list)
    {
        WordModel get_word=new WordModel();
        for(int i=0;i<len;i++)
        {
            List<String> temp=new ArrayList<String>();
            try
            {

                String x=keywords[i].replace("\"","");
                logyangqi.info("导入字符串："+x);
                temp=get_word.distance(x);
            }catch (Exception e)
            {
                logyangqi.info(e.toString());
            }
            if(!temp.isEmpty())
            {
                for(String x:temp)
                {
                    list.add(x);
                }
            }

        }
    }


    private static boolean JadgeIncreseArrayWithRecursion(int[] array, int begin) {
        if (begin == array.length - 1) {
            return true;
        } else {
            return array[begin] <= array[begin + 1] && JadgeIncreseArrayWithRecursion(array, begin + 1);
        }
    }
}
