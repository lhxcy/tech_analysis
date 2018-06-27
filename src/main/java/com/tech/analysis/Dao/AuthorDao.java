package com.tech.analysis.Dao;

import com.tech.analysis.entity.Expert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by Administrator on 2018/5/9 0009.
 */
@Repository
public class AuthorDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<String> getDisplayNameListByFullAddress(String enterpriseName){
        String sql = String.format("SELECT  display_name from AuthorTemp where full_address like '%s%%'",enterpriseName);
        List<String> displayNameList = jdbcTemplate.query(sql, new RowMapper<String>(){
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString("display_name");
            }
        });
        return displayNameList;
    }

    public void insertFromAuthorTemp(String aliasName){
        String sql = String.format("insert into Author (uid,daisng_id,seq_no,reprint,display_name,full_name,wos_standard,last_name,email_addr,full_address,addr_no,expertid) SELECT  A.uid,A.daisng_id,A.seq_no,A.reprint,A.display_name,A.full_name,A.wos_standard,A.last_name,A.email_addr,A.full_address,A.addr_no,B.id as expertid from AuthorTemp as A inner join expert as B on A.display_name = B.name where B.enterprisename = (select name from enterpriseInfo where id = (select companyid from CompanyAlias where aliasname = '%s')) and A.full_address like '%%%s%%' ",aliasName,aliasName);
        try{
            jdbcTemplate.update(sql);
        }catch(Exception e){

        }finally{

        }
    }
    public void deleteFormAuthorTemp(String aliasName){
        String sql = String.format("delete from AuthorTemp where full_address = '%s'",aliasName);
        jdbcTemplate.update(sql);
    }

    /**
     * @return 对authorTemp 和  Address 联合查找，找出机构能被对上的作者的信息
     */
    public List<Expert> getAuthorForNewExpertList(){
        String sql = "SELECT  display_name,companyid,organization\n" +
                "  FROM AuthorTemp a inner join Address b on a.uid = b.uid and a.full_address = b.full_address";
//        String sql = "SELECT  display_name,companyid,organization\n" +
//                "  FROM AuthorTemp a inner join Address b on a.uid = b.uid and a.addr_no = b.addr_no";
        List<Expert> list = new LinkedList<>();
        jdbcTemplate.query(sql, new RowMapper<Integer>() {
            @Override
            public Integer mapRow(ResultSet rs, int i) throws SQLException {
                Expert e = new Expert();
                e.setName(rs.getString("display_name"));
                e.setEnterpriseName(rs.getString("organization"));
                e.setEnterpriseId(rs.getString("companyid"));
                list.add(e);
                return 1;
            }
        });
        return list;
    }

    /**
     * 将temp中能对出的数据插入Author中，并将Temp中数据删除
     */
    public void updateAuthorAndDeleteInTemp(){
        String sql = "insert into Author (uid,daisng_id,seq_no,reprint,display_name,full_name,wos_standard,last_name,email_addr,full_address,addr_no,expertid)  (select distinct d.uid,daisng_id,seq_no,reprint,display_name,full_name,wos_standard,last_name,email_addr,d.full_address,d.addr_no,c.id as expertid from Expert c inner join (SELECT a.uid,daisng_id,seq_no,reprint,display_name,full_name,wos_standard,last_name,email_addr,a.full_address,a.addr_no,organization\n" +
                "  FROM AuthorTemp a inner join Address b on a.uid = b.uid and a.full_address = b.full_address) d on c.name = d.display_name and c.enterprisename = d.organization)\n" +
                "\n" +
                " delete from AuthorTemp where id in (select a.id from AuthorTemp a inner join Author b on a.uid = b.uid and a.display_name = b.display_name and a.full_address = b.full_address)";
//        String sql = "insert into Author (uid,daisng_id,seq_no,reprint,display_name,full_name,wos_standard,last_name,email_addr,full_address,addr_no,expertid)  (select distinct d.uid,daisng_id,seq_no,reprint,display_name,full_name,wos_standard,last_name,email_addr,d.full_address,d.addr_no,c.id as expertid from Expert c inner join (SELECT a.uid,daisng_id,seq_no,reprint,display_name,full_name,wos_standard,last_name,email_addr,a.full_address,a.addr_no,organization\n" +
//                "  FROM AuthorTemp a inner join Address b on a.uid = b.uid and a.addr_no = b.addr_no) d on c.name = d.display_name and c.enterprisename = d.organization)\n" +
//                "\n" +
//                " delete from AuthorTemp where id in (select a.id from AuthorTemp a inner join Author b on a.uid = b.uid and a.display_name = b.display_name and a.addr_no = b.addr_no)";
        try {
            jdbcTemplate.update(sql);
        } catch (Exception exc) {
            System.out.println(exc);
        }
    }

    public Map<String,List<String>> getExpertid2Uids(){
        String sql = "select uid , expertid from author";
        Map<String,List<String>> expertid2Uids = new HashMap<>();
        jdbcTemplate.query(sql, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet resultSet, int i) throws SQLException {
                String uid = resultSet.getString("uid");
                String expertid = resultSet.getString("expertid");
                if(expertid2Uids.get(expertid) == null){
                    LinkedList<String> uidList = new LinkedList<>();
                    expertid2Uids.put(expertid,uidList);
                }
                expertid2Uids.get(expertid).add(uid);
                return "null";
            }
        });
        return  expertid2Uids;
    }
}
