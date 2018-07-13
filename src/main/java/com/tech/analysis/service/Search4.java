package com.tech.analysis.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.lucene.queryParser.ParseException;
import org.springframework.stereotype.Service;


import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2018/3/21 0021.
 */
@Service
public class Search4 {
    public  static JsonObject search(String[] str1, String[] str2,int pagesize, int currentPage)  throws IOException, ParseException
    {
        Directory directory = FSDirectory.open(new File("F:\\IdeaProjects\\lucene\\index_test"));
        IndexReader ireader = IndexReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(ireader);
        int len=0;
        for (int i=0;i<str1.length;i++)
        {
            //System.out.println(i+str1[i]);
            if(str1[i]!=null)
            {
                len++;
            }

        }
        String[] searchField=new String[len];
        String[] q=new String[len];
        int[] id=new int[len];
        for (int j=0;j<len;j++)
        {
            searchField[j]=str1[j];
            q[j]=str2[j];
            id[j]=j;
        }
        for (int i=0;i<len;i++)
        {
            //System.out.println(q[i]);
        }
        System.out.println(len);
        TopDocs hits=null;
        if(searchField.length==1)
        {
            System.out.println("单领域查询");
            System.out.println(searchField[0]);
            if(searchField[0].equals("study_dir")||searchField[0].equals("tech_area")) {
                QueryParser parser1 = new QueryParser(Version.LUCENE_35, searchField[0], new StandardAnalyzer(Version.LUCENE_35));
                Query query1 = null;
                try {
                    System.out.println("hello");
                    query1 = parser1.parse(q[0]);//不支持不分词的filed进行查询
                    hits = searcher.search(query1, null, 10);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            else {
                Term term = new Term(searchField[0], q[0]);
                TermQuery termQuery = new TermQuery(term);
                hits=searcher.search(termQuery,null,10);
            }
        }
        else {
            System.out.println("多领域查询");
            System.out.println(searchField[1]);
            System.out.println(searchField[0]);
            BooleanQuery booleanQuery = new BooleanQuery();
            for (int i=0;i<len;i++)
            {
                if (searchField[i].equals("study_dir")||searchField[i].equals("tech_area"))
                {
                    System.out.println(1);
                    QueryParser parser1=new QueryParser(Version.LUCENE_35,searchField[i], new StandardAnalyzer(Version.LUCENE_35));
                    Query query1=null;
                    try{
                        query1=parser1.parse(q[i]);//不支持不分词的filed进行查询
                        booleanQuery.add(query1, Occur.MUST);
                    }catch (Exception e)
                    {
                        System.out.println(e);
                    }
                }
                else if (searchField[i].equals("avoid_people"))
                {
                    System.out.println(2);
                    Term term = new Term("name", q[i]);
                    TermQuery termQuery1 = new TermQuery(term);
                    booleanQuery.add(termQuery1, Occur.MUST_NOT);
                }
                else if(searchField[i].equals("avoid_company"))
                {
                    System.out.println(2);
                    Term term = new Term("enterprisename", q[i]);
                    TermQuery termQuery1 = new TermQuery(term);
                    booleanQuery.add(termQuery1, Occur.MUST_NOT);
                    System.out.println(booleanQuery);
                }
                else
                {
                    System.out.println(3);
                    Term term = new Term(searchField[i], q[i]);
                    TermQuery termQuery1 = new TermQuery(term);
                    booleanQuery.add(termQuery1, Occur.MUST);
                    System.out.println(booleanQuery);
                }
            }
            hits=searcher.search(booleanQuery,null,10);//读取前10个
        }
//        System.out.println(booleanQuery);
        System.out.println("start");
        System.out.println("匹配 "+q[0]+"和"+searchField[0] +"，总共查询到"+hits.totalHits+"个文档");
        System.out.println("start");
        //scoreDoc.doc是匹配记录的序号0,1,2
        JsonObject obj = new JsonObject();
        JsonObject lan1 = new JsonObject();
        int num=hits.scoreDocs.length;
        int pagenum=num/pagesize+1;
        System.out.println("num="+num);
        System.out.println("pagenum="+pagenum);
        System.out.println("currentPage"+currentPage);
        lan1.addProperty("currentPage",currentPage);
        lan1.addProperty("total",num);
        lan1.addProperty("pageSize",pagesize);
        lan1.addProperty("startIndex","better");
        lan1.addProperty("totalPage",pagenum);
        JsonArray array=new JsonArray();
        int index=(currentPage-1)*pagesize;
        for(ScoreDoc scoreDoc:hits.scoreDocs){
            Document doc=searcher.doc(scoreDoc.doc);
            System.out.println(doc.get("name"));
            System.out.println(doc.get("enterprisename"));
        }
        System.out.println(index);
        int sum=0;
        while(sum<pagesize&&index<num)
        {
            System.out.println("start的值");
            JsonObject temp=new JsonObject();
            try{
                System.out.println("y的值"+hits.scoreDocs[index].doc);
            }catch (Exception e)
            {
                System.out.println(e);
            }
            Document doc=searcher.doc(hits.scoreDocs[index].doc);
            System.out.println(doc.get("name"));
            System.out.println(doc.get("enterprisename"));
            temp.addProperty("id",index);
            temp.addProperty("name",doc.get("name"));
            temp.addProperty("sex","男");
            temp.addProperty("function",doc.get("functionname"));
            temp.addProperty("profield","profield");
            temp.addProperty("ename",doc.get("enterprisename"));
            array.add(temp);
            index++;
            sum++;
        }
        lan1.add("searchResult",array);
        JsonObject lan2=new JsonObject();
        JsonArray array1=new JsonArray();
        JsonArray array2=new JsonArray();
        JsonArray array3=new JsonArray();
        JsonArray array4=new JsonArray();
        if(num!=0)
        {
            System.out.println(index+"hhhh");
            Map<String,Integer> type=new HashMap<>();
            Map<String,Integer> function=new HashMap<>();
            Map<String,Integer> education=new HashMap<>();
            for(ScoreDoc scoreDoc:hits.scoreDocs)
            {
//                获取每个文档的分数
                Document doc = searcher.doc(scoreDoc.doc);
                System.out.println(scoreDoc);
                System.out.println(doc.get("type"));
                System.out.println(doc.get("functionname"));
                System.out.println(doc.get("education"));
                if (doc.get("type")!=null&&(doc.get("type").equals("青年海外高层次人才引进计划") || doc.get("type").equals("中国工程院院士") || doc.get("type").equals("全国杰出专业技术人才") || doc.get("type").equals("海外高层次人才引进计划") || doc.get("type").equals("中国科学院院士") || doc.get("type").equals("国家杰出青年科学基金") || doc.get("type").equals("海外高层次人才引进计划（千人计划）") || doc.get("type").equals("百千万人才工程")))
                {
                    System.out.println("type");
                    String experttype = doc.get("type");
                    int x = 0;
                    if (type.containsKey(experttype)) {
                        x = type.get(experttype);
                    }
                    x = x + 1;
                    type.put(experttype, x);
                }
                if (doc.get("functionname")!=null&&(doc.get("functionname").equals("工程师") || doc.get("functionname").equals("研究员") || doc.get("functionname").equals("高级工程师")))
                {
                    System.out.println("functionname");
                    String expertfunction = doc.get("functionname");
                    int x = 0;
                    if (function.containsKey(expertfunction)) {
                        x = function.get(expertfunction);
                    }
                    x = x + 1;
                    function.put(expertfunction, x);
                }
                if (doc.get("education")!=null&&(doc.get("education").equals("博士研究生") || doc.get("education").equals("硕士研究生")))
                {
                    System.out.println("education");
                    String experteducation = doc.get("education");
                    int x = 0;
                    if (education.containsKey(experteducation)) {
                        x = education.get(experteducation);
                    }
                    x = x + 1;
                    education.put(experteducation, x);
                }
            }
            array1=inerst_expert(type,array1);
            array2=inerst_expert(function,array2);
            array3=inerst_expert(education,array3);
            System.out.println("end of if");

        }
        lan2.add("expertType",array1);
        lan2.add("expertFunction",array2);
        lan2.add("expertEducation",array3);
        JsonObject lan3=new JsonObject();
        System.out.println("end of if");
        for(int xiaBiao=0;xiaBiao<searchField.length;xiaBiao++)
        {
            System.out.println(searchField.length);
            JsonObject lanTemp=new JsonObject();
            System.out.println(searchField[xiaBiao]);
            System.out.println(id[xiaBiao]);
            System.out.println(q[xiaBiao]);
            lanTemp.addProperty("type",searchField[xiaBiao]);
            lanTemp.addProperty("id",id[xiaBiao]);
            lanTemp.addProperty("value",q[xiaBiao]);
            array4.add(lanTemp);
            System.out.println("end of if3");
        }
        System.out.println("end of if2");
        obj.add("result",lan1);
        obj.add("collection",lan2);
        obj.add("conditions",array4);
        System.out.println(obj);
        System.out.println("end");
        ireader.close();
        return obj;
    }
    public static JsonArray inerst_expert(Map typelist,JsonArray array)
    {
        Set<Map.Entry<String,Integer>> set2 = typelist.entrySet();
        for(Iterator<Map.Entry<String,Integer>> iter = set2.iterator(); iter.hasNext();){
            Map.Entry<String,Integer> entry = iter.next();
            //System.out.println(entry.getKey());
            //System.out.println(entry.getValue());
            JsonObject temp1=new JsonObject();
            temp1.addProperty("name",entry.getKey());
            temp1.addProperty("value",entry.getValue());
            array.add(temp1);
        }
        return array;
    }
}