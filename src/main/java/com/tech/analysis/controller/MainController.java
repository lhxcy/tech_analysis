package com.tech.analysis.controller;

import com.tech.analysis.Dao.CustomTaskDao;
import com.tech.analysis.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhzy on 18-6-1.
 */
@CrossOrigin
@RestController
@RequestMapping("/main")
public class MainController {

    @Autowired
    private MatchService matchService;
    @Autowired
    private GetKeyphraseSevice getKeyphraseSevice;
    @Autowired
    private DealDataService dealDataService;
    @Autowired
    private CustomTaskDao customTaskDao;

    private DoIndexService doIndexService=new DoIndexService();
    @RequestMapping("/maindata")
    public String mainData(@RequestParam String id){
        if(!customTaskDao.check(id))return "spider or importdata uncomplete";

        //sgc
        try{
            matchService.whenDataUpdate();
            getKeyphraseSevice.updateKeyphraseForPaper();
            getKeyphraseSevice.updateKeyphraseForPatent();
            getKeyphraseSevice.getKeyphraseForExpert();
            customTaskDao.updateAfterSgc(id);
        }catch(Exception e){
            String msg = e.toString();
            customTaskDao.updateMsg("msg_deal_data",msg,id);
            return "error when data update: "+msg;
        }
        customTaskDao.updateMsg("msg_deal_data","update data successs",id);
        //yxj
        try{
            doIndexService.creat_yangqiIndex();
            doIndexService.creat_paperIndex();
            doIndexService.creat_yangqipaperIndex();
        }catch(Exception e){
            String msg = e.toString();
            customTaskDao.updateMsg("msg_deal_data",msg,id);
            return "error when create Index: "+msg;
        }
        customTaskDao.updateMsg("msg_deal_data","create Index successs",id);

        //xcy
//        try{
//            dealDataService.dealNeo4j();
//        }catch(Exception e){
//            String msg = e.toString();
//            customTaskDao.updateMsg("msg_neo4j_data",msg,id);
//            return "error when create Neo4j: "+msg;
//        }
//        customTaskDao.updateMsg("msg_neo4j_data","create Neo4j successs",id);

        customTaskDao.update(id);
        return "success";
    }

}
