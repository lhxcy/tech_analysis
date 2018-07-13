package com.tech.analysis.Dao;

import com.tech.analysis.entity.Prize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Administrator on 2018/5/28 0028.
 */
@Repository
public class PrizeDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void getPrizeForMatch(){
        String sql = "select code,authors,id from PrizeTemp";
        List<Prize> prizeList = jdbcTemplate.query(sql, new RowMapper<Prize>() {
            @Override
            public Prize mapRow(ResultSet resultSet, int i) throws SQLException {
                Prize prize = new Prize();
                prize.setCode(resultSet.getString("code"));
                prize.setAuthors(resultSet.getString("authors"));
                prize.setPrizeid(resultSet.getString("id"));
                return prize;
            }
        });

        for (Prize p : prizeList) {
            sql = "insert into PrizeForMatch (prizecode,enterprisename,prizeid) values";
            String authors = p.getAuthors();
            String[] author = authors.split("，");
            for (String a : author) {
                String[] authorAndEnterprise = a.split("\\(");
                try{
                    sql += String.format("('%s','%s','%s')",p.getCode(),authorAndEnterprise[1].substring(0,authorAndEnterprise[1].length()-1),p.getPrizeid());
                    jdbcTemplate.update(sql);
                }catch (Exception e){
                    System.out.println(p.getPrizeid()+" : "+a);
                }

            }
        }

    }

    public void insertNewPrize2Enterprise(){
        String sql  = "insert into Enterprise2Prize (prizeid,prizecode,enterpriseid) select prizeid,prizecode,companyid from PrizeForMatch a inner join CompanyAlias b on a.enterprisename = b.aliasname\n" +
                "delete from prizeTemp where id in (select prizeid from PrizeForMatch a inner join CompanyAlias b on a.enterprisename = b.aliasname)\n" +
                "delete from PrizeForMatch where id in (select a.id from PrizeForMatch a inner join CompanyAlias b on a.enterprisename = b.aliasname)";

        try {
            jdbcTemplate.update(sql);
        } catch (Exception exc) {
            System.out.println(exc);
        }
    }

    public void insertNewEnterpriseFromPrizeForMatch(){
        String sql = "insert into EnterpriseInfo(name,enterprisetype) SELECT distinct enterprisename,1\n" +
                "  FROM prizeForMatch";
        try{
            jdbcTemplate.update(sql);
        }catch(Exception e){
            System.out.println("存在重复机构");
        }
    }

    public void insertNewCompanyAlias(){
        String sql = "insert into CompanyAlias (aliasname,companyid) select distinct enterprisename as aliasname,a.id as companyid from EnterpriseInfo a inner join PrizeForMatch b on a.name = b.enterprisename";

        try {
            jdbcTemplate.update(sql);
        } catch (Exception exc) {
            System.out.println(exc);
        }
        sql = "delete from CompanyAlias where id not in (select min(id) from CompanyAlias group by aliasname)";
        jdbcTemplate.update(sql);
    }

    public void deletePrizeTemp(){
        String sql = "delete from prizetemp";
        jdbcTemplate.update(sql);
    }
}
