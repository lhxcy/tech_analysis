package com.tech.analysis.Dao;

import org.json.JSONArray;
import org.json.JSONObject;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.StatementResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static org.neo4j.driver.v1.Values.parameters;

/**
 * Created by zhzy on 18-5-21.
 */
@Repository
public class GetCommunity {
    @Autowired
    private WordModel wordModel;

    public String getJsonStringCommunity(String query){
        List<String> list = UtilRead.readQuery();
//        boolean flag = false;
        if ("\"\"".equals(query) || "“”".equals(query) || query == null) {
            return formatJsonString(getCommubityData(list));
        }else {
            list.clear();
            list.add(query);
//            list.addAll(wordModel.distance(query));
//            System.out.println(list);
//            return list.toString();
            return formatJsonString(getLocalCommubityData(list));
        }
    }

    /**
     * 返回词云
     * @return
     */
    public String getJsonStringCiYun(){
        List<String> list = UtilRead.readQuery();
        JSONArray jsonArray = new JSONArray();
        HashMap<String, Integer> ciyun = new HashMap<>();
        ConnectAndOperNeo4j connect = new ConnectAndOperNeo4j();
        StatementResult result;

        int bigIndex = 1;
        int sonIndex = 1;
        int count = 0;
        int good = 0;
        int bad = 0;
//        return formatJsonString(getLocalCommubityData(list));
        for (String string : list){

            result = connect.excute("MATCH (p:newKeywordKey{name:\""+string+"\"})-[r:similarKey]-(n:newKeywordKey) " +
                            "RETURN p.name as p_name,p.times as p_times,n.name as n_name,n.times as n_times limit 100",
                    parameters( "", "" ));

            while ( result.hasNext() )
            {
                ++count;
                Record record = result.next();
                try {
                    String Pname = record.get("p_name").asString();
                    int Ptimes = Integer.parseInt(record.get("p_times").asString());
                    String Nname = record.get("n_name").asString();
                    int Ntimes = Integer.parseInt(record.get("n_times").asString());
                    if (Ntimes < 2) {
                        ++bad;
                        continue;
                    }
                    if (!ciyun.containsKey(Pname)) ciyun.put(Pname,Ptimes);
                    if (!ciyun.containsKey(Nname)) ciyun.put(Nname,Ntimes);
                    ++good;
                }catch (Exception e){
                    e.printStackTrace();
                    continue;
                }
            }
        }
        System.out.println("总数： "+count);
        System.out.println("bad总数： "+bad);
        System.out.println("good总数： "+good);

        for(String key : ciyun.keySet()){
            JSONObject obj = new JSONObject();
            obj.put("name",key);
            obj.put("size",ciyun.get(key));
            jsonArray.put(obj);
        }

        return jsonArray.toString();
    }

    public String getJsonArrayForKeywordsAndTimes(){
        HashMap<String,Integer> map = UtilRead.readLocalWordsObject();
        JSONArray jsonArray = new JSONArray();
        int count = 0;
        for (String string : map.keySet()){
            ++count;
            if (count > 100) break;
            JSONObject obj = new JSONObject();
            obj.put("name",string);
            obj.put("size",map.get(string));
            jsonArray.put(obj);
        }
        System.out.println(jsonArray);
        return jsonArray.toString();
    }

    public HashMap<String, Integer> getMaxKeyAndTimes(){
        List<String> list = UtilRead.readQuery();
        System.out.println(list);
        HashMap<String,Integer> keywordstimesMap = new HashMap<>();
        ConnectAndOperNeo4j connect = new ConnectAndOperNeo4j();
        StatementResult result;
        int count = 0;
        for (String string : list){
            ++count;
            if (count > 150) break;
//            MATCH (n:newKeywordKey{name:"人工智能"}) RETURN n.name as name, n.times as times
            result = connect.excute("MATCH (n:newKeywordKey{name:\""+string+"\"}) RETURN n.name as name, n.times as times",
                    parameters( "", "" ));
            while (result.hasNext()){
                Record record = result.next();
                try {
                    int key_times = Integer.parseInt(record.get("times").asString());
                    String key_name = record.get("name").asString();
                    keywordstimesMap.put(key_name,key_times);
                }catch (Exception e){
                    e.printStackTrace();
                    continue;
                }
            }
        }
        System.out.println(keywordstimesMap.size());
        return keywordstimesMap;
    }



    public HashMap<Integer,HashMap<Integer,HashMap<String,Integer>>> getLocalCommubityData(List<String> list){
//        LinkedList<String> locao_words = new LinkedList<>();
        HashMap<String,Integer> locao_words = new HashMap<>();
        HashMap<Integer,HashMap<Integer,HashMap<String,Integer>>> community = new HashMap<Integer,HashMap<Integer,HashMap<String,Integer>>>();
        HashMap<Integer,HashMap<String,Integer>> sonCommunity = new HashMap<Integer,HashMap<String,Integer>>();
        ConnectAndOperNeo4j connect = new ConnectAndOperNeo4j();
        StatementResult result;

        int bigIndex = 1;
        int sonIndex = 1;
        for (String string : list){
            int count = 0;
            int good = 0;
            int bad = 0;
            result = connect.excute("MATCH (p:newKeywordKey{name:\""+string+"\"})-[r:similarKey]-(n:newKeywordKey) " +
                            "RETURN p.name as p_name,p.times as p_times,n.name as n_name,n.times as n_times,r.times as r_times limit 400",
                    parameters( "", "" ));

            HashMap<String,Integer> templist = new HashMap<>();
            while ( result.hasNext() )
            {
                ++count;
                Record record = result.next();
                try {
                    if (templist.get(string) == null){
                        templist.put(record.get("p_name").asString(),Integer.parseInt(record.get("p_times").asString()));
                    }
                    int similar_times = Integer.parseInt(record.get("n_times").asString());
//                    System.out.println("////////////////////////////////////////////");
                    if (similar_times < 2) {
                        ++bad;
                        continue;
                    }
                    int name_times = Integer.parseInt(record.get("n_times").asString());
//                    System.out.println(name_times);
                    String name = record.get("n_name").asString();
                    locao_words.put(name,name_times);
                    templist.put(name,name_times);
//                    templist.put(record.get("n_name").asString(),name_times);
                    ++good;
                }catch (Exception e){
//                    e.printStackTrace();
//                    ++bad;
                    continue;
                }
            }
//            if (flag && templist.size() > 1) {
//                sonCommunity.put(sonIndex++,templist);
//                break;
//            }else {
//                sonCommunity.put(sonIndex++,templist);
//            }
            sonCommunity.put(sonIndex++,templist);
            System.out.println("query： "+string);
            System.out.println("总数： "+count);
            System.out.println("bad总数： "+bad);
            System.out.println("good总数： "+good);

        }

        community.put(bigIndex,sonCommunity);
        connect.closeConnect();
        UtilWrite.WriteLocalWordsObject(locao_words);
        return community;

    }


//    public String communityBasedQuery(String query){
//        HashMap<Integer,HashMap<Integer,HashMap<String,Integer>>> community = new HashMap<Integer,HashMap<Integer,HashMap<String,Integer>>>();
//        ConnectAndOperNeo4j connect = new ConnectAndOperNeo4j();
//        StatementResult result;
//        int bigIndex = 1;
//        int sonIndex = 1;
//        int count = 0;
//        int good = 0;
//        int bad = 0;
//        result = connect.excute("MATCH (p:newKeywordKey{name:\""+query+"\"})-[r:similarKey]-(n:newKeywordKey) " +
//                        "RETURN p.name as p_name,p.times as p_times,n.name as n_name,n.times as n_times,r.times as r_times limit 200",
//                parameters( "", "" ));
//
//        HashMap<String,Integer> templist = new HashMap<>();
//        while ( result.hasNext() )
//        {
//            ++count;
//            Record record = result.next();
//            try {
//                if (templist.get(query) == null){
//                    templist.put(record.get("p_name").asString(),Integer.parseInt(record.get("p_times").asString()));
//                }
//                int similar_times = Integer.parseInt(record.get("similar_times").asString());
//                if (similar_times < 2) {
//                    ++bad;
//                    continue;
//                }
//                int name_times = Integer.parseInt(record.get("name_times").asString());
//                templist.put(record.get("name").asString(),name_times);
//                ++good;
//            }catch (Exception e){
//                continue;
//            }
//        }
//    }
    /**
     * 新方法 用于返回社区结构图
     * @return
     */
//    public HashMap<Integer,HashMap<Integer,HashMap<String,Integer>>> getCommubityData(List<String> list,boolean flag){
    public HashMap<Integer,HashMap<Integer,HashMap<String,Integer>>> getCommubityData(List<String> list){
//        LinkedList<String> locao_words = new LinkedList<>();
        HashMap<Integer,HashMap<Integer,HashMap<String,Integer>>> community = new HashMap<Integer,HashMap<Integer,HashMap<String,Integer>>>();
        HashMap<Integer,HashMap<String,Integer>> sonCommunity = new HashMap<Integer,HashMap<String,Integer>>();
        ConnectAndOperNeo4j connect = new ConnectAndOperNeo4j();
        StatementResult result;

        int bigIndex = 1;
        int sonIndex = 1;
        for (String string : list){
            int count = 0;
            int good = 0;
            int bad = 0;
            result = connect.excute("MATCH (p:newKeywordKey{name:\""+string+"\"})-[r:similarKey]-(n:newKeywordKey) " +
                            "RETURN p.name as p_name,p.times as p_times,n.name as n_name,n.times as n_times,r.times as r_times limit 400",
                    parameters( "", "" ));

            HashMap<String,Integer> templist = new HashMap<>();
            while ( result.hasNext() )
            {
                ++count;
                Record record = result.next();
                try {
                    if (templist.get(string) == null){
                        templist.put(record.get("p_name").asString(),Integer.parseInt(record.get("p_times").asString()));
                    }
                    int similar_times = Integer.parseInt(record.get("n_times").asString());
//                    System.out.println("////////////////////////////////////////////");
                    if (similar_times < 2) {
                        ++bad;
                        continue;
                    }
                    int name_times = Integer.parseInt(record.get("n_times").asString());
//                    System.out.println(name_times);
                    String name = record.get("n_name").asString();
//                    locao_words.add(name);
                    templist.put(name,name_times);
//                    templist.put(record.get("n_name").asString(),name_times);
                    ++good;
                }catch (Exception e){
//                    e.printStackTrace();
//                    ++bad;
                    continue;
                }
            }
//            if (flag && templist.size() > 1) {
//                sonCommunity.put(sonIndex++,templist);
//                break;
//            }else {
//                sonCommunity.put(sonIndex++,templist);
//            }
            sonCommunity.put(sonIndex++,templist);
            System.out.println("query： "+string);
            System.out.println("总数： "+count);
            System.out.println("bad总数： "+bad);
            System.out.println("good总数： "+good);

        }

        community.put(bigIndex,sonCommunity);
        connect.closeConnect();
//        UtilWrite.WriteLocalWordsObject(locao_words);
        return community;

    }




    /**
     * 传入（社区id为键,该社区成员的成员社区为键，对应的字符串为值）返回jsonString
     * @param community HashMap<Integer,HashMap<Integer,HashMap<String,Integer>>>
     *                  （社区id为键,该社区成员的成员社区为键，对应的字符串为键，该字符串的次数为值）
     * @return jsonString
     */
    public String formatJsonString(HashMap<Integer,HashMap<Integer,HashMap<String,Integer>>> community){
        JSONObject obj = new JSONObject();
        JSONArray objArray = new JSONArray();
        obj.put("name","community");
        for (int out : community.keySet()){
            int count = 0;
            for (int temp : community.get(out).keySet()){
                count += community.get(out).get(temp).size();
            }
//            if (count < 20)
//                continue;
            JSONObject obj1 = new JSONObject();
            boolean flag = true;
//            System.out.println("");
            JSONArray objArray1 = new JSONArray();
            for (int in : community.get(out).keySet()){
//                if (community.get(out).get(in).size() < 5){//处理一些小社区的碎点，可以不做处理，只添加社区节点多的一部分，若为美观，可以处理一下
////                    continue;
//                    for (String string : community.get(out).get(in).keySet()){
//                        JSONObject objtemp = new JSONObject();
//                        objtemp.put("name",string);
//                        objtemp.put("size",community.get(out).get(in).get(string));
//                        objArray1.put(objtemp);
//                    }
//                    continue;
//                }
//                System.out.println("");
                if(flag){
//                    obj1.put("name",community.get(out).get(in).get(0));
                    flag = false;
                }
                JSONObject obj2 = new JSONObject();
//                obj2.put("name",community.get(out).get(in).get(0);
                String stringMaxTimes = null;
                int maxTimes = 0;
                for (String string : community.get(out).get(in).keySet()){
                    if (community.get(out).get(in).get(string) > maxTimes){
                        maxTimes = community.get(out).get(in).get(string);
                        stringMaxTimes = string;
                    }
                }
                obj2.put("name",stringMaxTimes);
                JSONArray objArray2 = new JSONArray();
                for (String string : community.get(out).get(in).keySet()){
                    JSONObject obj3 = new JSONObject();
                    obj3.put("name",string);
                    obj3.put("size",community.get(out).get(in).get(string));
                    objArray2.put(obj3);
                }
                obj2.put("children",objArray2);
                objArray1.put(obj2);
            }
            obj1.put("children",objArray1);
//            obj1.put("name",objArray1.get(0).)
            objArray.put(obj1);
        }
        obj.put("children",objArray);
//        System.out.println(obj.toString());
//        boolean flag = createJsonFile(obj.toString(),"E:\\communitydata","Communityjson1");
//        System.out.println(flag);
        return obj.toString();
    }


//    public void method(){
//        List<String> list = new ArrayList<>();
//        String[] strings = {"人工智能","机器学习","云计算","物联网","信息技术",
//                "多媒体","云南","互联网+","故障诊断","数据挖掘","自动化","量子计算"};
//        for (String string : strings)
//            list.add(string);
//        UtilWrite.WriteQueryEntity(list);
//    }
}
