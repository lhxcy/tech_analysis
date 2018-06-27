package com.tech.analysis.Dao;

import com.hankcs.hanlp.HanLP;
import com.tech.analysis.entity.AddressTemp;
import com.tech.analysis.entity.Expert;
import com.tech.analysis.entity.PatentIdAndEnterpriseNames;
import com.tech.analysis.entity.UidText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by Administrator on 2018/3/29 0029.
 */
@Repository
public class PatentDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Logger logger = LoggerFactory.getLogger("sgc");
    /**
     * @return 从patent数据表中抽出patentid,enterpriseNames对
     */
    public List<PatentIdAndEnterpriseNames> getpatentIdAndEnterpriseNames(){
        String sql = "select id,enterprisename from patent";
        List<PatentIdAndEnterpriseNames> list = jdbcTemplate.query(sql, new RowMapper<PatentIdAndEnterpriseNames>(){
            @Override
            public PatentIdAndEnterpriseNames mapRow(ResultSet rs, int rowNum) throws SQLException {
                PatentIdAndEnterpriseNames patentIdAndEnterpriseNames = new PatentIdAndEnterpriseNames();
                patentIdAndEnterpriseNames.setPatentId(rs.getString("id"));
                String enterpriseName =  rs.getString("enterprisename");
                String[] names = enterpriseName.split("\\|");
                for(int i = 0;i < names.length;i++){
                    names[i] = names[i].trim();
                }
                patentIdAndEnterpriseNames.setEnterpriseNames(names);
                return patentIdAndEnterpriseNames;
            }
        });
        return list;
    }

    public List<String> getEnterpriseIdListByPatentId(String patentId){
        String sql = "select enterpriseid from Patent2Enterprise where patentid = ?";
        List<String> list = jdbcTemplate.query(sql, new RowMapper<String>(){
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                String enterpriseId = rs.getString("enterpriseid");
                return enterpriseId;
            }
        },patentId);
        return list;
    }

    public void putPatentToPatentForMatchTable(String patentId,String enterpriseName){
        jdbcTemplate.update(String.format("INSERT INTO patentForMatch VALUES('%s','%s')",patentId,enterpriseName));
        //备份这个表，在撤销操作时方便操作
        jdbcTemplate.update(String.format("INSERT INTO patentForMatchBackup VALUES('%s','%s')",patentId,enterpriseName));
    }

    public List<String> getPatentIdListByEnterpriseName(String table, String enterpriseName){
        String sql = String.format("select patentid from %s where enterpriseName = '%s'",table,enterpriseName);
        List<String> idList = new ArrayList<>();
        idList = jdbcTemplate.query(sql, new RowMapper<String>(){
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                String id = rs.getString("patentid");
                return id;
            }
        });
        return idList;
    }

    public void updatePatent2Enterprise(String patentId,int companyId){
        jdbcTemplate.update(String.format("INSERT INTO patent2Enterprise VALUES('%s',%d)",patentId,companyId));
    }

    public void deleteItemByEnterpriseName(String table,String enterpriseName){
        jdbcTemplate.update(String.format("delete from %s where enterpriseName = '%s'",table,enterpriseName));
    }

    /**
     * @param patentId   在回滚专利机构人工选择时调用，将之前匹配后删除的记录重新添加回来
     * @param enterpriseName
     */
    public void updatePatentForMatch(String patentId,String enterpriseName){
        jdbcTemplate.update(String.format("INSERT INTO patentForMatch VALUES('%s','%s')",patentId,enterpriseName));
    }

    public void deleteItemInPatent2Enterprise(String patentId,int companyId){
        jdbcTemplate.update(String.format("delete from patent2enterprise where patentid = '%s' and enterpriseid = %d",patentId,companyId));
    }

    public List<String> getidList(){
        String sql = "select id from Patent where has_keywords = 0";
        List<String> idList = jdbcTemplate.query(sql, new RowMapper<String>(){
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                String name = rs.getString("id");
                return name;
            }
        });
        return idList;
    }

    public void updatePatentKeywordsByid(String id){
        String sql = String.format("select abstract_cn from Patent where id = '%s'", id);

        try{
            List<String> abstractList = jdbcTemplate.query(sql, new RowMapper<String>(){
                @Override
                public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return rs.getString("abstract_cn");
                }
            });
            String text = abstractList.get(0);
            if(text == null){
                jdbcTemplate.update(String.format("update Patent set has_keywords = 10 where id = '%s'",id));
                return;
            }
            List<String> phraseList = HanLP.extractPhrase(text, 8);
            String keywords = "";
            for (String phrase : phraseList) {
                keywords = keywords + phrase + " ";
            }
            keywords = keywords.trim();
            jdbcTemplate.update(String.format("update Patent set keywords = '%s',has_keywords = 10 where id = '%s'",keywords,id));
        }catch(Exception e){
            logger.error(e.toString());
            return;
        }finally {

        }
    }

    public void insertNewPatent2Enterprise(){
        String sql = "insert into Patent2Enterprise(patentid,enterpriseid) select patentid,companyid from patentForMatch a inner join CompanyAlias b on a.enterpriseName = b.aliasname";
        jdbcTemplate.update(sql);
    }

    public List<Expert> getExpertPreMatch(){
        String sql = "select distinct inventors_first as expertname,d.enterpriseName,d.companyid from patent c inner join (select distinct enterpriseName,patentid,companyid from patentForMatch a inner join CompanyAlias b on a.enterpriseName = b.aliasname) d on c.id = d.patentid and  c.enterprisename_first = d.enterpriseName ";
        List<Expert> list = new LinkedList<>();
        jdbcTemplate.query(sql, new RowMapper<Expert>() {
            @Override
            public Expert mapRow(ResultSet rs, int i) throws SQLException {
                Expert e = new Expert();
                e.setName(rs.getString("expertname"));
                e.setEnterpriseName(rs.getString("enterprisename"));
                e.setEnterpriseId(rs.getString("companyid"));
                list.add(e);
                return e;
            }
        });
        return list;
    }

    public void insertNewPatent2Expert(){
        String sql = "insert into Patent2Expert select patentid,e.id from Expert e inner join (select inventors_first,d.enterpriseName,d.patentid from patent c inner join (select enterpriseName,patentid,companyid from patentForMatch a inner join CompanyAlias b on a.enterpriseName = b.aliasname) d on c.id = d.patentid and  c.enterprisename_first = d.enterpriseName) f on e.name = f.inventors_first and e.enterprisename = f.enterpriseName";
        jdbcTemplate.update(sql);
    }

    public void deleteItemInPatentForMatch(){
        String sql = "delete from patentForMatch where id in (select a.id from patentForMatch a inner join CompanyAlias b on a.enterpriseName = b.aliasname)\n" +
                "delete from patentForMatchBackup where id in (select a.id from patentForMatchBackup a inner join CompanyAlias b on a.enterpriseName = b.aliasname)";
        jdbcTemplate.update(sql);
    }

    public void insertNewEnterpriseFromPatentForMatch(){
        String sql = "insert into EnterpriseInfo(name,enterprisetype) SELECT distinct enterprisename,1\n" +
                "  FROM patentForMatch";
        try{
            jdbcTemplate.update(sql);
        }catch(Exception e){
            System.out.println("++++++++++++Patent 插入专家失败++++++++++++++++");
        }
    }
    public void insertNewCompanyAlias(){
        String sql = "insert into CompanyAlias (aliasname,companyid) select distinct enterprisename,a.id from EnterpriseInfo a inner join PatentForMatch b on a.name = b.enterprisename";
        jdbcTemplate.update(sql);
        sql = "delete from CompanyAlias where id not in (select min(id) from CompanyAlias group by aliasname)";
        jdbcTemplate.update(sql);
    }

    public Map<String,List<String>> getExpertid2Patentids(){
        Map<String,List<String>> expertid2Patentids = new HashMap<>();
        String sql = "select expertid , patentid from patent2expert";
        jdbcTemplate.query(sql, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet resultSet, int i) throws SQLException {
                String patentid = resultSet.getString("patentid");
                String expertid = resultSet.getString("expertid");
                if(expertid2Patentids.get(expertid) == null){
                    LinkedList<String> patentidList = new LinkedList<>();
                    expertid2Patentids.put(expertid,patentidList);
                }
                expertid2Patentids.get(expertid).add(patentid);
                return "null";
            }
        });
        return expertid2Patentids;
    }

    public Map<String,String> getPatentid2Keywords(){
        Map<String,String> patentid2Keywords = new HashMap<>();
        String sql = "select id , keywords from patent";
        jdbcTemplate.query(sql, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet resultSet, int i) throws SQLException {
                patentid2Keywords.put(resultSet.getString("id"),resultSet.getString("keywords"));
                return "null";
            }
        });
        return patentid2Keywords;
    }

    public List<UidText> getForKeywords(){
        String sql = String.format("select TOP 10000 id,abstract_cn from Patent where has_keywords = 0");
        List<UidText> uidTextList = jdbcTemplate.query(sql, new RowMapper<UidText>() {
            @Override
            public UidText mapRow(ResultSet rs, int i) throws SQLException {
                UidText ut = new UidText();
                ut.setUid(rs.getString("id"));
                ut.setText(rs.getString("abstract_cn"));
                return ut;
            }
        });
        return uidTextList;
    }

    public void updateKeyphraseForPatent(){
        try{//防止因某次中途关闭程序，导致没有删除表，导致表存在异常
            jdbcTemplate.update("CREATE table patentTemp1(id int identity (1,1),patentid int not null,keywords nvarchar(MAX) NOT NULL)");
        }catch(Exception e){}
        List<UidText> uidTexts = getForKeywords();
        while(uidTexts.size() > 0){
            System.out.println("+++++++++++++++start for ex+++++++++++++++++");
            int num = 1;
            for(UidText ut : uidTexts){

                if(ut.getText()==null)ut.setText("");
                else{
                    List<String> phraseList = HanLP.extractPhrase(ut.getText(),5);
                    String keywords = "";
                    for (String phrase : phraseList) {
                        keywords = keywords + phrase + " ";
                    }
                    if(ut.getKeywords()!=null){
                        String[] keywordsOfPaper = ut.getKeywords().split(",");
                        for (String keyword : keywordsOfPaper) {
                            keywords = keywords + keyword.trim() + " ";
                        }
                    }

                    keywords = keywords.trim();
                    ut.setText(keywords);
                }
            }
            String sql="insert into patentTemp1 (patentid,keywords)" +
                    " values";
            int time = 0;
            for(UidText ut : uidTexts){
                if(time<999){
                    sql += String.format("('%s','%s'),",ut.getUid(),ut.getText().replace("'","''"));
                    time++;
                }else{
                    sql += String.format("('%s','%s'),",ut.getUid(),ut.getText().replace("'","''"));
                    sql = sql.substring(0,sql.length()-1);
                    jdbcTemplate.update(sql);
                    sql = "insert into patentTemp1 (patentid,keywords)" +
                            " values";
                    time = 0;
                }
            }
            if(time>0){
                sql = sql.substring(0,sql.length()-1);
                jdbcTemplate.update(sql);
            }

            jdbcTemplate.update("update patent set patent.keywords = patentTemp1.keywords, patent.has_keywords = 10 from patentTemp1 where patent.id=patentTemp1.patentid");
            jdbcTemplate.update("delete from patentTemp1");
            uidTexts = getForKeywords();
        }
        try{
            jdbcTemplate.update("drop table patentTemp1");
        }catch (Exception e){}
    }

    public void getPatentForMatchToTable(){
        int id = jdbcTemplate.queryForObject("select patentid from patentasist",Integer.class);

        String sql = String.format("select id,enterprisename from patent where id > %d",id);

        List<PatentIdAndEnterpriseNames> patentIdAndEnterpriseNamesList = jdbcTemplate.query(sql, new RowMapper<PatentIdAndEnterpriseNames>(){
            @Override
            public PatentIdAndEnterpriseNames mapRow(ResultSet rs, int rowNum) throws SQLException {
                PatentIdAndEnterpriseNames patentIdAndEnterpriseNames = new PatentIdAndEnterpriseNames();
                patentIdAndEnterpriseNames.setPatentId(rs.getString("id"));
                String enterpriseName =  rs.getString("enterprisename");
                String[] names = enterpriseName.split("\\|");
                for(int i = 0;i < names.length;i++){
                    names[i] = names[i].trim();
                }
                patentIdAndEnterpriseNames.setEnterpriseNames(names);
                return patentIdAndEnterpriseNames;
            }
        });
        if(patentIdAndEnterpriseNamesList.size()<=0)return;
        int times = 0;
        sql = "insert into patentForMatch (patentid,enterpriseName) values";
        //String sql2 = "insert into patentForMatchBackup (patentid,enterpriseName) values";
        for (PatentIdAndEnterpriseNames patentIdAndEnterpriseNames : patentIdAndEnterpriseNamesList) {
            if(times>=900){
                System.out.println("---------------"+times);
                jdbcTemplate.update(sql.substring(0,sql.length()-1));
                //jdbcTemplate.update(sql2.substring(0,sql2.length()-1));
                sql = "insert into patentForMatch (patentid,enterpriseName) values";
                //sql2 = "insert into patentForMatchBackup (patentid,enterpriseName) values";
                times = 0;
            }
            String patentid = patentIdAndEnterpriseNames.getPatentId();
            for (String name : patentIdAndEnterpriseNames.getEnterpriseNames()) {
                sql += String.format("('%s','%s'),",patentid,name);
                //sql2 += String.format("('%s','%s'),",patentid,name);
                times++;
            }
        }
        if(times>0){
            jdbcTemplate.update(sql.substring(0,sql.length()-1));
            //jdbcTemplate.update(sql2.substring(0,sql2.length()-1));
        }
        //对patentformatch去重
        sql = "delete from patentformatch where id not in (select max(id) from patentForMatch group by patentid,enterpriseName)";
        jdbcTemplate.update(sql);
        sql = "UPDATE patentasist set patentid = (select IDENT_CURRENT('patent'))";
        jdbcTemplate.update(sql);
    }

    public void removeDuplicate(){
        String sql = "delete from Patent2Enterprise where id not in (select Max(id) from Patent2Enterprise group by patentid,enterpriseid)";
        String sql2 = "delete from Patent2Expert where id not in (select Max(id) from Patent2Expert group by patentid,expertid)";
        jdbcTemplate.update(sql);
        jdbcTemplate.update(sql2);
    }
}

