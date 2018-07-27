//package com.tech.analysis.Dao;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.RowMapper;
//import org.springframework.stereotype.Repository;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.LinkedList;
//import java.util.List;
//
///**
// * Created by zhzy on 18-7-27.
// */
//@Repository
//public class GetFilterWords {
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
//    public static List<String> getFilterWordsFromSQL(){
//        List<String> filterWords = new LinkedList<>();
//        System.out.println("start get getFilterWords data from sql");
//        String sql ="select stopword from stopwords";
//        List<String> sqlDataList = jdbcTemplate.query(sql, new RowMapper<String>(){
//            @Override
//            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
//                String stopword =  rs.getString("stopword");
//                if (stopword == null || "".equals(stopword))
//                    return "";
//                //System.out.println(name.trim()+","+code.trim()+","+fathercode.trim());
//                return stopword.trim().replace("\n","");
//            }
//        });
//        for (String string : sqlDataList){
//            if ("".equals(string) || string == null)
//                continue;
//            filterWords.add(string);
//        }
//        return filterWords;
//    }
//}
