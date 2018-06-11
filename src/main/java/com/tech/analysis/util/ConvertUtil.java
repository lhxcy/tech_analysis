package com.tech.analysis.util;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/19 0019.
 */
@Component
public class ConvertUtil {


    public  JSONObject getKeywordJsonByStringList(List<String> keywordsList){
        HashMap<String,Integer> map = new HashMap<>();
        for (String keywords : keywordsList) {
            if(keywords == null)continue;
            String[] keyword = keywords.split(" ");
            for (String word : keyword) {
                map.put(word,map.getOrDefault(word,0)+1);
            }
        }
        JSONObject obj = new JSONObject();
        for (Map.Entry<String,Integer> entry : map.entrySet()) {
            obj.put(entry.getKey(),entry.getValue());
        }
        return obj;
    }

}
