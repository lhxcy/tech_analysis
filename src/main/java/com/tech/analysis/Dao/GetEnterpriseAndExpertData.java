package com.tech.analysis.Dao;

import com.tech.analysis.entity.AuthorEntity;
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
 * 从sql中得到专家、企业的信息
 */
@Repository
public class GetEnterpriseAndExpertData {
    @Autowired
    private JdbcTemplate jdbcTemplate;
//    private String basePath = System.getProperty("user.dir");
    private String basePath = GetEnterpriseAndExpertData.class.getClassLoader().getResource("/").getPath();
    private HashMap<String,String> UID2Pubyear = null;
    public void getAllData(){
        UID2Pubyear = getPaperUIDAndPubyear();

        //得到一片论文所属专家的对象
        System.out.println("start to get data");
        getSQLExpertData();//expertCooperateForImport
        //得到论文所属机构的对象
        getPaperInstitutionFormSql();//paperInstitutionForImport
        //得到专家和企业的对应关系对象
        getSQLExpertAndEnterpriseData();//enterpriseAndExpertForImport  name2EnterpriseForImport
        //得到机构和上级机构数据
        getInstitutionAndSuper();
        //得到机构详细信息
        getInstitutionDetailInfo();//institutionDetailInfoForImport
    }

    /**
     * 得到机构和其上级机构
     */
    public void getInstitutionAndSuper(){
        HashMap<String,String> code2fathercode = new HashMap<>();
        HashMap<String,String> code2name = new HashMap<>();
        System.out.println("start get enterpriseinfo data from sql");
        String sql ="select name,code,fathercode from enterpriseinfo";
        List<String> sqlDataList = jdbcTemplate.query(sql, new RowMapper<String>(){
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                String name =  rs.getString("name");
                String code =  rs.getString("code");
                String fathercode =  rs.getString("fathercode");
                if (name == null || "".equals(name)||code == null||fathercode==null)
                    return "";
                //System.out.println(name.trim()+","+code.trim()+","+fathercode.trim());
                return name.trim()+","+code.trim()+","+fathercode.trim();
            }
        });
        for (String string : sqlDataList){
            if (string == null || "".equals(string)) continue;
            String[] stringSplit = string.split(",");
            if (stringSplit.length < 3) continue;
            code2fathercode.put(stringSplit[1],stringSplit[2]);
            code2name.put(stringSplit[1],stringSplit[0]);
        }

//        UtilWrite.WriteCode2FatherCodeObject(code2fathercode);
//        UtilWrite.WriteCode2NameObject(code2name);
        writeCodeAndName(code2fathercode,basePath+File.separator+"py"+File.separator+"model","code2fathercodeForImport");
        writeCodeAndName(code2name,basePath+File.separator+"py"+File.separator+"model","code2nameForImport");
    }

    /**
     * 拿到从UID到时间的映射
     */
    public HashMap<String,String> getPaperUIDAndPubyear(){
        String sql ="select UID,pubyear from paper";
        HashMap<String,String> UID2Pubyear = new HashMap<>();
        List<String> UIDAndPubyearString = jdbcTemplate.query(sql, new RowMapper<String>(){
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                String UID =  rs.getString("UID");
                String pubyear =  rs.getString("pubyear");
                if (pubyear == null || "".equals(pubyear))
                    return "";
//                System.out.println(UID.trim().replace(',',' ')+","+pubyear.trim().replace(',',' '));
                return (UID.trim().replace(',',' ')+","+pubyear.trim().replace(',',' '));
            }
        });

        for (String s : UIDAndPubyearString){
            if (s == null || "".equals(s)) continue;
            String[] splitString = s.split(",");
            if (splitString.length<2) continue;
            UID2Pubyear.put(splitString[0],splitString[1]);
        }
        System.out.println("UID2Pubyear size: "+UID2Pubyear.size());
        return UID2Pubyear;
    }

    /**
     * 另外还有一个表存储 id，机构名，level，provinceid,province,city,cityid，
     * institutionDetailInfoForImport
     */
    public void getInstitutionDetailInfo(){
        System.out.println("start get InstitutionDetailInfo data from sql");
            String sql ="select ei.id,ei.name,level,provinceid,p.name as province,c.name as city," +
                    "cityid from EnterpriseInfo ei left join province p on p.id=ei.provinceid left join city c on c.id=ei.cityid";
        List<String> institutionDetailInfo = jdbcTemplate.query(sql, new RowMapper<String>(){
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                String sql_id =  rs.getString("id");
                String name =  rs.getString("name");
                String level =  rs.getString("level");
                String provinceid =  rs.getString("provinceid");
                String province =  rs.getString("province");
                String city =  rs.getString("city");
                String cityid =  rs.getString("cityid");
                if (name == null || "".equals(name))
                    return "";
                if (province == null || "".equals(province))province = "null";
                if (provinceid == null || "".equals(provinceid))provinceid = "null";
                if (city == null || "".equals(city))city = "null";
                if (cityid == null || "".equals(cityid))cityid = "null";
                if (level == null || "".equals(level))level = "null";

                System.out.println((sql_id.trim()+","+name+","+level+","+provinceid+
                        ","+province+","+city+","+cityid).replace("\n"," "));
                return (sql_id.trim()+","+name+","+level.trim()+","+provinceid.trim()+
                        ","+province.trim()+","+city.trim()+","+cityid.trim()).replace("\n"," ");
            }
        });
        System.out.println("institutionDetailInfo size: "+institutionDetailInfo);
        writeInstitutionDetailInfo(institutionDetailInfo,basePath+File.separator+"py"+File.separator+"model","institutionDetailInfoForImport");
    }

    /**
     * 返回  论文uid对应的机构（论文uid，发表该论文的机构的id，发表该论文的机构的名字）///////////////
     * HashMap<String,HashMap<String,String>> CSCD:5606807,时间@@46036@@重庆声光电智联电子有限公司
     * uid,论文时间，机构名，，，机构名，
     * @return
     */
//    public HashMap<String,LinkedList<String>> getPaperInstitutionFormSql(){
    public void getPaperInstitutionFormSql(){
        HashMap<String,HashMap<String,String>> tempInstitutionData = new HashMap<>();
        HashMap<String,LinkedList<String>> institutionData = new HashMap<>();
        System.out.println("start get paperInstution data from sql");
        String sql ="select UID,companyid,organization from Address";///////////////不一定有pubyear////////////////
        List<String> sqlDataList = jdbcTemplate.query(sql, new RowMapper<String>(){
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                String uid =  rs.getString("UID");
//                String pubyear =  rs.getString("pubyear");
                String companyid =  rs.getString("companyid");
                String institutionName =  rs.getString("organization");
                if (institutionName == null || "".equals(institutionName))
//                if (institutionName == null || "".equals(institutionName) ||
//                        institutionName.contains("C") || institutionName.contains("T"))
                    return "";
//                System.out.println((uid.trim()+"@@"+companyid.trim()+"@@"+institutionName.trim()).replace("\n"," "));
//                return (uid.trim()+","+pubyear+"@@"+companyid.trim()+"@@"+institutionName.trim()).replace("\n"," ");
                return (uid.trim()+"@@"+companyid.trim()+"@@"+institutionName.trim()).replace("\n"," ");
            }
        });
        for (String string : sqlDataList){
            if(string == null || "".equals(string)) continue;
            String[] stringSplit = string.split("@@");
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
//        int count = 0;
        for (String uid : tempInstitutionData.keySet()){
//            System.out.println(++count);
//            if (institutionData.size() > 200000) break;
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
     * 从数据库中得到撰写一片论文的共同作者 /////////////////////////////
     * HashMap<String,LinkedList<String>>  键为论文uid,sql_id,时间，值为作者列表
     * uid,sql_id,时间，作者，作者。。。，作者
     * @return
     */
//    public HashMap<String,LinkedList<String>> getSQLExpertData(){
    public void getSQLExpertData(){
        HashMap<String,LinkedList<String>> expertData = new HashMap<>();
        System.out.println("start get expert data from sql");
//        String sql ="select full_name from Author where UID in( select UID from paper)";
        String sql ="select UID,id,full_name from Author";////////////////不一定有pubyear
        List<String> sqlDataList = jdbcTemplate.query(sql, new RowMapper<String>(){
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                String uid =  rs.getString("UID");
                String name =  rs.getString("full_name");
                String sql_id =  rs.getString("id");
//                String pubyear =  rs.getString("pubyear");
//                name = name.replaceAll("[a-zA-Z]","" );
                if (name == null || "".equals(name))
                    return "";
//                System.out.println(uid.trim()+","+sql_id.trim()+"@@"+name.trim());
                return uid.trim()+"@@"+sql_id.trim()+","+name.trim();
            }
        });

        int count = 0;
        for (String string : sqlDataList){
            String[] stringSplit = string.split("@@");
            if (stringSplit.length < 2) continue;
//            String[] uidAndsqlid = stringSplit[0].split(",");
//            System.out.println(++count);
//            if (expertData.size() > 200000) break;//////////////////////////////////
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

//    public HashMap<String,String> getAddsqlIdExpertSQLData(String sql,String first, String second,String third){
//        System.out.println("start get expert and enterprise data from sql");
////        String sql ="select id,name from expert";
////        String sql ="select id,name from enterpriseinfo";
//        List<String> sqlDataList = jdbcTemplate.query(sql, new RowMapper<String>(){
//            @Override
//            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
//                String id =  rs.getString(first);
////                String keywords =  rs.getString("keywords");
//                String name =  rs.getString(second);
//                if (id == null || name == null)
//                    return "";
////                System.out.println(id+","+name);
//                return id.trim()+","+name.trim();
//            }
//        });
//
//        HashMap<String,String> map = new HashMap<>();
//        int count = 0;
//        for (String string : sqlDataList){
//            if (string == null || "".equals(string)) continue;
////            System.out.println(++count);
////            if (map.size() > 200000) break;
//            String[] stringSplit = string.split(",");
//            map.put(stringSplit[0],stringSplit[1]);
//        }
////        System.out.println("data size: " + map.size());
//        return map;
//    }

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
        HashMap<String,String> id2expert = getSQLData(sql_id_expert,"id","name");////这个id就是数据库id
        //name为键，专家id为值
        HashMap<String,String> expert2id = new HashMap<>();
        for (String id : id2expert.keySet()){
            expert2id.put(id2expert.get(id),id);
        }
        //企业id为键，name为值
        HashMap<String,String> id2enterprise = getSQLData(sql_id_enterprise,"id","name");
        //专家id为键，企业id为值
        HashMap<String,String> experid2enterpriseid = getSQLData(sql_experid_enterpriseid,"expertid","enterpriseid");//////

        HashMap<String,LinkedList<String>> enterpriseAndExpert = new HashMap<>();
        for (String expertid : experid2enterpriseid.keySet()){
//            if (enterpriseAndExpert.size() > 200000) break;
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
        //author_sql_id,专家,机构
        writeName2Enterprise(name2Enterprise,expert2id,basePath+File.separator+"py"+File.separator+"model","name2EnterpriseForImport");
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
//                System.out.println(id+","+name);
                return id.trim()+","+name.trim();
            }
        });

        HashMap<String,String> map = new HashMap<>();
        int count = 0;
        for (String string : sqlDataList){
            if (string == null || "".equals(string)) continue;
//            System.out.println(++count);
//            if (map.size() > 200000) break;
            String[] stringSplit = string.split(",");
            if (stringSplit.length < 2) continue;
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
//                System.out.println(temp);
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
     *
     * author_sql_id,专家,机构////////////////////////
     * @param name2Enterprise
     * @param filePath
     * @param fileName
     * @return
     */
    public boolean writeName2Enterprise(HashMap<String,String> name2Enterprise,HashMap<String,String> expert2id,String filePath, String fileName){
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
                String temp = expert2id.get(expertName)+","+expertName+","+name2Enterprise.get(expertName);
//                System.out.println(temp);
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
     *写入论文uid和论文合著作者//////////////////////////////////
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
//                String[] splitString = key.split(",");
//                if (splitString.length<2) continue;
                String temp = uid;
                boolean flagsql = false;
                for (String expertName : expertData.get(uid)){
//                    System.out.println(expertName);
                    String[] sqlidAndname = expertName.split(",");
                    if (sqlidAndname.length < 2)continue;
                    if (!flagsql){
                        temp += ","+sqlidAndname[0]+","+UID2Pubyear.get(uid)+","+sqlidAndname[1];
                        flagsql = true;
                    }else
                        temp += ","+sqlidAndname[1];
                }
                System.out.println(temp);
                write.write(temp);
                write.write('\n');
                ++countline;
            }
            write.flush();
            write.close();
            System.out.println("论文uid,id,时间和论文作者CSV写入成功！");
        } catch (IOException e) {
            flag = false;
            System.out.println("论文uid,id,时间论文作者CSV写入失败 !");
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
                String temp = uid+","+UID2Pubyear.get(uid);
                for (String institutionName : institutionData.get(uid)){
                    temp += ","+institutionName;
                }
//                System.out.println(temp);
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

    public boolean writeCodeAndName(HashMap<String,String> codeandname,String filePath, String fileName){
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
            for (String tempcodeandname : codeandname.keySet()){
                String temp = tempcodeandname;
                    temp += ","+ codeandname.get(tempcodeandname);
                System.out.println(temp);
                write.write(temp);
                write.write('\n');
                ++countline;
            }
            write.flush();
            write.close();
            System.out.println("机构和机构上级数据CSV写入成功！");
        } catch (IOException e) {
            flag = false;
            System.out.println("机构和机构上级数据CSV写入失败 !");
            e.printStackTrace();
        }
        System.out.println(countline);
        // 返回是否成功的标记
        return flag;

    }

    boolean writeInstitutionDetailInfo(List<String> institutionDetailInfo,String filePath, String fileName){
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
            for (String info : institutionDetailInfo){
                if (info == null || "".equals(info)) continue;
//                System.out.println(info);
                write.write(info);
                write.write('\n');
                ++countline;
            }
            write.flush();
            write.close();
            System.out.println("机构详细信息CSV写入成功！");
        } catch (IOException e) {
            flag = false;
            System.out.println("机构详细信息CSV写入失败 !");
            e.printStackTrace();
        }
        System.out.println(countline);
        // 返回是否成功的标记
        return flag;
    }

}
