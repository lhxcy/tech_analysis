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
            customTaskDao.updateErrorMsg("msg_deal_data",msg,id);
            return "error when data update: "+msg;
        }

        //yxj
        try{
            doIndexService.creat_yangqiIndex();
            doIndexService.creat_paperIndex();
            doIndexService.creat_yangqipaperIndex();
        }catch(Exception e){
            String msg = e.toString();
            customTaskDao.updateErrorMsg("msg_deal_data",msg,id);
            return "error when create Index: "+msg;
        }


        //xcy
//        dealDataService.dealNeo4j();

        try{
            customTaskDao.update(id);
        }catch(Exception e){
            String msg = e.toString();
            customTaskDao.updateErrorMsg("msg_neo4j_data",msg,id);
            return "error when create Neo4j: "+msg;
        }

        return "success";
    }

}
