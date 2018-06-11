package com.tech.analysis.Dao;

import org.json.JSONArray;
import org.json.JSONObject;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.StatementResult;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.neo4j.driver.v1.Values.parameters;

/**
 * 按年份生成图
 * Created by XCY on 2018/3/27.
 */
@Repository
public class CreatGraphAboutYear {
    /**
     * 返回给定年份的社区列表
     * @return
     */
//    public List<String> creatAll(){
    public String creatAll(){
//        List<String> ans = new ArrayList<>();
        JSONArray jsonArray = new JSONArray();
        int[] years = {2005,2008,2010,2012,2014,2016};
//        int[] years = {2011};
//        KeywordsDao keywordsD ao = new KeywordsDao();
        for (int year : years){
            HashMap<String,Long> data = creat(year);
//            String tempCommunity =keywordsDao.formatJsonString(keywordsDao.getCommunity(data));
            JSONObject tempCommunity =formatJsonString(data);
//            ans.add(tempCommunity);
            jsonArray.put(tempCommunity);
        }
//        HashMap<String,ArrayList<Integer>> data = keywordsDao.getData("");

//        String str =keywordsDao.formatJsonString(keywordsDao.getCommunity(data));
//        return ans;
        return jsonArray.toString();
    }


    /**
     * 创造出给定年份的图
     * @param year
     * @return
     */
//    public HashMap<Integer,HashMap<Integer,HashMap<String,Integer>>> getCommubityData(List<String> list,boolean flag){
    public  HashMap<String,Long> creat(int year){
        ConnectAndOperNeo4j connect = new ConnectAndOperNeo4j();
        HashMap<String,Long> year_list = new HashMap<>();
        StatementResult result;
        int count = 0;
        int good = 0;
        int bad = 0;
        result = connect.excute("MATCH (p:yearNewKeywordKey{year:\""+year+"\"}) " +
                            "RETURN p.name as p_name,p.times as p_times limit 300",
                    parameters( "", "" ));

            while ( result.hasNext() )
            {
                ++count;
                Record record = result.next();
                try {

                    String temp_name = record.get("p_name").asString();
                    String year_name = temp_name.substring(0,temp_name.length()-4);
                    long year_times = Integer.parseInt(record.get("p_times").asString());
                    if (year_times < 5) {
                        ++bad;
                        continue;
                    }
                    year_list.put(year_name,year_times);
                    ++good;
                }catch (Exception e){
                    e.printStackTrace();
                    continue;
                }
            }

            System.out.println("总数： "+count);
            System.out.println("bad总数： "+bad);
            System.out.println("good总数： "+good);

//        }

        connect.closeConnect();
        return year_list;

    }



    /**
     * 传入（社区id为键,该社区成员的成员社区为键，对应的字符串为值）返回jsonString
     * @param community HashMap<String,Integer> community
     *                  （社区name    次数）
     * @return jsonString
     */
    public JSONObject formatJsonString(HashMap<String,Long> community){
        JSONObject obj = new JSONObject();
        JSONArray objArray = new JSONArray();
        obj.put("name","community");
        for (String name : community.keySet()){
            JSONObject obj1 = new JSONObject();
            obj1.put("name",name);
            obj1.put("size",community.get(name));
            objArray.put(obj1);
        }
        obj.put("children",objArray);
        return obj;
    }



//    /**
//     * 创造出给定年份的图
//     * @param year
//     * @return
//     * MATCH (n:yearNewKeyword) WHERE n.year = "2011" return  n.name,n.partitionKey,n.partitionKey1
//     */
//    public HashMap<String,ArrayList<Integer>> creat(int year){
//        ConnectAndOperNeo4j connect = new ConnectAndOperNeo4j();
//        HashMap<String,ArrayList<Integer>> data = new HashMap<String,ArrayList<Integer>>();
//        StatementResult result = connect.excute(
//                "MATCH (n:yearNewKeyword) WHERE n.year = \""+year+"\" RETURN n.name AS name,n.year AS year," +
//                        "n.partitionKey AS partitionKey,n.partitionKey1 AS partitionKey1",
//                parameters( "", "" ));//获取结果集
////        StatementResult result = connect.excute(
////                "MATCH (n:yearNewKeyword) WHERE n.year = \""+ year +" \" RETURN n.name AS name,n.year AS year," +
////                        "n.partitionKey AS partitionKey,n.partitionKey1 AS partitionKey1",
////                parameters( "", "" ));//获取结果集
////        StatementResult result = connect.excute(
////                "MATCH (n:yearKeyNode) WHERE n.year = "+ year +" RETURN n.name AS name,n.year AS year," +
////                        "n.Community AS Community,n.Community1 AS Community1",
////                parameters( "", "" ));//获取结果集
//
//        int count = 0;
//        int good = 0;
//        int bad = 0;
//        while ( result.hasNext() )
//        {
//            ++count;
//            Record record = result.next();
//            try {
//                ArrayList<Integer> templist = new ArrayList<Integer>();
////                System.out.println(record.get("partitionKey").asInt());
////                System.out.println(record.get("partitionKey1").asInt());
////                System.out.println(record.get( "name" ).asString());
//                templist.add(record.get("partitionKey").asInt());
//                templist.add(record.get("partitionKey1").asInt());
//                data.put(record.get( "name" ).asString(),templist);
//                ++good;
//            }catch (Exception e){
//                ++bad;
//                continue;
//            }
//        }
//        connect.closeConnect();
//        System.out.println("year: "+ year + "总数： "+count);
//        System.out.println("year: "+ year + "bad总数： "+bad);
//        System.out.println("year: "+ year + "good总数： "+good);
//        return data;
//    }
}
