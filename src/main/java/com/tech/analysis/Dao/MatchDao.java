package com.tech.analysis.Dao;

/**
 * Created by Administrator on 2018/3/28 0028.
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Date;

/**
 * 用于操作匹配过程中的操作表,主要操作表CompanyAlias,和MatchRecord
 * */
@Repository
public class MatchDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void updateMatchRecord(int companyId,String aliasName,String source){
        Date now = new Date();
        DateFormat df = DateFormat.getDateInstance();
        String nowTime = df.format(now);
        try{
            jdbcTemplate.update(String.format("insert into MatchRecord values(%d,'%s','%s','%s')",companyId,aliasName,source,nowTime));
        }catch(Exception e){}finally {

        }

    }

    public void deleteItemByCompanyIdAndAliasname(String table,int companyid,String aliasName){
        try{
            jdbcTemplate.update(String.format("delete from %s where companyid = %d and aliasName = '%s'",table,companyid,aliasName));
        }catch(Exception e){}finally {

        }
    }

    public void updateCompanyAlias(int companyId,String aliasName){
        try{
            jdbcTemplate.update(String.format("INSERT INTO CompanyAlias VALUES(%d,'%s')",companyId,aliasName));
        }catch(Exception e){}finally {

        }
    }

    /**
     * 将AddressTemp中的剩余的新机构插入对应CompanyAlias中
     */
    public void insertNewCompanyAlias(){
        try{
            String sql = "insert into CompanyAlias (aliasname,companyid) select distinct name,a.id from EnterpriseInfo a inner join AddressTemp b on a.name = b.organization";
            jdbcTemplate.update(sql);
            sql = "delete from CompanyAlias where id not in (select min(id) from CompanyAlias group by aliasname)";
            jdbcTemplate.update(sql);
        }catch(Exception e){}

    }
}
