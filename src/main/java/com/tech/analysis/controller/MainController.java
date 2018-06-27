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
        matchService.whenDataUpdate();
        getKeyphraseSevice.updateKeyphraseForPaper();
        getKeyphraseSevice.updateKeyphraseForPatent();
        getKeyphraseSevice.getKeyphraseForExpert();
        customTaskDao.updateAfterSgc(id);
        //yxj
        doIndexService.creat_yangqiIndex();
        doIndexService.creat_paperIndex();
        doIndexService.creat_yangqipaperIndex();

        //xcy
//        dealDataService.dealNeo4j();

        customTaskDao.update(id);
        return "success";
    }

}
