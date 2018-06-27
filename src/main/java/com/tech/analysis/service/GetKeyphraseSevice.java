package com.tech.analysis.service;

import com.tech.analysis.Dao.*;
import com.tech.analysis.util.ConvertUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/17 0017.
 */
@Service
public class GetKeyphraseSevice {

    @Autowired
    private PaperDao paperDao;
    @Autowired
    private PatentDao patentDao;
    @Autowired
    private ProjectDao projectDao;
    @Autowired
    private ExpertDao expertDao;
    @Autowired
    private EnterpriseDao enterpriseDao;
    @Autowired
    private ConvertUtil convertUtil;
    @Autowired
    private AuthorDao authorDao;

    public void updateKeyphraseForPaper(){
//        //1.获取所有has_keyword字段为0的论文UID列表
//        List<String> uidList = paperDao.getUidList();
//        //2.对于每个uid对该论文的abstract_text_cn内容抽取关键词，存放进去
//        for (String uid : uidList) {
//            paperDao.updatePaperKeywordsByUid(uid);
//        }
        paperDao.updateKeyphraseForPaper();
    }

    public void updateKeyphraseForPatent(){

//        //1.获取所有has_keyword字段为0的专利id列表
//        List<String> idList = patentDao.getidList();
//        //2.对每个id对该专利的abstract_cn抽取关键词，存放
//        for (String id : idList) {
//            patentDao.updatePatentKeywordsByid(id);
//        }
        patentDao.updateKeyphraseForPatent();
    }

    public void updateKeyphraseForProject(){
        //1.获取所有has_keyword字段为0的专利id列表
        List<String> idList = projectDao.getidList();
        //2.对每个id对该专利的abstract_cn抽取关键词，存放
        for (String id : idList) {
            projectDao.updateProjectKeywordsByid(id);
        }
    }

    public void getKeyphraseForExpert(){

//        //1、获取所有的专家的id
//        List<String> expertIdList  =  expertDao.getIdList();
//        //2、对每一个专家进行合并关键词
//        for (String expertId : expertIdList) {
//            List<String> keywordsList = expertDao.getKeywordByExpertId("paper",expertId);
//            keywordsList.addAll(expertDao.getKeywordByExpertId("patent",expertId));
//            //keywordsList.addAll(expertDao.getKeywordByExpertId("project",expertId));
//            JSONObject obj = convertUtil.getKeywordJsonByStringList(keywordsList);
//            expertDao.putKeywordByExpertId(expertId,obj.toString());
//        }

        Map<String,String> uid2Keywords = paperDao.getUid2Keywords();
        Map<String,String> patentid2Keywords = patentDao.getPatentid2Keywords();
        Map<String,List<String>> expertid2Uids = authorDao.getExpertid2Uids();
        Map<String,List<String>> expertid2Patentids = patentDao.getExpertid2Patentids();
        Map<String,List<String>> expertid2Keywords = new HashMap<>();
        for(Map.Entry<String,List<String>> entry : expertid2Uids.entrySet()){
            List<String> uidList = entry.getValue();
            List<String> keywordList = new LinkedList<>();
            for (String uid : uidList) {
                if(uid2Keywords.containsKey(uid)){
                    keywordList.add(uid2Keywords.get(uid));
                }
            }
            expertid2Keywords.put(entry.getKey(),keywordList);
        }
        uid2Keywords = null;
        for(Map.Entry<String,List<String>> entry : expertid2Patentids.entrySet()){
            List<String> patentidList = entry.getValue();
            List<String> keywordList = new LinkedList<>();
            for (String patentid : patentidList) {
                if(patentid2Keywords.containsKey(patentid)){
                    keywordList.add(patentid2Keywords.get(patentid));
                }
            }
            String expertid = entry.getKey();
            if(expertid2Keywords.containsKey(expertid)){
                List<String> preKeywordsList = expertid2Keywords.get(expertid);
                keywordList.addAll(preKeywordsList);
            }
            expertid2Keywords.put(expertid,keywordList);
        }
        patentid2Keywords = null;
        Map<String,String> expertid2Keyword = new HashMap<>();
        for (String expertid : expertid2Keywords.keySet()) {
            JSONObject obj = convertUtil.getKeywordJsonByStringList(expertid2Keywords.get(expertid));
            expertid2Keyword.put(expertid,obj.toString());
        }
        expertDao.updateKeyowrdsForExpert(expertid2Keyword);
        expertid2Keyword = null;
        Map<String,List<String>> enterprise2Experts = enterpriseDao.getEnterprise2Expert();
        Map<String,List<String>> enterpriseid2Keywords = new HashMap<>();
        for (Map.Entry<String,List<String>> entry : enterprise2Experts.entrySet()) {
            List<String> expertidList = entry.getValue();
            List<String> keywordList = new LinkedList<>();
            for (String expertid : expertidList) {
                if(expertid2Keywords.containsKey(expertid)){
                    keywordList.addAll(expertid2Keywords.get(expertid));
                }
            }
            enterpriseid2Keywords.put(entry.getKey(),keywordList);
        }
        expertid2Keywords = null;
        enterprise2Experts = null;
        Map<String,String> enterpriseid2Keyword = new HashMap<>();
        for (String enterpriseid : enterpriseid2Keywords.keySet()) {
            JSONObject obj = convertUtil.getKeywordJsonByStringList(enterpriseid2Keywords.get(enterpriseid));
            enterpriseid2Keyword.put(enterpriseid,obj.toString());
        }
        enterpriseDao.updateKeyowrdsForEnterprise(enterpriseid2Keyword);
        enterpriseid2Keyword = null;
        enterpriseid2Keywords = null;
    }

    public void getKeyphraseForEnterprise(){
        //1、获取所有企业id
        List<String> enterpriseIdList = enterpriseDao.getAllEnterpriseIdList();
        //2、对于每一个企业id合并出它的关键词
        for (String enterpriseId : enterpriseIdList) {
            List<String> keywordsList = enterpriseDao.getKeywordsByEnterpriseId(enterpriseId);
            JSONObject obj = convertUtil.getKeywordJsonByStringList(keywordsList);
            enterpriseDao.putKeywordByEnterpriseId(enterpriseId,obj.toString());
        }

        //Map<String,String> enterprise2Experts = enterpriseDao.getEnterprise2Expert();
    }

}
