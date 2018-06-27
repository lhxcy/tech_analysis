package com.tech.analysis.Dao;

import com.tech.analysis.entity.KeywordEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by XCY on 2018/4/18.
 * 该类主要用于创建模型
 */
@Repository
public class LoadWordAndVector {
    @Autowired
    private JdbcTemplate jdbcTemplate;
//    private String basePath = System.getProperty("user.dir");
    private String basePath = LoadWordAndVector.class.getClassLoader().getResource("/").getPath();

    /**
     * 根据语料生成模型
     */
    public void buildModel(){
        try {
//            getOriginalData();
//            GetWordAndVexDoc();
            creatModel();
        }catch (Exception e){
            System.out.println("Ori文件写入错误！");
        }

    }

    /**
     * 从数据库中取出摘要、标题等构成OriginalData.dat  内容是摘要和标题
     */

    public void getOriginalData() throws IOException{
//        BufferedWriter out = new BufferedWriter(new FileWriter("/home/zhzy/Downloads/xcy/tech_analysis/py/model/OriginalData.dat"));;
        BufferedWriter out = new BufferedWriter(new FileWriter(basePath+File.separator+"py"+File.separator+"model"+File.separator+"OriginalData.dat"));;
        try {
            String sql = "select abstract_text_cn,title_cn from Paper ";
//            String sql = "select abstract_text_cn,title_cn from Papertest ";
            int count = 0;
            List<String> dataList = jdbcTemplate.query(sql, new RowMapper<String>(){
                @Override
                public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                    String abstract_text =  rs.getString("abstract_text_cn");
                    String title_text =  rs.getString("title_cn");
                    if (abstract_text == null && title_text == null)
                        return "";
//                    System.out.println(abstract_text+title_text);

                    return title_text+abstract_text;
                }
            });
            int goodCount = 0;
            int badCount = 0;
            System.out.println("size: "+dataList.size());
            if (dataList != null){
                for (String string : dataList){
                    if("".equals(string)){
                        ++badCount;
                        continue;
                    }
//                    if (string.length() < 300){
//                        ++badCount;
//                        continue;
//                    }
                    ++goodCount;
                    System.out.println("line: "+goodCount);
                    out.write(string);
                    out.newLine();
                    out.flush();
                }
            }
            System.out.println("size: "+dataList.size());
            dataList.clear();
            System.out.println("goodCount: "+goodCount);
            System.out.println("badCount: "+badCount);
            System.out.println("写入OriginalData.dat成功");
        }catch (Exception e){
            System.out.println("写入OriginalData.dat失败");
            e.printStackTrace();
//            out.close();
        }finally {
            out.close();
        }

    }

    /**
     * 构建jieba分词所用的前置字典，根据数据集构建
     */
    public void buildKeyDict(){
        HashMap<String, KeywordEntity> keywords = UtilRead.readKeywords();
        HashMap<String, Long> keywordTimes = UtilRead.readKeywordTimes();

        int count = 0;
//        String filename = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/keyDict.dat";
        String filename = basePath+File.separator+"py"+File.separator+"model"+File.separator+"keyDict.dat";
        File file = new File(filename);
        try {
            Writer write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            for (String string : keywords.keySet()){
                write.write(string+" "+(200+keywordTimes.get(string))+"\n");
                ++count;
            }
            write.flush();
            write.close();
            System.out.println(count);
            System.out.println("构建自己的前置字典keyDict.dat成功");
        }catch (Exception e){
            System.out.println("构建自己的前置字典keyDict.dat失败");
            e.printStackTrace();
        }
    }

    /**
     * 创建加载模型所需的Word2Vec文件
     *
     */
    public void GetWordAndVexDoc(){
        try {
//            buildKeyDict();
            System.out.println("Starting execute python word2vec model");
            String[] parm = new String[] { "/usr/bin/python3",
                    basePath+File.separator+"py"+File.separator+"model"+File.separator+"Word2VectorBasedGensim.py"};
//                    basePath+File.separator+"py"+File.separator+"model"+File.separator+"Word2VectorBasedGensim.py"};

            Process pr = Runtime.getRuntime().exec(parm);
            pr.waitFor();
            BufferedReader in = new BufferedReader(new
                    InputStreamReader(pr.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            pr.waitFor();
            System.out.println("End python word2vector训练完毕");
        }catch (Exception e){
            System.out.println("调用GetWordAndVexDoc训练模型失败！");
        }
    }
    /**
     * 加载词和向量的文件，构建成以词为键，向量为值的hashmap
     */
    public void creatModel(){
//        String filename = "E:\\tech_analysis\\py\\model\\Word2Vec.dat"; //词和向量存储的文件位置
//        String filename = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/Word2Vec.dat"; //词和向量存储的文件位置
//        String filename = basePath+File.separator+"py"+File.separator+"model"+File.separator+"Word2Vec.dat"; //词和向量存储的文件位置
        String filename = basePath+File.separator+"py"+File.separator+"model"+File.separator+"Word2Vec_more.dat"; //词和向量存储的文件位置
        BufferedReader in;
        HashMap<String, double[]> wordMap = new HashMap<String, double[]>();
        int i = 0;
        try {
            System.out.println("starting......");
            in = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
            String line;
            while ((line = in.readLine()) != null){
                ++i;
                System.out.println("deal: " + i);
//                if (i > 10)
//                    break;
                String[] split = line.split(",");
                double[] doubles = new double[150];
                for (int index = 1; index < split.length-1;++index)
                    doubles[index] = Double.parseDouble(split[index]);
                wordMap.put(split[0],doubles);
//                System.out.println();
            }
            System.out.println(i);
            System.out.println(wordMap.size());
            UtilWrite.WriteModel(wordMap);
            System.out.println("构建词和向量的对象成功！");
        }catch (Exception e){
            System.out.println("构建词和向量的对象成功");
        }
    }



    public static void main(String[] args){
//        new LoadWordAndVector().getOriginalData();
//        new LoadWordAndVector().GetWordAndVexDoc();
        new LoadWordAndVector().creatModel();
//        System.out.println(Double.parseDouble("12.3"));
    }
}
