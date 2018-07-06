package com.tech.analysis.Dao;

import com.tech.analysis.entity.*;
import org.neo4j.cypher.internal.frontend.v2_3.ast.functions.Str;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by XCY on 2018/5/29.
 */
@Repository
public class BuildAuthorAndInstitution {
    @Autowired
    private GetEnterpriseAndExpertData getEnterpriseAndExpertData;
//    private String basePath = System.getProperty("user.dir");
    private String basePath =  BuildAuthorAndInstitution.class.getClassLoader().getResource("/").getPath();

    public void getDataAndBuildCSV(){
        getEnterpriseAndExpertData.getAllData();
//        getAuthorAndInstitutionCSV();
    }

    public void getAuthorAndInstitutionCSV(){
        //载入数据库数据
        //论文的合著专家
        HashMap<String,LinkedList<String>> expertCooperate = UtilRead.readExpertCooperateObject();
        //企业下对应的专家
        HashMap<String,LinkedList<String>> enterpriseAndExpert = UtilRead.readEnterpriseAndExpertObject();
        //专家和所属企业
        HashMap<String,String> name2Enterprise = UtilRead.readName2EnterpriseObject();
        //论文的合作机构
        HashMap<String,LinkedList<String>> paperInstitution = UtilRead.readPaperInstitutionObject();
        System.out.println("expertCooperate size: "+expertCooperate.size());
        System.out.println("enterpriseAndExpert size: "+enterpriseAndExpert.size());
        System.out.println("name2Enterprise size: "+name2Enterprise.size());
        System.out.println("paperInstitution size: "+paperInstitution.size());


        //Author出现的次数
        HashMap<String, Long> authorTimes = new HashMap<String, Long>();
        //存储Authors
        HashMap<String, AuthorEntity> authors = new HashMap<String, AuthorEntity>();

        //机构
        HashMap<String, Long> institutionTimes = new HashMap<String, Long>();
        HashMap<String, InstitutionEntity> institutions = new HashMap<String, InstitutionEntity>();

        //3种关系
        //作者共现关系
        HashMap<String, RelationshipEntity> authorsRelationship = new HashMap<String, RelationshipEntity>();
        //机构关系
        HashMap<String, RelationshipEntity> institutionsRelationship = new HashMap<String, RelationshipEntity>();
        //作者所属机构关系
        HashMap<String, RelationshipEntity> unionsRelationship = new HashMap<String, RelationshipEntity>();

        long nodeId = 0l;
        long relationshipId = 0l;
//        for (String string : expertCooperate.keySet()){
//            System.out.println(string+": "+"size: "+expertCooperate.get(string).size()+" "+expertCooperate.get(string));
//        }
//        for (String string : enterpriseAndExpert.keySet()){
//            System.out.println(string+": "+"size: "+enterpriseAndExpert.get(string).size()+" "+enterpriseAndExpert.get(string));
//        }
//        for (String string : paperInstitution.keySet()){
//            System.out.println(string+": "+"size: "+paperInstitution.get(string).size()+" "+paperInstitution.get(string));
//        }
//        for (String string : name2Enterprise.keySet()){
//            System.out.println(string+": "+"size: "+name2Enterprise.size()+" "+name2Enterprise.get(string));
//        }


        /*构建作者关系*/
        for (String uid : expertCooperate.keySet()){
            //存储要建立关系的author实体Id
            List<Long> authorIdList = new ArrayList<Long>();
            for (String authorName : expertCooperate.get(uid)){
//                String autherInstitution = name2Enterprise.get(authorName);
                if (!authors.containsKey(authorName)){
                    AuthorEntity authorEntity = new AuthorEntity(authorName,nodeId);
                    ++nodeId;
                    authorIdList.add(nodeId);
                    authors.put(authorName,authorEntity);
                    authorTimes.put(authorName,1L);
                }else {
                    authorIdList.add(authors.get(authorName).getId());
                    authorTimes.put(authorName,authorTimes.get(authorName)+1L);
                }
            }
            //建立作者合作关系，权重为合作次数
            for (int i = 0; i < authorIdList.size(); ++i){
                Long authorId1 = authorIdList.get(i);
                for (int j = i+1; j < authorIdList.size(); ++j){
                    Long authorId2 = authorIdList.get(j);
                    String relationshipHashKey1 = authorId1.toString()+"_"+authorId2.toString();
                    String relationshipHashKey2 = authorId2.toString()+"_"+authorId1.toString();
                    if (!authorsRelationship.containsKey(relationshipHashKey1) && !authorsRelationship.containsKey(relationshipHashKey2)){
                        RelationshipEntity relationshipEntity = new RelationshipEntity(authorId1,authorId2,1L,"coauthor");
                        authorsRelationship.put(relationshipHashKey1,relationshipEntity);
                    }else if (authorsRelationship.containsKey(relationshipHashKey1)){
                        authorsRelationship.get(relationshipHashKey1).setTimes(authorsRelationship.get(relationshipHashKey1).getTimes()+1L);
                    }else if (authorsRelationship.containsKey(relationshipHashKey2)){
                        authorsRelationship.get(relationshipHashKey2).setTimes(authorsRelationship.get(relationshipHashKey2).getTimes()+1L);
                    }
                }
            }
        }
        /*构建机构关系*/
        for (String uid : paperInstitution.keySet()){
            //存储要建立关系的institution实体Id
            List<Long> institutionIdList = new ArrayList<Long>();
            //往institution表中添加元素
            for (String institutionOfThisPaper : paperInstitution.get(uid)){
                if (!institutions.containsKey(institutionOfThisPaper)){
                    InstitutionEntity institutionEntity = new InstitutionEntity(institutionOfThisPaper,nodeId);
                    ++nodeId;
                    institutionIdList.add(nodeId);
                    institutions.put(institutionOfThisPaper,institutionEntity);
                    institutionTimes.put(institutionOfThisPaper,1L);
                }else {
                    institutionIdList.add(institutions.get(institutionOfThisPaper).getId());
                    institutionTimes.put(institutionOfThisPaper,institutionTimes.get(institutionOfThisPaper)+1L);
                }
            }

            //建立institutions之间的关系 权重为合作次数
            for (int i = 0; i < institutionIdList.size(); ++i){
                Long institutionId1 = institutionIdList.get(i);
                for (int j = i + 1; j < institutionIdList.size(); j++) {
                    Long institutionId2 = institutionIdList.get(j);
                    String relationshipHashKey1 = institutionId1.toString() + "_" + institutionId2.toString();
                    String relationshipHashKey2 = institutionId2.toString() + "_" + institutionId1.toString();
                    if (!institutionsRelationship.containsKey(relationshipHashKey1) && !institutionsRelationship.containsKey(relationshipHashKey2)){
                        RelationshipEntity relationshipEntity = new RelationshipEntity(institutionId1,institutionId2,1L,"cooperate");
                        institutionsRelationship.put(relationshipHashKey1,relationshipEntity);
                    }else if (institutionsRelationship.containsKey(relationshipHashKey1)){
                        institutionsRelationship.get(relationshipHashKey1).setTimes(institutionsRelationship.get(relationshipHashKey1).getTimes()+1L);
                    }else if (institutionsRelationship.containsKey(relationshipHashKey2)){
                        institutionsRelationship.get(relationshipHashKey2).setTimes(institutionsRelationship.get(relationshipHashKey2).getTimes()+1L);
                    }
                }
            }
        }

        /*构建作者和机构的关系*/
        for (String institutionName : enterpriseAndExpert.keySet()){
            for (String authorName : enterpriseAndExpert.get(institutionName)){
                String hashKey = institutionName+authorName;
                if (!unionsRelationship.containsKey(hashKey)){
                    if (institutions.containsKey(institutionName) && authors.containsKey(authorName)){
                        RelationshipEntity relationshipEntity =
                                new RelationshipEntity(institutions.get(institutionName).getId(),authors.get(authorName).getId(),"works_in");
                        unionsRelationship.put(hashKey,relationshipEntity);
                    }else if (institutions.containsKey(institutionName) && !authors.containsKey(authorName)){
                        AuthorEntity authorEntity = new AuthorEntity(authorName,nodeId);
                        ++nodeId;
                        authors.put(authorName,authorEntity);
                        authorTimes.put(authorName,1L);
                        RelationshipEntity relationshipEntity =
                                new RelationshipEntity(institutions.get(institutionName).getId(),authors.get(authorName).getId(),"works_in");
                        unionsRelationship.put(hashKey,relationshipEntity);
                    }else if (!institutions.containsKey(institutionName) && authors.containsKey(authorName)){
                        InstitutionEntity institutionEntity = new InstitutionEntity(institutionName,nodeId);
                        ++nodeId;
                        institutions.put(institutionName,institutionEntity);
                        institutionTimes.put(institutionName,1L);
                        RelationshipEntity relationshipEntity =
                                new RelationshipEntity(institutions.get(institutionName).getId(),authors.get(authorName).getId(),"works_in");
                        unionsRelationship.put(hashKey,relationshipEntity);
                    }else if (!institutions.containsKey(institutionName) && !authors.containsKey(authorName)){
                        AuthorEntity authorEntity = new AuthorEntity(authorName,nodeId);
                        ++nodeId;
                        authors.put(authorName,authorEntity);
                        authorTimes.put(authorName,1L);
                        InstitutionEntity institutionEntity = new InstitutionEntity(institutionName,nodeId);
                        ++nodeId;
                        institutions.put(institutionName,institutionEntity);
                        institutionTimes.put(institutionName,1L);
                        RelationshipEntity relationshipEntity =
                                new RelationshipEntity(institutions.get(institutionName).getId(),authors.get(authorName).getId(),"works_in");
                        unionsRelationship.put(hashKey,relationshipEntity);
                    }
                }
            }
        }


        //写下csv文件
        boolean flagAuthors = writeAuthorsCSV(authors,authorTimes,basePath+File.separator+"py"+File.separator+"model","authors");
        boolean flagAuthorrelationship = writeAuthorsRelationshipCSV(authorsRelationship,basePath+File.separator+"py"+File.separator+"model","authorsRelationship");
        boolean flagInstitutions = writeInstitutionsCSV(institutions,institutionTimes,basePath+File.separator+"py"+File.separator+"model","institutions");
        boolean flagInstitutionrelationship = writeInstitutionsRelationshipCSV(institutionsRelationship,basePath+File.separator+"py"+File.separator+"model","institutionsRelationship");
        boolean flagUnionrelationship = writeUnionRelationshipCSV(unionsRelationship,basePath+File.separator+"py"+File.separator+"model","unionsRelationship");
//        boolean flagAuthors = writeAuthorsCSV(authors,authorTimes,"E:\\tech_analysis_my\\tech_analysis\\py\\model","authors");
//        boolean flagAuthorrelationship = writeAuthorsRelationshipCSV(authorsRelationship,"E:\\tech_analysis_my\\tech_analysis\\py\\model","authorsRelationship");
//        boolean flagInstitutions = writeInstitutionsCSV(institutions,institutionTimes,"E:\\tech_analysis_my\\tech_analysis\\py\\model","institutions");
//        boolean flagInstitutionrelationship = writeInstitutionsRelationshipCSV(institutionsRelationship,"E:\\tech_analysis_my\\tech_analysis\\py\\model","institutionsRelationship");
//        boolean flagUnionrelationship = writeUnionRelationshipCSV(unionsRelationship,"E:\\tech_analysis_my\\tech_analysis\\py\\model","unionsRelationship");
//
//        boolean flagAuthorss = writeAuthorsCSV(authors,authorTimes,"/home/zhzy/Downloads/xcy/tech_analysis","authors");
//        boolean flagAuthorrelationship = writeAuthorsRelationshipCSV(authorsRelationship,"/home/zhzy/Downloads/xcy/tech_analysis","authorsRelationship");
//        boolean flagInstitutions = writeInstitutionsCSV(institutions,institutionTimes,"/home/zhzy/Downloads/xcy/tech_analysis","institutions");
//        boolean flagInstitutionrelationship = writeInstitutionsRelationshipCSV(institutionsRelationship,"/home/zhzy/Downloads/xcy/tech_analysis","institutionsRelationship");
//        boolean flagUnionrelationship = writeUnionRelationshipCSV(unionsRelationship,"/home/zhzy/Downloads/xcy/tech_analysis","unionsRelationship");

        System.out.println("flagAuthors: "+flagAuthors);
        System.out.println("flagAuthorrelationship: "+flagAuthorrelationship);
        System.out.println("flagInstitutions: "+flagInstitutions);
        System.out.println("flagInstitutionrelationship: "+flagInstitutionrelationship);
        System.out.println("flagUnionrelationship: "+flagUnionrelationship);

    }



    private boolean writeAuthorsCSV(HashMap<String, AuthorEntity> authors, HashMap<String,Long> authorsTimes,String filePath, String fileName) {
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
            write.write("l:label\tid:string:AuthorsId\tname\ttimes\n");
            for (String author : authors.keySet()){
//                if (keywords.get(str).getName().length() < 3)
//                    continue;
                String temp = "Authors\t"+authors.get(author).getId()+"\t"+author+"\t"+authorsTimes.get(author);
                write.write(temp);
                write.write('\n');
                ++countline;
            }
            write.flush();
            write.close();
            System.out.println("作者合作关系CSV写入成功！");
        } catch (IOException e) {
            flag = false;
            System.out.println("作者合作关系CSV写入失败 !");
            e.printStackTrace();
        }
        System.out.println(countline);
        // 返回是否成功的标记
        return flag;
    }


    private boolean writeInstitutionsCSV(HashMap<String, InstitutionEntity> institutions, HashMap<String,Long> institutionsTimes,String filePath, String fileName) {
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
            write.write("l:label\tid:string:InstitutionId\tname\ttimes\n");
            for (String insitution : institutions.keySet()){
//                if (keywords.get(str).getName().length() < 3)
//                    continue;
                String temp = "Institution\t"+institutions.get(insitution).getId()+"\t"+insitution+"\t"+institutionsTimes.get(insitution);
                write.write(temp);
                write.write('\n');
                ++countline;
            }
            write.flush();
            write.close();
            System.out.println("作者合作关系CSV写入成功！");
        } catch (IOException e) {
            flag = false;
            System.out.println("作者合作关系CSV写入失败 !");
            e.printStackTrace();
        }
        System.out.println(countline);
        // 返回是否成功的标记
        return flag;
    }



    private boolean writeAuthorsRelationshipCSV(HashMap<String, RelationshipEntity> authorRelationships,
                                             String filePath, String fileName){
        // 标记文件生成是否成功
        boolean flag = true;

        //文件总行数
        long countAuthor = 0;

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
//            write.write("id:string:yearKeywordId\tid:string:yearKeywordId\ttype\ttimes\n");
            write.write("id:string:AuthorsId\tid:string:AuthorsId\ttype\ttimes\n");
            for (String authorHashKey : authorRelationships.keySet()){
                String temp = authorRelationships.get(authorHashKey).getSource()+"\t"+
                        authorRelationships.get(authorHashKey).getTarget()+"\t"+
                        authorRelationships.get(authorHashKey).getStringType()+"\t"+
                        authorRelationships.get(authorHashKey).getTimes()+'\n';
                write.write(temp);
                ++countAuthor;
            }
            write.flush();
            write.close();
            System.out.println("作者合著关系关系CSV写入成功！");
        }catch (IOException e){
            flag = false;
            System.out.println("作者合著关系关系CSV写入失败！");
            e.printStackTrace();
        }
        System.out.println(countAuthor);
        // 返回是否成功的标记
        return flag;
    }

    private boolean writeInstitutionsRelationshipCSV(HashMap<String, RelationshipEntity> institutionsRelationships,
                                                String filePath, String fileName){
        // 标记文件生成是否成功
        boolean flag = true;

        //文件总行数
        long countInstitution = 0;

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
//            write.write("id:string:yearKeywordId\tid:string:yearKeywordId\ttype\ttimes\n");
            write.write("id:string:InstitutionId\tid:string:InstitutionId\ttype\ttimes\n");
            for (String institutionHashKey : institutionsRelationships.keySet()){
                String temp = institutionsRelationships.get(institutionHashKey).getSource()+"\t"+
                        institutionsRelationships.get(institutionHashKey).getTarget()+"\t"+
                        institutionsRelationships.get(institutionHashKey).getStringType()+"\t"+
                        institutionsRelationships.get(institutionHashKey).getTimes()+'\n';
                write.write(temp);
                ++countInstitution;
            }
            write.flush();
            write.close();
            System.out.println("机构合作关系关系CSV写入成功！");
        }catch (IOException e){
            flag = false;
            System.out.println("机构合作关系关系CSV写入失败！");
            e.printStackTrace();
        }
        System.out.println(countInstitution);
        // 返回是否成功的标记
        return flag;
    }


    private boolean writeUnionRelationshipCSV(HashMap<String, RelationshipEntity> unionRelationships,
                                                     String filePath, String fileName){
        // 标记文件生成是否成功
        boolean flag = true;

        //文件总行数
        long countInstitution = 0;

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
//            write.write("id:string:yearKeywordId\tid:string:yearKeywordId\ttype\ttimes\n");
            write.write("id:string:AuthorsId\tid:string:InstitutionId\ttype\ttimes\n");
            for (String hashKey : unionRelationships.keySet()){
                String temp = unionRelationships.get(hashKey).getSource()+"\t"+
                        unionRelationships.get(hashKey).getTarget()+"\t"+
                        unionRelationships.get(hashKey).getStringType()+"\t"+
                        1L+'\n';
                write.write(temp);
                ++countInstitution;
            }
            write.flush();
            write.close();
            System.out.println("专家与企业关系关系CSV写入成功！");
        }catch (IOException e){
            flag = false;
            System.out.println("专家与企业关系关系CSV写入失败！");
            e.printStackTrace();
        }
        System.out.println(countInstitution);
        // 返回是否成功的标记
        return flag;
    }



    public static void main(String[] args){
        new BuildAuthorAndInstitution().getAuthorAndInstitutionCSV();
    }
}
