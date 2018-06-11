package com.tech.analysis.Dao;

import com.tech.analysis.entity.Enterprise;
import org.neo4j.cypher.internal.compiler.v3_1.commands.expressions.Null;
import org.neo4j.cypher.internal.frontend.v2_3.perty.recipe.Pretty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by Administrator on 2018/3/21 0021.
 */
@Repository
public class EnterpriseDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public String getEnterpriseNameById(String id) {

        String sql = String.format("SELECT name FROM EnterpriseInfo WHERE id = '%s'",id);
        String name = jdbcTemplate.queryForObject(sql,String.class);
        return name;
    }

    public List<Enterprise> getAllEnterpriseList(){
        String sql = "SELECT name,chuziqiye,zhuceziben,hangye,zhuceriqi,code,type,zuzhixingshi,level,zhucedi,id FROM EnterpriseInfo";
        List<Enterprise> list =  (List<Enterprise>) jdbcTemplate.query(sql, new RowMapper<Enterprise>(){

            @Override
            public Enterprise mapRow(ResultSet rs, int rowNum) throws SQLException {
                Enterprise enterprise = new Enterprise();
                enterprise.setName(rs.getString("name"));
                enterprise.setChuziqiye(rs.getString("chuziqiye"));
                enterprise.setZhuceziben(rs.getString("zhuceziben"));
                enterprise.setHangye(rs.getString("hangye"));
                enterprise.setZhuceriqi(rs.getString("zhuceriqi"));
                enterprise.setCode(rs.getString("code"));
                enterprise.setType(rs.getString("type"));
                enterprise.setZuzhixingshi(rs.getString("zuzhixingshi"));
                enterprise.setLevel(rs.getString("level"));
                enterprise.setZhucedi(rs.getString("zhucedi"));
                enterprise.setId(rs.getString("id"));

                return enterprise;
            }

        });
        return  list;
    }

    public List<String> getAllEnterpriseNameList(){
        List<String> nameList = new ArrayList<>();
        String sql = "select name from EnterpriseInfo";
        //RowMapper<String> rowMapper = new BeanPropertyRowMapper<>(String.class);
        nameList = jdbcTemplate.query(sql, new RowMapper<String>(){
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString("name");
            }
        });
        return nameList;
    }

    /**
     * @param enterprise 根据传来的企业实体信息，去EnterpriseInfo 中获取对应的companyId
     * @return
     */
    public int getCompanyIdByEnterprise(Enterprise enterprise){
        String sql = String.format("select id from EnterpriseInfo where name = '%s'",enterprise.getName());
        //"select id from EnterpriseInfo where name = ?";
        if(enterprise.getChuziqiye()!=null)sql += " and chuziqiye = ?";
        if(enterprise.getZhuceziben()!=null)sql += " and zhuceziben = ?";
        if(enterprise.getHangye()!=null)sql += " and hangye = ?";
        if(enterprise.getZhuceriqi()!=null)sql += " and zhuceriqi = ?";
        if(enterprise.getCode()!=null)sql += " and code = ?";
        if(enterprise.getType()!=null)sql += " and type = ?";
        if(enterprise.getZuzhixingshi()!=null)sql += " and zuzhixingshi = ?";
        if(enterprise.getLevel()!=null)sql += " and level = ?";
        if(enterprise.getZhucedi()!=null)sql += " and zhucedi = ?";

        if(enterprise.getChuziqiye()==null)sql += " and chuziqiye is NULL";
        if(enterprise.getZhuceziben()==null)sql += " and zhuceziben is NULL";
        if(enterprise.getHangye()==null)sql += " and hangye is NULL";
        if(enterprise.getZhuceriqi()==null)sql += " and zhuceriqi is NULL";
        if(enterprise.getCode()==null)sql += " and code is NULL";
        if(enterprise.getType()==null)sql += " and type is NULL";
        if(enterprise.getZuzhixingshi()==null)sql += " and zuzhixingshi is NULL";
        if(enterprise.getLevel()==null)sql += " and level is NULL";
        if(enterprise.getZhucedi()==null)sql += " and zhucedi is NULL";
        //String sql = "select id from EnterpriseInfo where name = ? and chuziqiye = ? and zhuceziben = ? and hangye = ? and zhuceriqi = ? and code = ? and type = ? and zuzhixingshi = ? and level = ? and zhucedi = ?";
        //防止出现有重复记录的情况
        List<Integer> list =  (List<Integer>) jdbcTemplate.query(sql, new RowMapper<Integer>(){
            @Override
            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                int companyId = rs.getInt("id");
                return companyId;
            }
        },enterprise.getSQL());//enterprise.getName(),enterprise.getChuziqiye(),enterprise.getZhuceziben(),enterprise.getHangye(),enterprise.getZhuceriqi(),enterprise.getCode(),enterprise.getType(),enterprise.getZuzhixingshi(),enterprise.getLevel(),enterprise.getZhucedi());
        if(list.size()>0)//否则没查到会报出异常
            return list.get(0);
        return 0;
    }

    public void updateCompanyAlias(int companyId,String aliasName){
        try{
            jdbcTemplate.update(String.format("INSERT INTO CompanyAlias VALUES(%d,'%s')",companyId,aliasName));
        }catch(Exception e){

        }finally{

        }
    }

    public List<String> getAllEnterpriseIdList(){
        String sql = "select id from EnterpriseInfo";
        List<String> idList = jdbcTemplate.query(sql, new RowMapper<String>(){
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                String id = rs.getString("id");
                return id;
            }
        });
        return idList;
    }

    public List<String> getKeywordsByEnterpriseId(String enterpriseId){
        String sql = String.format("SELECT keywords from expert where id in (select p.expertid from Expert2Enterprise p inner join EnterpriseInfo e on e.id = p.enterpriseid where e.id = %s)", enterpriseId);

        List<String> keywordsList = jdbcTemplate.query(sql, new RowMapper<String>(){
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                String keywords = rs.getString("keywords");
                return keywords;
            }
        });
        return keywordsList;
    }

    public void putKeywordByEnterpriseId(String expertId,String keywords){
        jdbcTemplate.update(String.format("update EnterpriseInfo set keywords = '%s' where id = '%s'",keywords,expertId));
    }


    /**
     * @param enterpriseName
     * @param enterpriseNameInTable
     * @return 获取待匹配企业别名和基准企业的同名员工个数
     */
    public int getConWorkerNums(String enterpriseName, String enterpriseNameInTable){
        String sql = String.format("select count(*) from (select display_name from author where uid in (SELECT uid" +
                "  FROM AddressTemp where organization = '%s')) as a inner join" +
                "( SELECT name " +
                "  FROM Expert where id in(SELECT expertid" +
                "  FROM Expert2Enterprise where enterpriseid in (SELECT id" +
                "  FROM EnterpriseInfo where name = '%s'))) as b on a.display_name = b.name",enterpriseName,enterpriseNameInTable);
        List<Integer> res = jdbcTemplate.query(sql, new RowMapper<Integer>() {
            @Override
            public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getInt(1);
            }
        });
        return res.get(0);
    }


    /**
     * @param aliasname
     * @return  根据待匹配企业别名去表中查询是否已经被匹配过
     */
    public List<Enterprise> getEnterpriseByAliasname(String aliasname){
        String sql = String.format("select name,chuziqiye,zhuceziben,hangye,zhuceriqi,code,type,zuzhixingshi,level,zhucedi,id from EnterpriseInfo where id in (SELECT distinct companyid\n" +
                "  FROM CompanyAlias where aliasname = '%s')",aliasname);
        List<Enterprise> list =  (List<Enterprise>) jdbcTemplate.query(sql, new RowMapper<Enterprise>(){

            @Override
            public Enterprise mapRow(ResultSet rs, int rowNum) throws SQLException {
                Enterprise enterprise = new Enterprise();
                enterprise.setName(rs.getString("name"));
                enterprise.setChuziqiye(rs.getString("chuziqiye"));
                enterprise.setZhuceziben(rs.getString("zhuceziben"));
                enterprise.setHangye(rs.getString("hangye"));
                enterprise.setZhuceriqi(rs.getString("zhuceriqi"));
                enterprise.setCode(rs.getString("code"));
                enterprise.setType(rs.getString("type"));
                enterprise.setZuzhixingshi(rs.getString("zuzhixingshi"));
                enterprise.setLevel(rs.getString("level"));
                enterprise.setZhucedi(rs.getString("zhucedi"));
                enterprise.setId(rs.getString("id"));

                return enterprise;
            }

        });
        return list;
    }

    public Map<Enterprise,String> getEnterpriseWorkers(){
        String sql = "SELECT distinct name,chuziqiye,zhuceziben,hangye,zhuceriqi,code,type,zuzhixingshi,level,zhucedi, id,\n" +
                "(SELECT expertname+',' FROM  (select  e.name,chuziqiye,zhuceziben,hangye,zhuceriqi,code,type,zuzhixingshi,level,zhucedi,id,e2n.name as expertname from enterpriseInfo as e inner join (select Expert2Enterprise.enterpriseid,  Expert.name  from Expert2Enterprise inner join Expert on Expert.id = Expert2Enterprise.expertid) as e2n on e.id = e2n.enterpriseid )B\n" +
                "  \n" +
                "  WHERE B.id=A.id\n" +
                "  order by expertname \n" +
                "  FOR XML PATH('')) AS expertname  \n" +
                "FROM (select  e.name,chuziqiye,zhuceziben,hangye,zhuceriqi,code,type,zuzhixingshi,level,zhucedi,id,e2n.name as expertname from enterpriseInfo as e inner join (select Expert2Enterprise.enterpriseid,  Expert.name  from Expert2Enterprise inner join Expert on Expert.id = Expert2Enterprise.expertid) as e2n on e.id = e2n.enterpriseid  \n" +
                ") A";
        Map<Enterprise,String> enterpriseWithExpert = new HashMap<>();
        List<Enterprise> list =  (List<Enterprise>) jdbcTemplate.query(sql, new RowMapper<Enterprise>(){

            @Override
            public Enterprise mapRow(ResultSet rs, int rowNum) throws SQLException {
                Enterprise enterprise = new Enterprise();
                enterprise.setName(rs.getString("name"));
                enterprise.setChuziqiye(rs.getString("chuziqiye"));
                enterprise.setZhuceziben(rs.getString("zhuceziben"));
                enterprise.setHangye(rs.getString("hangye"));
                enterprise.setZhuceriqi(rs.getString("zhuceriqi"));
                enterprise.setCode(rs.getString("code"));
                enterprise.setType(rs.getString("type"));
                enterprise.setZuzhixingshi(rs.getString("zuzhixingshi"));
                enterprise.setLevel(rs.getString("level"));
                enterprise.setZhucedi(rs.getString("zhucedi"));
                enterprise.setId(rs.getString("id"));
                enterpriseWithExpert.put(enterprise,rs.getString("expertname"));
                return enterprise;
            }

        });
        return enterpriseWithExpert;
    }

    public void insertNewEnterprise(String name){
        String sql = String.format("insert into EnterpriseInfo (name,enterprisetype) values ('%s',1)",name);
        jdbcTemplate.update(sql);
    }

    public int getEnterpriseIdByName(String name){
        String sql = String.format("select id from EnterpriseInfo where name = '%s'",name);
        List<Integer> list = jdbcTemplate.query(sql, new RowMapper<Integer>() {
            @Override
            public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
                Integer a = resultSet.getInt("id");
                return a;
            }
        });
        return list.get(0);
    }

    public Map<String,List<String>> getEnterprise2Expert(){
        String sql = "Select enterpriseid , expertid from expert2enterprise";
        Map<String,List<String>> enterprise2Experts = new HashMap<>();
        jdbcTemplate.query(sql, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet resultSet, int i) throws SQLException {
                String enterpriseid= resultSet.getString("enterpriseid");
                String expertid = resultSet.getString("expertid");
                if(enterprise2Experts.get(enterpriseid) == null){
                    LinkedList<String> expertidList = new LinkedList<>();
                    enterprise2Experts.put(expertid,expertidList);
                }
                enterprise2Experts.get(enterpriseid).add(expertid);
                return "null";
            }
        });
        return enterprise2Experts;
    }

    public void updateKeyowrdsForEnterprise(Map<String,String> enterpriseid2Keyword){
        try{//防止因某次中途关闭程序，导致没有删除表，导致表存在异常
            jdbcTemplate.update("CREATE table enterpriseid2keyword(id int identity (1,1),enterpriseid int not null,keywords nvarchar(MAX) NOT NULL)");
        }catch(Exception e){}
        String sql="insert into enterpriseid2keyword(enterpriseid,keywords)" +
                " values";
        int time = 0;
        for (Map.Entry<String,String> entry : enterpriseid2Keyword.entrySet()) {
            if(time<999){
                sql += String.format("('%s','%s'),",entry.getKey(),entry.getValue().replace("'","''"));
                time++;
            }else{
                sql += String.format("('%s','%s'),",entry.getKey(),entry.getValue().replace("'","''"));
                sql = sql.substring(0,sql.length()-1);
                jdbcTemplate.update(sql);
                sql="insert into enterpriseid2keyword(enterpriseid,keywords)" +
                        " values";
                time = 0;
                jdbcTemplate.update("update EnterpriseInfo set EnterpriseInfo.keywords = enterpriseid2keyword.keywords from enterpriseid2keyword where EnterpriseInfo.id = enterpriseid2keyword.enterpriseid");
                jdbcTemplate.update("delete from enterpriseid2keyword");
            }
        }
        if(time>0){
            sql = sql.substring(0,sql.length()-1);
            jdbcTemplate.update(sql);
            jdbcTemplate.update("update EnterpriseInfo set EnterpriseInfo.keywords = enterpriseid2keyword.keywords from enterpriseid2keyword where EnterpriseInfo.id = enterpriseid2keyword.enterpriseid");
            jdbcTemplate.update("delete from enterpriseid2keyword");
        }
        try{
            jdbcTemplate.update("drop table enterpriseid2keyword");
        }catch (Exception e){}
    }
}
