package com.tech.analysis.controller;

import com.tech.analysis.entity.Enterprise;
import com.tech.analysis.service.GetKeyphraseSevice;
import com.tech.analysis.service.MatchService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;


/**
 * Created by Administrator on 2018/3/21 0021.
 */
@CrossOrigin
@RestController
@RequestMapping("/match")
public class MatchController {

    private Logger logger = LoggerFactory.getLogger("sgc");

    @Autowired
    private MatchService matchService;
    @Autowired
    private GetKeyphraseSevice getKeyphraseSevice;
//    @RequestMapping("/getEnterprise")
//    public List<Enterprise> getEnterprise(@RequestBody Map<String,Object> map){
//        String source = (String) map.get("source");//数据来源
//        List<Enterprise> list = new ArrayList<>();
//        if(source.equals("paper")){
//            list = matchService.getEnterpriseListOfSimilarName((String) map.get("aliasName"));
//        }
//        return  list;
//    }


    @RequestMapping("/testLog") //提供路由信息，”/“路径的HTTP Request都会被映射到sayHello方法进行处理。
    public String sayHello(){

        logger.info("This is an info message,test SUCCESS");

        return "Hello,World!";
    }

    @RequestMapping("/getEnterprise")
    public List<Enterprise> getEnterprise(@RequestParam String aliasName,@RequestParam String source){
        List<Enterprise> list = new ArrayList<>();
        list = matchService.getEnterpriseListOfSimilarName(aliasName,source);
        return  list;
    }
//    @RequestMapping("/update")
//    public Enterprise updateEnterprise(@RequestBody Map<String,Object> map){
//
//        //构造Enterprise实体，数据来源，机构名；找到该机构实体对应的EntepriseInfo 中的id
//        Enterprise enterprise = matchService.setEnterpriseByRequest(map);
//        String source = (String) map.get("source");//数据来源
//        String aliasName = (String) map.get("aliasName");//机构名
//        //如果数据来源是论文
//        if(source.equals("paper")){
//            //根据机构名去temp表中查该机构发表的论文（可能多篇）,
//            // 将该机构发的论文对应到Address表中，将该机构名和对应id存入别名中
//            matchService.updatePaper(enterprise,aliasName);
//        }
//        return enterprise;
//    }w
    @RequestMapping("/update")
    public String updateEnterprise(@RequestParam String aliasName,@RequestParam String source,@RequestParam String id){

        int companyId = Integer.parseInt(id);
        if(source.equals("paper")){
            //根据机构名去temp表中查该机构发表的论文（可能多篇）,
            // 将该机构发的论文对应到Address表中，将该机构名和对应id存入别名中
            matchService.updatePaper(companyId,aliasName);
        }else if(source.equals("patent")){
            matchService.updatePatent(companyId,aliasName);
        }
        return "success";
    }

    /**
     * @param source  根据请求来源返回匹配机构列表
     * @return
     */
    @RequestMapping("/getWaitForMatch")
    public List<String> getWaitForMatch(@RequestParam String source){
        List<String> names = new ArrayList<>();
        names = matchService.getWaitForMatch(source);
        return names;
    }

    /**
     * @param aliasName  回退人工匹配
     * @param source
     * @param id
     * @return
     */
    @RequestMapping("/rollbackMatch")
    public String rollbackMatch(@RequestParam String aliasName,@RequestParam String source,@RequestParam String id){
        int companyId = Integer.parseInt(id);
        matchService.rollbackMatch(companyId,aliasName,source);
        return "success";
    }

    /**
     * @return  从数据表中抽出待匹配专利机构表
     */
    @RequestMapping("/getPatentForMatchToTable")
    public String getPatentForMatchToTable(){
        matchService.getPatentForMatchToTable();
        return "success";
    }

    @RequestMapping("/updateKeywords")
    public String updateKeywords(){
        getKeyphraseSevice.updateKeyphraseForPaper();
        getKeyphraseSevice.updateKeyphraseForPatent();
        //getKeyphraseSevice.updateKeyphraseForProject();

        return "success";
    }

    @RequestMapping("/prizeForMatch")
    public void i(){
        matchService.getPrizeForMatch();
    }

    @RequestMapping("/ttt")
    public String ttt(){
        matchService.whenDataUpdate();

        updateKeywords();
        getKeyphraseSevice.getKeyphraseForExpert();
        getKeyphraseSevice.getKeyphraseForEnterprise();
        return "success";
    }
}
