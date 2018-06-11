package com.tech.analysis.Dao;

import com.tech.analysis.entity.AuthorEntity;
import org.neo4j.cypher.internal.frontend.v2_3.ast.functions.Str;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by XCY on 2018/5/23.
 */
@Repository
public class GetEnterpriseAndExpertData {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private String basePath = System.getProperty("user.dir");
    //        String test = basePath+File.separator+"py"+File.separator+"model"+File.separator;
    public void getAllData(){
        //得到一片论文所属专家的对象
//        getSQLExpertData();
        //得到论文所属机构的对象
//        getPaperInstitutionFormSql();
        //得到专家和企业的对应关系对象
        getSQLExpertAndEnterpriseData();


////        HashMap<String,LinkedList<String>> institutionData = getPaperInstitutionFormSql();
////        getName2EnterpriseForGenerateHashTable();
//        HashMap<String,LinkedList<String>> expertCooperate = getSQLExpertData();
//        HashMap<String,LinkedList<String>> enterpriseAndExpert = getSQLExpertAndEnterpriseData();
////        HashMap<String,LinkedList<String>> expertCooperate = UtilRead.readExpertCooperateObject();
//        int count = 0;
//        for (String string : expertCooperate.keySet())
//            if (expertCooperate.get(string).size() >1){
//                ++count;
//                System.out.println(string+": "+"size: "+expertCooperate.get(string).size()+" "+expertCooperate.get(string));
//            }
//        System.out.println("count: "+count);
//        System.out.println("count: "+expertCooperate.size());
////        HashMap<String,LinkedList<String>> enterpriseAndExpert = UtilRead.readEnterpriseAndExpertObject();
//        count = 0;
//        for (String string : enterpriseAndExpert.keySet()){
//            if (enterpriseAndExpert.get(string).size() > 1){
//                ++count;
//                System.out.println(string+": "+"size: "+enterpriseAndExpert.get(string).size()+" "+enterpriseAndExpert.get(string));
//            }
//        }
//        System.out.println("count: "+count);
//        System.out.println(enterpriseAndExpert.size());
        //专家合作关系
//        HashMap<String,LinkedList<String>> expertCooperate = getSQLExpertData();
//        //专家所属机构和专家同事
//        HashMap<String,LinkedList<String>> enterpriseAndExpert = getSQLExpertAndEnterpriseData();
//        UtilWrite.WriteExpertCooperateObject(expertCooperate);
//        UtilWrite.WriteEnterpriseAndExpertObject(enterpriseAndExpert);
    }

//    public void getName2EnterpriseForGenerateHashTable(){
//        HashMap<String,LinkedList<String>> expertCooperate = getSQLExpertData();
//        HashMap<String,LinkedList<String>> enterpriseAndExpert = getSQLExpertAndEnterpriseData();
//        HashMap<String, AuthorEntity> authorEntityHashMap = new HashMap<>();
//        HashMap<String,String> name2Enterprise = new HashMap<>();
//        for (String enterpriseName : enterpriseAndExpert.keySet()){
//            for (String expertName : enterpriseAndExpert.get(enterpriseName)){
//                if (!name2Enterprise.containsKey(expertName)){
////                    AuthorEntity authorEntity = new AuthorEntity(expertName,enterpriseName);
//                    name2Enterprise.put(expertName,enterpriseName);
//                }
//            }
//        }
//        for (String name : name2Enterprise.keySet())
//            System.out.println(name+","+name2Enterprise.get(name));
//        System.out.println(name2Enterprise.size());
////        for (String uid : expertCooperate.keySet()){
////            for (String name : expertCooperate.get(uid)){
////
////            }
////        }
//    }


    /**
     * 返回  论文uid对应的机构（论文uid，发表该论文的机构的id，发表该论文的机构的名字）
     * HashMap<String,HashMap<String,String>> CSCD:5606807,46036,重庆声光电智联电子有限公司
     * @return
     */
//    public HashMap<String,LinkedList<String>> getPaperInstitutionFormSql(){
    public void getPaperInstitutionFormSql(){
        HashMap<String,HashMap<String,String>> tempInstitutionData = new HashMap<>();
        HashMap<String,LinkedList<String>> institutionData = new HashMap<>();
        System.out.println("start get paperInstution data from sql");
        String sql ="select UID,companyid,organization from Address";
        List<String> sqlDataList = jdbcTemplate.query(sql, new RowMapper<String>(){
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                String uid =  rs.getString("UID");
                String companyid =  rs.getString("companyid");
                String institutionName =  rs.getString("organization");
                if (institutionName == null || "".equals(institutionName))
//                if (institutionName == null || "".equals(institutionName) ||
//                        institutionName.contains("C") || institutionName.contains("T"))
                    return "";
                System.out.println(uid+","+companyid+","+institutionName);
                return (uid.trim()+","+companyid.trim()+","+institutionName.trim()).replace("\n"," ");
            }
        });
        for (String string : sqlDataList){
            if(string == null || "".equals(string)) continue;
            String[] stringSplit = string.split(",");
            if (stringSplit.length < 3) continue;
            if (tempInstitutionData.get(stringSplit[0]) == null){
                HashMap<String,String> tempMap = new HashMap<>();
                tempMap.put(stringSplit[1],stringSplit[2]);
                tempInstitutionData.put(stringSplit[0],tempMap);
            }else {
                HashMap<String,String> tempMap = tempInstitutionData.get(stringSplit[0]);
                if (!tempMap.containsKey(stringSplit[1])){
                    tempMap.put(stringSplit[1],stringSplit[2]);
                    tempInstitutionData.put(stringSplit[0],tempMap);
                }
            }
        }


        //去除companyid
        int count = 0;
        for (String uid : tempInstitutionData.keySet()){
            System.out.println(++count);
            if (institutionData.size() > 200000) break;
            if (!institutionData.containsKey(uid)){
                HashMap<String,String> tempMap = tempInstitutionData.get(uid);
                LinkedList<String> tempList = new LinkedList<>();
                for (String companyid : tempMap.keySet()){
                    tempList.add(tempMap.get(companyid));
                }
                institutionData.put(uid,tempList);
            }
        }

//        int count = 0;
//        for (String string : institutionData.keySet()){
//            if (institutionData.get(string).size() > 1){
//                ++count;
//                System.out.println(string+": "+"size: "+institutionData.get(string).size()+" "+institutionData.get(string));
//            }
//        }
//        System.out.println("count: "+count);

        System.out.println("institutionData size : "+institutionData.size());
        System.out.println("sqlDataList size : "+sqlDataList.size());
//        System.out.println(sqlDataList.size());
//        UtilWrite.WritePaperInstitutionObject(institutionData);
//        writePaperInstitution(institutionData,"/home/zhzy/Downloads/xcy/tech_analysis/py/model","paperInstitutionForImport");
//        writePaperInstitution(institutionData,"E:\\tech_analysis_my\\tech_analysis\\py\\model","paperInstitutionForImport");
        writePaperInstitution(institutionData,basePath+File.separator+"py"+File.separator+"model","paperInstitutionForImport");
        institutionData.clear();
//        return institutionData;
    }

    /**
     * 从数据库中得到撰写一片论文的共同作者
     * HashMap<String,LinkedList<String>>  键为论文uid，值为作者列表
     * @return
     */
//    public HashMap<String,LinkedList<String>> getSQLExpertData(){
    public void getSQLExpertData(){
        HashMap<String,LinkedList<String>> expertData = new HashMap<>();
        System.out.println("start get expert data from sql");
//        String sql ="select full_name from Author where UID in( select UID from paper)";
        String sql ="select UID,full_name from Author";
        List<String> sqlDataList = jdbcTemplate.query(sql, new RowMapper<String>(){
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                String uid =  rs.getString("UID");
                String name =  rs.getString("full_name");
//                name = name.replaceAll("[a-zA-Z]","" );
                if (name == null || "".equals(name))
                    return "";
                System.out.println(uid+","+name);
                return uid.trim()+","+name.trim();
            }
        });

        int count = 0;
        for (String string : sqlDataList){
            String[] stringSplit = string.split(",");
            if (stringSplit.length < 2) continue;
            System.out.println(++count);
            if (expertData.size() > 200000) break;
            if (expertData.get(stringSplit[0]) == null){
                LinkedList<String> tempList = new LinkedList<>();
                tempList.add(stringSplit[1]);
                expertData.put(stringSplit[0],tempList);
            }else {
                LinkedList<String> tempList = expertData.get(stringSplit[0]);
                tempList.add(stringSplit[1]);
                expertData.put(stringSplit[0],tempList);
            }
        }
        System.out.println("expert datalist size: "+expertData.size());
//        UtilWrite.WriteExpertCooperateObject(expertData);
//        writeExpertCooperate(expertData,"/home/zhzy/Downloads/xcy/tech_analysis/py/model","expertCooperateForImport");
//        writeExpertCooperate(expertData,"E:\\tech_analysis_my\\tech_analysis\\py\\model","expertCooperateForImport");
        writeExpertCooperate(expertData,basePath+File.separator+"py"+File.separator+"model","expertCooperateForImport");
        expertData.clear();

//        int count = 0;
//        for (String string : expertData.keySet())
//            if (expertData.get(string).size() >1){
//                ++count;
//                System.out.println(string+": "+"size: "+expertData.get(string).size()+" "+expertData.get(string));
//        }
//        System.out.println("count: "+count);
//        System.out.println("count: "+expertData.size());
//        return expertData;
    }


    /**
     * 从数据空中得到属于某一企业的专家列表
     * HashMap<String,LinkedList<String>> 键为企业名字，值为专家名字
     * @return
     */
//    public HashMap<String,LinkedList<String>> getSQLExpertAndEnterpriseData(){
    public void getSQLExpertAndEnterpriseData(){
        String sql_id_expert ="select id,name from expert";
        String sql_id_enterprise ="select id,name from enterpriseinfo";
        String sql_experid_enterpriseid ="select expertid,enterpriseid from expert2enterprise";
        //专家id为键，name为值
        HashMap<String,String> id2expert = getSQLData(sql_id_expert,"id","name");
        //企业id为键，name为值
        HashMap<String,String> id2enterprise = getSQLData(sql_id_enterprise,"id","name");
        //专家id为键，企业id为值
        HashMap<String,String> experid2enterpriseid = getSQLData(sql_experid_enterpriseid,"expertid","enterpriseid");

        HashMap<String,LinkedList<String>> enterpriseAndExpert = new HashMap<>();
        for (String expertid : experid2enterpriseid.keySet()){
            if (enterpriseAndExpert.size() > 200000) break;
            String enterpriseid = experid2enterpriseid.get(expertid);
            String enterpriseName = id2enterprise.get(enterpriseid);
            if (enterpriseAndExpert.get(enterpriseName) == null){
                LinkedList<String> tempList = new LinkedList<>();
                tempList.add(id2expert.get(expertid));
                enterpriseAndExpert.put(enterpriseName,tempList);
            }else {
                LinkedList<String> tempList = enterpriseAndExpert.get(enterpriseName);
                tempList.add(id2expert.get(expertid));
                enterpriseAndExpert.put(enterpriseName,tempList);
            }
        }

        HashMap<String,String> name2Enterprise = new HashMap<>();
        for (String enterpriseName : enterpriseAndExpert.keySet()){
            for (String expertName : enterpriseAndExpert.get(enterpriseName)){
                if (!name2Enterprise.containsKey(expertName)){
//                    AuthorEntity authorEntity = new AuthorEntity(expertName,enterpriseName);
                    name2Enterprise.put(expertName,enterpriseName);
                }
            }
        }
//
//        int count = 0;
//        for (String string : enterpriseAndExpert.keySet()){
//            if (enterpriseAndExpert.get(string).size() > 1){
//                ++count;
//                System.out.println(string+": "+"size: "+enterpriseAndExpert.get(string).size()+" "+enterpriseAndExpert.get(string));
//            }
//        }
//        System.out.println("count: "+count);
        System.out.println("enterpriseAndExpert size: "+enterpriseAndExpert.size());
        System.out.println("name2Enterprise size: "+name2Enterprise.size());

//        UtilWrite.WriteEnterpriseAndExpertObject(enterpriseAndExpert);
//        UtilWrite.WriteName2EnterpriseObject(name2Enterprise);

//        writeEnterpriseAndExpert(enterpriseAndExpert,"/home/zhzy/Downloads/xcy/tech_analysis/py/model","enterpriseAndExpertForImport");
//        writeName2Enterprise(name2Enterprise,"/home/zhzy/Downloads/xcy/tech_analysis/py/model","name2EnterpriseForImport");

//        writeEnterpriseAndExpert(enterpriseAndExpert,"E:\\tech_analysis_my\\tech_analysis\\py\\model","enterpriseAndExpertForImport");
//        writeName2Enterprise(name2Enterprise,"E:\\tech_analysis_my\\tech_analysis\\py\\model","name2EnterpriseForImport");
        writeEnterpriseAndExpert(enterpriseAndExpert,basePath+File.separator+"py"+File.separator+"model","enterpriseAndExpertForImport");
        writeName2Enterprise(name2Enterprise,basePath+File.separator+"py"+File.separator+"model","name2EnterpriseForImport");
        enterpriseAndExpert.clear();
        name2Enterprise.clear();
//        return enterpriseAndExpert;
    }


    /**
     * 从数据库得到指定表的指定列名的数据
     * @param sql sql语句
     * @param first 第一列列名
     * @param second 第二列列名
     * @return HashMap<String,String> 第一列为键，第二列为值
     */
    public HashMap<String,String> getSQLData(String sql,String first, String second){
        System.out.println("start get expert and enterprise data from sql");
//        String sql ="select id,name from expert";
//        String sql ="select id,name from enterpriseinfo";
        List<String> sqlDataList = jdbcTemplate.query(sql, new RowMapper<String>(){
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                String id =  rs.getString(first);
//                String keywords =  rs.getString("keywords");
                String name =  rs.getString(second);
                if (id == null || name == null)
                    return "";
                System.out.println(id+","+name);
                return id.trim()+","+name.trim();
            }
        });

        HashMap<String,String> map = new HashMap<>();
        int count = 0;
        for (String string : sqlDataList){
            if (string == null || "".equals(string)) continue;
            System.out.println(++count);
//            if (map.size() > 200000) break;
            String[] stringSplit = string.split(",");
            map.put(stringSplit[0],stringSplit[1]);
        }
//        System.out.println("data size: " + map.size());
        return map;
    }

    /* 将读取到的数据写入csv， 供创建关系使用*/
    /**
     * 机构和在里面工作的专家
     * 第一列为企业名  其后面为在该企业内的专家
     * @param enterpriseAndExpert
     * @param filePath
     * @param fileName
     * @return
     */
    public boolean writeEnterpriseAndExpert(HashMap<String,LinkedList<String>> enterpriseAndExpert,String filePath, String fileName){
        // 标记文件生成是否成功
        boolean flag = true;

        //文件总行数
        long countline = 0;

        // 拼接文件完整路径
        String fullPath = filePath + File.separator + fileName + ".csv";

        // 生成csv格式文件
        try {
            // 保证创建一个新文件
            File file = new File(fullPath);
            if (!file.getParentFile().exists()) { // 如果父目录不存在，创建父目录
                file.getParentFile().mkdirs();
            }
            if (file.exists()) { // 如果已存在,删除旧文件
                file.delete();
            }
            file.createNewFile();


            // 将格式化后的字符串写入文件
            Writer write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            for (String enterpriseName : enterpriseAndExpert.keySet()){
                String temp = enterpriseName;
                for (String expertName : enterpriseAndExpert.get(enterpriseName)){
                    temp += ","+ expertName;
                }
                write.write(temp);
                write.write('\n');
                ++countline;
            }
            write.flush();
            write.close();
            System.out.println("机构和在里面工作的专家CSV写入成功！");
        } catch (IOException e) {
            flag = false;
            System.out.println("机构和在里面工作的专家CSV写入失败 !");
            e.printStackTrace();
        }
        System.out.println(countline);
        // 返回是否成功的标记
        return flag;

    }

    /**
     * 写入专家和所属的企业
     * 第一列为专家名  第二列为所属企业名
     * @param name2Enterprise
     * @param filePath
     * @param fileName
     * @return
     */
    public boolean writeName2Enterprise(HashMap<String,String> name2Enterprise,String filePath, String fileName){
        // 标记文件生成是否成功
        boolean flag = true;

        //文件总行数
        long countline = 0;

        // 拼接文件完整路径
        String fullPath = filePath + File.separator + fileName + ".csv";

        // 生成csv格式文件
        try {
            // 保证创建一个新文件
            File file = new File(fullPath);
            if (!file.getParentFile().exists()) { // 如果父目录不存在，创建父目录
                file.getParentFile().mkdirs();
            }
            if (file.exists()) { // 如果已存在,删除旧文件
                file.delete();
            }
            file.createNewFile();


            // 将格式化后的字符串写入文件
            Writer write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            for (String expertName : name2Enterprise.keySet()){
                String temp = expertName+","+name2Enterprise.get(expertName);
                write.write(temp);
                write.write('\n');
                ++countline;
            }
            write.flush();
            write.close();
            System.out.println("专家和所属机构CSV写入成功！");
        } catch (IOException e) {
            flag = false;
            System.out.println("专家和所属机构CSV写入失败 !");
            e.printStackTrace();
        }
        System.out.println(countline);
        // 返回是否成功的标记
        return flag;
    }

    /**
     *写入论文uid和论文合著作者
     * 第一列为uid  其后为合著该论文的作者
     * @param expertData  键为论文uid，值为作者列表
     * @param filePath
     * @param fileName
     * @return
     */
    public boolean writeExpertCooperate(HashMap<String,LinkedList<String>> expertData, String filePath, String fileName){
        // 标记文件生成是否成功
        boolean flag = true;

        //文件总行数
        long countline = 0;

        // 拼接文件完整路径
        String fullPath = filePath + File.separator + fileName + ".csv";

        // 生成csv格式文件
        try {
            // 保证创建一个新文件
            File file = new File(fullPath);
            if (!file.getParentFile().exists()) { // 如果父目录不存在，创建父目录
                file.getParentFile().mkdirs();
            }
            if (file.exists()) { // 如果已存在,删除旧文件
                file.delete();
            }
            file.createNewFile();


            // 将格式化后的字符串写入文件
            Writer write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            for (String uid : expertData.keySet()){
                String temp = uid;
                for (String expertName : expertData.get(uid)){
                    temp += ","+expertName;
                }
                write.write(temp);
                write.write('\n');
                ++countline;
            }
            write.flush();
            write.close();
            System.out.println("论文uid和论文作者CSV写入成功！");
        } catch (IOException e) {
            flag = false;
            System.out.println("论文uid和论文作者CSV写入失败 !");
            e.printStackTrace();
        }
        System.out.println(countline);
        // 返回是否成功的标记
        return flag;
    }

    /**
     * 写下论文uid和写该论文的机构
     * 第一列为论文uid，其后为写改论文的机构
     * @param institutionData
     * @param filePath
     * @param fileName
     * @return
     */
    public boolean writePaperInstitution(HashMap<String,LinkedList<String>> institutionData,String filePath, String fileName){
        // 标记文件生成是否成功
        boolean flag = true;

        //文件总行数
        long countline = 0;

        // 拼接文件完整路径
        String fullPath = filePath + File.separator + fileName + ".csv";

        // 生成csv格式文件
        try {
            // 保证创建一个新文件
            File file = new File(fullPath);
            if (!file.getParentFile().exists()) { // 如果父目录不存在，创建父目录
                file.getParentFile().mkdirs();
            }
            if (file.exists()) { // 如果已存在,删除旧文件
                file.delete();
            }
            file.createNewFile();


            // 将格式化后的字符串写入文件
            Writer write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            for (String uid : institutionData.keySet()){
                String temp = uid;
                for (String institutionName : institutionData.get(uid)){
                    temp += ","+institutionName;
                }
                write.write(temp);
                write.write('\n');
                ++countline;
            }
            write.flush();
            write.close();
            System.out.println("论文uid和写该论文的机构CSV写入成功！");
        } catch (IOException e) {
            flag = false;
            System.out.println("论文uid和写该论文的机构CSV写入失败 !");
            e.printStackTrace();
        }
        System.out.println(countline);
        // 返回是否成功的标记
        return flag;
    }
}
