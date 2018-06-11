package com.tech.analysis.Dao;

import com.hankcs.hanlp.HanLP;
import com.tech.analysis.entity.AddressTemp;
import com.tech.analysis.entity.UidText;
import org.neo4j.cypher.internal.frontend.v2_3.ast.Add;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/22 0022.
 */
@Repository
public class PaperDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Logger logger = LoggerFactory.getLogger("sgc");

    /**
     * @param name 根据机构名字去AddressTemp表中找到论文对应UID
     * @return 找到该英文机构发表的某篇论文的UID
     */
    public String getUidByName(String name){

        String sql = "select UID from AddressTemp where organization = ?";
        List<String> uidList = jdbcTemplate.query(sql, new RowMapper<String>(){
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString("UID");
            }
        },name);
        return uidList.get(0);
    }

    /**
     * @param uid 根据UID去table中去查找该论文的作者机构
     * @return 返回机构名列表
     */
    public List<String> getNameListByUid(String uid,String table){
        String sql = String.format("SELECT organization from %s where UID = ?", table);
        List<String> nameList = jdbcTemplate.query(sql, new RowMapper<String>(){
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString("organization");
            }
        },uid);
        return nameList;
    }

    /**
     * @param name 根据企业名称获取AddressTemp 实体
     * @return
     */
    public List<AddressTemp> getAddressTempsByName(String name){
        String sql = "SELECT UID,organization,suborganization,full_address,city,country,zip,addr_no FROM AddressTemp where organization = ?";
//        List<AddressTemp> addressTemps = jdbcTemplate.query(sql, new RowMapper<AddressTemp>(){
//            @Override
//            public AddressTemp mapRow(ResultSet rs, int rowNum) throws SQLException {
//                AddressTemp addressTemp = new AddressTemp();
//                addressTemp.setUid(rs.getString("UID"));
//                addressTemp.setOrganization(rs.getString("organization"));
//                addressTemp.setSuborganization(rs.getString("suborganization"));
//                addressTemp.setFull_address(rs.getString("full_address"));
//                addressTemp.setCity(rs.getString("city"));
//                addressTemp.setCountry(rs.getString("country"));
//                addressTemp.setZip(rs.getString("zip"));
//                addressTemp.setAddr_no(rs.getInt("addr_no"));
//                return addressTemp;
//            }
//        },name);
        List<AddressTemp> addressTemps = getAddressTemps(sql,name);
        return addressTemps;
    }

    /**
     * @param companyId  对Address 进行插入
     * @param
     */
    public void updateAddress(int companyId,AddressTemp addressTemp){
        try{
            jdbcTemplate.update("INSERT INTO Address VALUES('"
                    + addressTemp.getUid() + "', '"
                    + companyId + "', '"
                    + addressTemp.getAddr_no() + "', '"
                    + addressTemp.getOrganization() + "', '"
                    + addressTemp.getSuborganization() + "', '"
                    + addressTemp.getFull_address() + "', '"
                    + addressTemp.getCity() + "', '"
                    + addressTemp.getCountry() + "', '"
                    + addressTemp.getZip() + "')");
        }catch(Exception e){}finally {

        }
    }

    /**
     * @param addressTemp 将需要回退的AddressTemp插入到AddressTemp表中
     */
    public void updateAddressTemp(AddressTemp addressTemp){
        jdbcTemplate.update(addressTemp.getInsertSql());
    }

    public void deleteItemInAddressTempByOrganization(String organization){
        jdbcTemplate.update(String.format("delete from AddressTemp where organization = '%s'",organization));
    }

    public List<String> getAddressTempNames(){
        String sql = "select organization from AddressTemp";
        List<String> names = jdbcTemplate.query(sql, new RowMapper<String>(){
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                String name = rs.getString("organization");
                return name;
            }
        });
        return names;
    }

    public List<AddressTemp> getAddressTempsInAddress(int companyId,String aliasName){
        String sql = "SELECT UID,organization,suborganization,full_address,city,country,zip,addr_no FROM Address where companyid = ? and organization = ?";
        List<AddressTemp> addressTemps = getAddressTemps(sql,companyId,aliasName);
        return addressTemps;
    }

    /**
     * @param sql 根据sql语句去获取AddressTemp实体
     * @param args
     * @return
     */
    public List<AddressTemp> getAddressTemps(String sql,Object... args){
        List<AddressTemp> addressTemps = jdbcTemplate.query(sql, new RowMapper<AddressTemp>(){
            @Override
            public AddressTemp mapRow(ResultSet rs, int rowNum) throws SQLException {
                AddressTemp addressTemp = new AddressTemp();
                addressTemp.setUid(rs.getString("UID"));
                addressTemp.setOrganization(rs.getString("organization"));
                addressTemp.setSuborganization(rs.getString("suborganization"));
                addressTemp.setFull_address(rs.getString("full_address"));
                addressTemp.setCity(rs.getString("city"));
                addressTemp.setCountry(rs.getString("country"));
                addressTemp.setZip(rs.getString("zip"));
                addressTemp.setAddr_no(rs.getString("addr_no"));
                return addressTemp;
            }
        },args);
        return addressTemps;
    }

    /**
     * @param table  Address or AddressTemp
     * @param companyid
     * @param aliasName
     */
    public void deleteItemByCompanyIdAndAliasname(String table,int companyid,String aliasName){
        jdbcTemplate.update(String.format("delete from %s where companyid = %d and organization = '%s'",table,companyid,aliasName));
    }

    public List<String> getUidList(){
        String sql = "select uid from Paper where has_keywords = 0";
        List<String> uidList = jdbcTemplate.query(sql, new RowMapper<String>(){
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                String name = rs.getString("uid");
                return name;
            }
        });
        return uidList;
    }

    public void updatePaperKeywordsByUid(String uid){
        String sql = String.format("select abstract_text_cn from Paper where uid = '%s'", uid);

        try{
            List<String> abstractList = jdbcTemplate.query(sql, new RowMapper<String>(){
                @Override
                public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return rs.getString("abstract_text_cn");
                }
            });
            String text = abstractList.get(0);
            if(text == null){
                jdbcTemplate.update(String.format("update Paper set has_keywords = 10 where uid = '%s'",uid));
                return;
            }
            List<String> phraseList = HanLP.extractPhrase(text, 5);
            String keywords = "";
            for (String phrase : phraseList) {
                keywords = keywords + phrase + " ";
            }
            keywords = keywords.trim();
            jdbcTemplate.update(String.format("update Paper set keywords = '%s',has_keywords = 10 where UID = '%s'",keywords,uid));
        }catch(Exception e){
            logger.error(e.toString());
            return;
        }finally {

        }
    }

    public List<UidText> getForKeywords(){
        String sql = String.format("select TOP 10000 uid,abstract_text_cn,keywords_cn from Paper where has_keywords = 0");
        List<UidText> uidTextList = jdbcTemplate.query(sql, new RowMapper<UidText>() {
            @Override
            public UidText mapRow(ResultSet rs, int i) throws SQLException {
                UidText ut = new UidText();
                ut.setUid(rs.getString("uid"));
                ut.setText(rs.getString("abstract_text_cn"));
                ut.setKeywords(rs.getString("keywords_cn"));
                return ut;
            }
        });
        return uidTextList;
    }

    public void updateKeyphraseForPaper(){
        try{//防止因某次中途关闭程序，导致没有删除表，导致表存在异常
            jdbcTemplate.update("CREATE table paperTemp1(id int identity (1,1),uid nvarchar(255) not null,keywords nvarchar(MAX) NOT NULL)");
        }catch(Exception e){}
        List<UidText> uidTexts = getForKeywords();
        while(uidTexts.size() > 0){
            System.out.println(uidTexts.size());
            int num = 1;
            for(UidText ut : uidTexts){
                if((num++)%1000==0)System.out.println("----------------------- "+num+" ----------------------");
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
            String sql="insert into paperTemp1 (uid,keywords)" +
                    " values";
            int time = 0;
            long startTime=System.currentTimeMillis();   //获取开始时间
            for(UidText ut : uidTexts){
//            num--;
//            if(num == 2)break;
                if(time<999){
                    sql += String.format("('%s','%s'),",ut.getUid(),ut.getText().replace("'","''"));
                    time++;
                }else{
                    sql += String.format("('%s','%s'),",ut.getUid(),ut.getText().replace("'","''"));
                    sql = sql.substring(0,sql.length()-1);
                    jdbcTemplate.update(sql);
                    sql = "insert into paperTemp1 (uid,keywords)" +
                            " values";
                    time = 0;
                }
            }
            if(time>0){
                sql = sql.substring(0,sql.length()-1);
                jdbcTemplate.update(sql);
            }

            long endTime=System.currentTimeMillis(); //获取结束时间

            System.out.println("程序运行时间： "+(endTime-startTime)+"ms");

            jdbcTemplate.update("update paper set paper.keywords = paperTemp1.keywords, paper.has_keywords = 10 from paperTemp1 where paper.uid=paperTemp1.uid");
            jdbcTemplate.update("delete from paperTemp1");
            uidTexts = null;
            uidTexts = getForKeywords();
        }
        try{
            jdbcTemplate.update("drop table paperTemp1");
        }catch (Exception e){}
    }


    /**
     * @param organization
     * @return 获取待匹配机构的员工
     */
    public List<String> getWorksByOrgnazationName(String organization){
        String sql = String.format("select display_name from author where uid in (SELECT uid" +
                "  FROM AddressTemp where organization = '%s') and full_address like '%%%s%%'",organization,organization);
        List<String> workers = jdbcTemplate.query(sql, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet rs, int i) throws SQLException {
                return rs.getString("display_name");
            }
        });
        return workers;
    }

    public Map<String,String> getUid2Keywords(){
        Map<String,String> uid2Keywords = new HashMap<>();
        String sql = "select uid , keywords from paper";
        jdbcTemplate.query(sql, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet resultSet, int i) throws SQLException {
                uid2Keywords.put(resultSet.getString("uid"),resultSet.getString("keywords"));
                return "null";
            }
        });
        return uid2Keywords;
    }
}
