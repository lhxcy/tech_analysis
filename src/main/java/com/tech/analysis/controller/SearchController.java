package com.tech.analysis.controller;

import com.google.gson.JsonObject;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.tech.analysis.service.Search4;
import com.tech.analysis.service.SearchServices;
import com.tech.analysis.service.DoIndexService;






@Controller
@CrossOrigin
public class SearchController {
    private static org.slf4j.Logger log = LoggerFactory.getLogger("yxj");

    @RequestMapping("/searchyangqi")
    @ResponseBody
    @CrossOrigin
    public String get_yangqi_result(@RequestParam String yangqi)
    {
        log.info(yangqi);
        if(yangqi.equals("“”")||yangqi.equals(""))
            return "请输入需要查询的关键字";
        String[] yangKey=yangqi.split(" ");
        for(String str:yangKey) {
            log.info("aaa"+str+"bbbb");
        }

        JsonObject res=null;
        if(yangKey.length>0)
        {
            res=SearchServices.searchYangqi(yangKey);
        }
        return res.toString();
    }

    @RequestMapping("/searchpaper")
    @ResponseBody
    @CrossOrigin
    public String get_expert_result(@RequestParam String paper,@RequestParam int year)
    {

        log.info("查询关键字"+paper);
        log.info("查询开始年份"+year);
        if(paper.equals("“”")||paper.equals(""))
            return "请输入需要查询的关键字";
        String[] paperKey=paper.split(" ");
        JsonObject res=null;
        if(paperKey.length>0)
        {
            res=SearchServices.searchpaper(paperKey,year);
        }
       return res.toString();
    }

    @RequestMapping("/searchyangqipaper")
    @ResponseBody
    @CrossOrigin
    public String get_yangqipaper_result(@RequestParam String paper,@RequestParam int year)
    {

        log.info("查询关键字"+paper);
        log.info("查询开始年份"+year);
        if(paper.equals("“”")||paper.equals(""))
            return "请输入需要查询的关键字";
        String[] paperKey=paper.split(" ");
        JsonObject res=null;
        if(paperKey.length>0)
        {
            res=SearchServices.searchyangqipaper(paperKey,year);
        }
        return res.toString();
    }

    @GetMapping("/greeting")
    public String greetingForm() {

        return "exper_search";
    }

    @GetMapping("/home")
    public String showHome() {

        return "home";
    }
//    @PostMapping("/data_accept")
    @RequestMapping(value="/data_accept",method=RequestMethod.GET)
    @ResponseBody
    public String greetingSubmit(@RequestParam String name,@RequestParam String sex,@RequestParam String functionname,
                                 @RequestParam String profield,@RequestParam String enterprisename,@RequestParam String type,
                                 @RequestParam String education,@RequestParam String zhucedi,@RequestParam String tech_area,
                                 @RequestParam String study_dir,@RequestParam String study_result,@RequestParam String avoid_people,
                                 @RequestParam String avoid_company,@RequestParam int current_page,@RequestParam int page_size) {//接收前端返回的json数据，转变为Map
        String[] str1=new String[14];
        String[] str2=new String[14];
        int index=0;
        if(!name.equals("")) {
            str1[index]="name";str2[index]=name;index++; }
        if(!sex.equals("")) {
            str1[index]="sex";str2[index]=sex;index++; }
        if(!functionname.equals("")) {
            str1[index]="functionname";str2[index]=functionname;index++; }
        if(!profield.equals("")) {
            str1[index]="profield";str2[index]=profield;index++; }
        if(!enterprisename.equals("")) {
            str1[index]="enterprisename";str2[index]=enterprisename;index++; }
        if(!type.equals("")) {
            str1[index]="type";str2[index]=type;index++; }
        if(!education.equals("")) {
            str1[index]="education";str2[index]=education;index++; }
        if(!zhucedi.equals("")) {
            str1[index]="zhucedi";str2[index]=zhucedi;index++; }
        if(!tech_area.equals("")) {
            str1[index]="tech_area";str2[index]=tech_area;index++; }
        if(!study_dir.equals("")) {
            str1[index]="study_dir";str2[index]=study_dir;index++; }
        if(!study_result.equals("")) {
            str1[index]="study_result";str2[index]=study_result;index++; }
        if(!avoid_people.equals("")) {
            str1[index]="avoid_people";str2[index]=avoid_people;index++; }
        if(!avoid_company.equals("")) {
            str1[index]="avoid_company";str2[index]=avoid_company;index++; }
//        Map<String,String> avoid=new HashMap<String,String>();
//        if(!avoid_people.equals("")) {
//            avoid.put("avoid_people",avoid_people); }
//        if(!avoid_company.equals("")) {
//            avoid.put("avoid_company",avoid_company); }
        JsonObject obj=new JsonObject();
        try{
            System.out.println("start");
            System.out.println(str1.length);
            System.out.println(avoid_people);
            System.out.println(avoid_company);
//             obj=SearchExpert.search(str1,str2,page_size,current_page);
//            obj=test_search.search(str1,str2,avoid,page_size,current_page);
            obj=Search4.search(str1,str2,page_size,current_page);
        }catch (Exception e)
        {
            System.out.println(e);
        }
        String str=obj.toString();

            //4.处理数据,并返回实体给用户,页面通过第一步的"greeting"参数来展示数据
        return str;
    }
    //索引创建和删除页面
    @GetMapping("/index")
    public String creat() {
        return "Do_Index";
    }
    //创建索引
    @PostMapping("/creat_index")
    @ResponseBody
    public String creat_index() {
        String data=DoIndexService.creatIndex();
        return data;
    }
    //创建央企索引
    @PostMapping("/creatyangqi_index")
    @ResponseBody
    public String creatyangqi_index() {
        String data=DoIndexService.creat_yangqiIndex();
        return data;
    }
    //创建专家索引
    @PostMapping("/creatpaper_index")
    @ResponseBody
    public String creatpaper_index() {
        String data=DoIndexService.creat_paperIndex();
        return data;
    }
    //删除索引
    @PostMapping("/delete_index")
    @ResponseBody
    public String delete_index() {
        String data=DoIndexService.deleteIndex();
        return data;
    }
    //删除央企索引
    @PostMapping("/deleteyangqi_index")
    @ResponseBody
    public String deleteyangqi_index() {
        String data=DoIndexService.deleteyangqiIndex();
        return data;
    }
    //删除专家索引
    @PostMapping("/deletepaper_index")
    @ResponseBody
    public String deletepaper_index() {
        String data=DoIndexService.deletepaperIndex();
        return data;
    }
}
