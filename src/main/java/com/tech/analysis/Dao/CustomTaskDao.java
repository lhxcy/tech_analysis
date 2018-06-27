package com.tech.analysis.Dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhzy on 18-6-1.
 */
@Repository
public class CustomTaskDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void update(String id){
        String sql = String.format("update customtask set status = 1 , status_neo4j_data = 1 where id = '%s'",id);
        jdbcTemplate.update(sql);
    }

    public void updateAfterSgc(String id){
        String sql = String.format("update customtask set status_deal_data = 1 where id = '%s'",id);
        jdbcTemplate.update(sql);
    }

    public Boolean check(String id){
        String sql = String.format("select status_spider,status_import_data from customtask where id = '%s'",id);
        List<String> list = jdbcTemplate.query(sql, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet resultSet, int i) throws SQLException {
                String res = "";
                res += resultSet.getString("status_spider");
                res += ",";
                res += resultSet.getString("status_import_data");
                return res;
            }
        });
        if(list==null||list.size()==0)return false;
        String res = list.get(0);
        String[] r = res.split(",");
        if(r[0].equals("1")&&r[1].equals("1"))return true;
        return false;
    }

}
