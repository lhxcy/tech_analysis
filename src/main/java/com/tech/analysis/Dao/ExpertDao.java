package com.tech.analysis.Dao;

import com.tech.analysis.entity.Expert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/18 0018.
 */
@Repository
public class ExpertDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<String> getIdList(){
        String sql = "select id from Expert";
        List<String> idList = jdbcTemplate.query(sql, new RowMapper<String>(){
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                String id = rs.getString("id");
                return id;
            }
        });
        return idList;
    }
    public List<String> getKeywordByExpertId(String table,String expertId){
        String sql = "";
        if(table.equals("paper"))
            sql = String.format("select keywords from paper where uid in(select a.uid from Expert e  inner join Author a on e.id = a.expertid where e.id = %s)", expertId);
        else if(table.equals("patent"))
            sql = String.format("select keywords from patent where id in (select p.patentid from expert e inner join patent2expert p on e.id = p.expertid where e.id = %s)",expertId);
        else if(table.equals("project"))
            sql = String.format("select keywords from project where id in (select p.projectid from expert e inner join project2expert p on e.id = p.expertid where e.id = %s)",expertId);
        List<String> keywordsList = jdbcTemplate.query(sql, new RowMapper<String>(){
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                String keywords = rs.getString("keywords");
                return keywords;
            }
        });
        return keywordsList;
    }

    public void putKeywordByExpertId(String expertId,String keywords){
        jdbcTemplate.update(String.format("update Expert set keywords = '%s' where id = '%s'",keywords,expertId));
    }

    public List<String> insertByEnterpriseNameAndName(String enterpriseName,List<String> nameList){
        List<String> expertIdList = new ArrayList<>();
//            String sql = "insert into expert (name,enterprisename,isdeath) values";
//            for (String name : nameList) {
//                sql += String.format("('%s','%s',0),",name,enterpriseName);
//            }
//            sql = sql.substring(0,sql.length()-1);
//            jdbcTemplate.update(sql);
        for (String name : nameList) {
            String sql = String.format("insert into expert (name,enterprisename,isdeath) values ('%s','%s',0)",name,enterpriseName);

            try{
                jdbcTemplate.update(sql);
                expertIdList.add(jdbcTemplate.queryForObject(String.format("select id from expert where name = '%s' and enterprisename = '%s'",name,enterpriseName),String.class));
            }catch(Exception e){

            }finally {

            }
        }
        return expertIdList;
    }

    public String getExpertIdByNameAndEnterpriseName(String name, String enterpriseName){
        String sql = String.format("Select id from Expert where name = '%s' and enterpriseName = '%s'",name,enterpriseName);
        return (String) jdbcTemplate.queryForObject(sql,String.class);
    }

    public List<Expert> getExpertList(){
        String sql = "select distinct name,enterprisename,b.enterpriseid from Expert a inner join Expert2Enterprise b on a.id = b.expertid";
        List<Expert> list = new ArrayList<>();
        list = jdbcTemplate.query(sql, new RowMapper<Expert>() {
            @Override
            public Expert mapRow(ResultSet rs, int i) throws SQLException {
                Expert e = new Expert();
                e.setName(rs.getString("name"));
                e.setEnterpriseName(rs.getString("enterprisename"));
                e.setEnterpriseId(rs.getString("enterpriseid"));
                return e;
            }
        });
        return list;
    }

    /**
     * @param newExpertList 将新的论文中的作者插入专家，并对到expert2enterprise中
     */
    public void insertNewExpert(List<Expert> newExpertList) {

        int preId = jdbcTemplate.queryForObject("select IDENT_CURRENT('expert')", Integer.class);
        String sql = "insert into Expert (name,isdeath,enterprisename,enterpriseid) values";
        int time = 0;
        for (Expert e : newExpertList) {
            if (time < 999) {
                time++;
                sql += String.format("('%s',0,'%s','%s'),", e.getName().replace("'", "''"), e.getEnterpriseName().replace("'", "''"), e.getEnterpriseId());
            } else {
                sql += String.format("('%s',0,'%s','%s'),", e.getName().replace("'", "''"), e.getEnterpriseName().replace("'", "''"), e.getEnterpriseId());
                sql = sql.substring(0, sql.length() - 1);
                try {//为专家表添加唯一索引，防止插入重复专家
                    jdbcTemplate.update(sql);
                } catch (Exception exc) {
                    System.out.println(exc);
                }
                time = 0;
                sql = "insert into Expert (name,isdeath,enterprisename,enterpriseid) values";
            }
//            try{//为专家表添加唯一索引，防止插入重复专家
//                    jdbcTemplate.update(sql+String.format("('%s',0,'%s')",e.getName().replace("'","''"),e.getEnterpriseName().replace("'","''")));
//            }catch(Exception exc){
//                    System.out.println(exc);
//            }

        }
        if (time > 0) {
            sql = sql.substring(0, sql.length() - 1);
            try {//为专家表添加唯一索引，防止插入重复专家
                jdbcTemplate.update(sql);
            } catch (Exception exc) {
                System.out.println(exc);
            }
        }
        //对expert表去重
        sql = "delete from expert where id not in (SELECT MAX (id) from expert group by name,enterprisename)";
        jdbcTemplate.update(sql);
        sql = String.format("insert into Expert2Enterprise (expertid,enterpriseid) (select id,enterpriseid from expert where id > %d)", preId);
        jdbcTemplate.update(sql);
        sql = "delete from Expert2Enterprise where id not in (SELECT MAX (id) from Expert2Enterprise group by expertid,enterpriseid)";
        jdbcTemplate.update(sql);
    }
//    public void insertNewExpert(List<Expert> newExpertList){
//        String sql = "insert into Expert (name,isdeath,enterprisename) values";
//        int time = 0;
//        for (Expert e : newExpertList) {
////            if(time < 999){
////                time++;
////                sql +=  String.format("('%s',0,'%s'),",e.getName().replace("'","''"),e.getEnterpriseName().replace("'","''"));
////            }else{
////                sql +=  String.format("('%s',0,'%s'),",e.getName().replace("'","''"),e.getEnterpriseName().replace("'","''"));
////                sql = sql.substring(0,sql.length()-1);
////                try{//为专家表添加唯一索引，防止插入重复专家
////                    jdbcTemplate.update(sql);
////                }catch(Exception exc){
////                    System.out.println(exc);
////                }
////                time = 0;
////                sql = "insert into Expert (name,isdeath,enterprisename) values";
////            }
//            try{//为专家表添加唯一索引，防止插入重复专家
//                    jdbcTemplate.update(sql+String.format("('%s',0,'%s')",e.getName().replace("'","''"),e.getEnterpriseName().replace("'","''")));
//            }catch(Exception exc){
//                    System.out.println(exc);
//            }
//
//        }
//        if(time > 0){
//            sql = sql.substring(0,sql.length()-1);
//            jdbcTemplate.update(sql);
//        }
//        for (Expert e : newExpertList) {
//            sql = String.format("select id from Expert where name = '%s' and enterprisename = '%s'",e.getName().replace("'","''"),e.getEnterpriseName().replace("'","''"));
//            //可能出现重复的，就报错了
//            try{
//                String id = jdbcTemplate.queryForObject(sql,String.class);
//                sql = String.format("insert into expert2enterprise (expertid,enterpriseid) values ('%s','%s')",id,e.getEnterpriseId());
//                jdbcTemplate.update(sql);
//            }catch(Exception ex){
//                System.out.println("+++++++++++++++++++++插入错误专家+++++++++++++++++++++++");
//                System.out.println(e.toString());
//            }
//
//        }
//    }

    public void  updateKeyowrdsForExpert(Map<String,String> expertid2Keyword){
        try{//防止因某次中途关闭程序，导致没有删除表，导致表存在异常
            jdbcTemplate.update("CREATE table expertid2keyword(id int identity (1,1),expertid int not null,keywords nvarchar(MAX) NOT NULL)");
        }catch(Exception e){}
        String sql="insert into expertid2keyword (expertid,keywords)" +
                " values";
        int time = 0;
        for (Map.Entry<String,String> entry : expertid2Keyword.entrySet()) {
            if(entry.getKey()==null)continue;
            if(time<999){
                sql += String.format("('%s','%s'),",entry.getKey(),entry.getValue().replace("'","''"));
                time++;
            }else{
                sql += String.format("('%s','%s'),",entry.getKey(),entry.getValue().replace("'","''"));
                sql = sql.substring(0,sql.length()-1);
                jdbcTemplate.update(sql);
                sql="insert into expertid2keyword (expertid,keywords)" +
                        " values";
                time = 0;
                jdbcTemplate.update("update Expert set expert.keywords = expertid2keyword.keywords from expertid2keyword where expert.id = expertid2keyword.expertid");
                jdbcTemplate.update("delete from expertid2keyword");
            }
        }
        if(time>0){
            sql = sql.substring(0,sql.length()-1);
            jdbcTemplate.update(sql);
            jdbcTemplate.update("update Expert set expert.keywords = expertid2keyword.keywords from expertid2keyword where expert.id = expertid2keyword.expertid");
            jdbcTemplate.update("delete from expertid2keyword");
        }
        try{
            jdbcTemplate.update("drop table expertid2keyword");
        }catch (Exception e){}
    }

}
