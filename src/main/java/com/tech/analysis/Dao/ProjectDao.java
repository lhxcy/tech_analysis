package com.tech.analysis.Dao;

import com.hankcs.hanlp.HanLP;
import com.tech.analysis.entity.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Administrator on 2018/4/17 0017.
 */
@Repository
public class ProjectDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Logger logger = LoggerFactory.getLogger("sgc");

    public List<String> getidList(){
        String sql = "select id from Project where has_keywords = 0";
        List<String> idList = jdbcTemplate.query(sql, new RowMapper<String>(){
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                String name = rs.getString("id");
                return name;
            }
        });
        return idList;
    }

    public void updateProjectKeywordsByid(String id){
        String sql = String.format("select main_content,abstract_str from Project where id = '%s'", id);

        try{
            List<Text> textList = jdbcTemplate.query(sql, new RowMapper<Text>(){
                @Override
                public Text mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Text text = new Text();
                    text.setContent(rs.getString("main_content"));
                    text.setAbstractStr(rs.getString("abstract_str"));
                    return text;
                }
            });
            Text text = textList.get(0);
            if(text.getContent()==null&&text.getAbstractStr()==null){
                jdbcTemplate.update(String.format("update Project set has_keywords = 10 where id = '%s'",id));
                return;
            }
            String texts = text.getContent() + text.getAbstractStr();
            List<String> phraseList = HanLP.extractPhrase(texts, 8);
            String keywords = "";
            for (String phrase : phraseList) {
                keywords = keywords + phrase + " ";
            }
            keywords = keywords.trim();
            jdbcTemplate.update(String.format("update Project set keywords = '%s',has_keywords = 10 where id = '%s'",keywords,id));
        }catch(Exception e){
            logger.error(e.toString());
            return;
        }finally {

        }
    }
}
