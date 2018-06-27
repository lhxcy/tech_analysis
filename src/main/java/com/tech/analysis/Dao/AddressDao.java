package com.tech.analysis.Dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.xml.ws.Action;

/**
 * Created by Administrator on 2018/5/17 0017.
 */
@Repository
public class AddressDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * AddressTemp能和companyAlias对的都对上，对上后删除
     */
    public void preMatchAddress(){
        String sql = "insert into Address (uid,companyid,addr_no,organization,suborganization,full_address,city,country,zip) (SELECT distinct a.uid,b.companyid,a.addr_no,a.organization,a.suborganization,a.full_address,a.city,a.country,a.zip\n" +
                "  FROM AddressTemp a inner join CompanyAlias b on a.organization = b.aliasname)\n" +
                "\n" +
                "delete from AddressTemp where id in(select a.id FROM AddressTemp a inner join CompanyAlias b on a.organization = b.aliasname)";
        try {
            jdbcTemplate.update(sql);
        } catch (Exception exc) {
            System.out.println(exc);
        }
    }

    /**
     * 在人工对齐之后，将AddressTemp中的未知企业插入Enterprise中作为新的标准企业
     */
    public void insertNewEnterpriseFromAddressTemp(){
        String sql = "insert into EnterpriseInfo(name,enterprisetype) SELECT distinct organization,1\n" +
                "  FROM AddressTemp";
        try{
            jdbcTemplate.update(sql);
        }catch(Exception e){
            System.out.println("++++++++++++Paper 插入专家失败++++++++++++++++");
        }
    }
}
