package com.tech.analysis.Dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

/**
 * Created by Administrator on 2018/5/9 0009.
 */
@Repository
public class Expert2EnterpriseDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insertByExpertIdAndEnterpriseId(String enterpriseId, List<String> expertIdList){
//        try{
//            String sql = "insert into expert2enterprise (enterpriseid,expertid) values";
//            for (String expertId : expertIdList) {
//                sql += String.format("('%s','%s'),",enterpriseId,expertId);
//            }
//            sql = sql.substring(0,sql.length()-1);
//            jdbcTemplate.update(sql);
//        }catch (Exception e){
//
//        }finally {
//
//        }
        for (String expertId : expertIdList) {
            try{
                String sql = String.format("insert into expert2enterprise (enterpriseid,expertid) values ('%s','%s')",enterpriseId,expertId);
                jdbcTemplate.update(sql);
            }catch(Exception e){

            }finally {

            }
        }
    }

}
