package com.tech.analysis.service;

import com.sun.xml.internal.bind.v2.runtime.output.Encoded;
import com.tech.analysis.Dao.*;
import com.tech.analysis.entity.*;
import com.tech.analysis.util.MatchUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by Administrator on 2018/3/21 0021.
 */
@Service
public class MatchService {

    @Autowired
    private EnterpriseDao enterpriseDao;
    @Autowired
    private PaperDao paperDao;
    @Autowired
    private MatchDao matchDao;
    @Autowired
    private PatentDao patentDao;
    @Autowired
    private MatchUtil matchUtil;
    @Autowired
    private AuthorDao authorDao;
    @Autowired
    private ExpertDao expertDao;
    @Autowired
    private Expert2EnterpriseDao expert2EnterpriseDao;
    @Autowired
    private AddressDao addressDao;
    @Autowired
    private PrizeDao prizeDao;


    Map<Enterprise,String> enterpriseWithExpert;
    List<Enterprise> enterpriseList;


    /**
     * @param enterpriseName 根据一个企业名称，获取和其可能对应的机构信息
     * @return 企业实体列表
     */
    public List<Enterprise> getEnterpriseListOfSimilarName(String enterpriseName,String source){
        List<Enterprise> list = new ArrayList<>();
        // （New） 不论待匹配项来源于哪，需要去匹配表中检测，如果已经被匹配过了，直接放回匹配结果
        List<Enterprise> enterpriseHasBeenMatched = enterpriseDao.getEnterpriseByAliasname(enterpriseName);
        if(enterpriseHasBeenMatched.size() > 0)return enterpriseHasBeenMatched;
        //获得所有基准企业实体,（写成成员变量可以防止每次调用方法都加载一遍，这样只需要第一次使用时加载）
        //List<Enterprise> enterpriseList = enterpriseDao.getAllEnterpriseList();
        if(enterpriseList == null)enterpriseList = enterpriseDao.getAllEnterpriseList();
        //如果该机构名称是汉语的
        if(matchUtil.isChinese(enterpriseName)){
            //将所有企业列表和该企业名传进去，返回所有和该企业名相似的企业列表
            list = matchUtil.getSimEnterpriseList(enterpriseName,enterpriseList);
            //  (New) 按工作人员进行匹配得出的相似企业列表
            Map<Enterprise,Integer> simEnterpriseAndWeight = new HashMap<>();
            //Map<Enterprise,String> enterpriseWithExpert =  enterpriseDao.getEnterpriseWorkers();
            if(enterpriseWithExpert == null)enterpriseWithExpert =  enterpriseDao.getEnterpriseWorkers();
            //待匹配机构的工作人员
            List<String> theWokers = new ArrayList<>();
            if(source.equals("paper")){
                //获取待匹配机构的工作人员
                theWokers = paperDao.getWorksByOrgnazationName(enterpriseName);
            }else if(source.equals("patent")){

            }
            for (Map.Entry<Enterprise,String> entry : enterpriseWithExpert.entrySet()) {
                int num = 0;
                String workers = entry.getValue();
                for (String worker : theWokers) {
                    if(workers.indexOf(worker)!=-1)
                        num++;
                }
                //当与某个机构共有的员工数量是这个带匹配机构员工数量的一定比例时，判断为可能别名机构
                if(num > theWokers.size()/3) simEnterpriseAndWeight.put(entry.getKey(),num);
            }
            //将按名字字符串匹配得到的相似机构，按相同工作人员个数为5的权值加入，用于统一排序
            for (Enterprise enterprise : list) {
                simEnterpriseAndWeight.put(enterprise,simEnterpriseAndWeight.getOrDefault(enterprise,0)+5);
            }
            List<Map.Entry<Enterprise,Integer>> sortList = new ArrayList<>();
            sortList.addAll(simEnterpriseAndWeight.entrySet());
            ValueComparator vc = new ValueComparator();
            Collections.sort(sortList,vc);
            List<Enterprise> resultList = new ArrayList<>();
            for(Map.Entry<Enterprise,Integer> entry : sortList){
                resultList.add(entry.getKey());
            }
            return resultList;

        }else if(source.equals("paper")){//机构名是英语的情况,一般出现在paper中

            //创建一个队列，用于存放该英文机构对应的可能中文机构
            List<String> maybeChineseNameList = new ArrayList<>();
            //在temp表中获取该英文机构的UID（可能不止一个，取一个即可包含）
            String paperUID = paperDao.getUidByName(enterpriseName);
            //在temp表中查包含该论文的中文机构名，加入队列
            List<String> nameListFromAddressTemp = paperDao.getNameListByUid(paperUID,"AddressTemp");
            //用UID去Address表中查找该论文对应的机构名，如果是中文的，加入队列
            List<String> nameListFromAddress = paperDao.getNameListByUid(paperUID,"Address");
            //将该机构对应的几个可能的中文名，按汉语相似机构匹配方式匹配
            nameListFromAddress.addAll(nameListFromAddressTemp);
            HashSet<String> hashSet = new HashSet<>(nameListFromAddress);
            nameListFromAddress.clear();
            nameListFromAddress.addAll(hashSet);
            for (String name:nameListFromAddress) {
                if(matchUtil.isChinese(name))
                    maybeChineseNameList.add(name);
            }
            for (String name:maybeChineseNameList) {
                //List<Enterprise> alist = matchUtil.getSimEnterpriseList(name,enterpriseList);
                List<Enterprise> alist = getEnterpriseListOfSimilarName(name,"paper");
                list.addAll(alist);
            }
            //对可能的多个中文名匹配的相似机构可能重复，去重
            HashSet<Enterprise> enterprisesSet = new HashSet<>(list);
            list.clear();
            list.addAll(enterprisesSet);
            if(list.size() == 0){
                String tanslationName = matchUtil.translationEnglishName(enterpriseName);
                list = getEnterpriseListOfSimilarName(tanslationName,"paper");
            }
        }
        return  list;
    }

    /**
     * @param map  根据请求参数构造Enterprise实体
     * @return Enterprise实体
     */
    public Enterprise setEnterpriseByRequest(Map<String,Object> map){
        Enterprise enterprise = new Enterprise();
        enterprise.setName((String)map.get("name"));
        enterprise.setChuziqiye((String)map.get("chuziqiye"));
        //不知道前端传来的数据是Integer还是Long（会根据数值大小变化），
        // Integer 不能直接转换为Long,他们没有继承关系
//        Number num = (Number) map.get("zhuceziben");
//        Long zhuceziben;
//        if(num!=null){
//            zhuceziben = num.longValue();
//        }else{
//            zhuceziben = null;
//        }
        enterprise.setZhuceziben((String) map.get("zhuceziben"));
        enterprise.setHangye((String)map.get("hangye"));
        enterprise.setZhuceriqi((String)map.get("zhuceriqi"));
        enterprise.setCode((String)map.get("code"));
        enterprise.setType((String)map.get("type"));
        enterprise.setZuzhixingshi((String)map.get("zhucexingshi"));
        enterprise.setLevel((String)map.get("level"));
        enterprise.setZhucedi((String)map.get("zhucedi"));
        return enterprise;
    }

//    /**
//     * @param enterprise 根据前端选择的企业名称和对应企业信息，更新数据库表内容，匹配机构别名
//     * @param aliasName AddressTemp 表中的organization名
//     */
//    public void updatePaper(Enterprise enterprise,String aliasName){
//        //1、根据organization==aliasName去AddressTemp表中匹配出该机构的所有论文List<Address>
//        List<AddressTemp> addressTemps = paperDao.getAddressTempsByName(aliasName);
//        //2、根据选中的企业实体enterprise去EnterpriseInfo中匹配得到companyid
//        int companyId = enterpriseDao.getCompanyIdByEnterprise(enterprise);
//        for (AddressTemp a:addressTemps
//                ) {
//            System.out.print(a);
//        }
//        //3、将AddressTemp和companyid插入Address表中
//        updateAddress(companyId,addressTemps);
//        //4、将AddressTemp中对应aliasName记录删除
//        paperDao.deleteItemInAddressTempByOrganization(aliasName);
//        //5、向CompanyAlias中插入企业别名和对应companyid
//        enterpriseDao.updateCompanyAlias(companyId,aliasName);
//        //注意他的数据库里id字段不是自增的
//    }
    @Transactional
    public void updatePaper(int companyId,String aliasName){
        //1、根据organization==aliasName去AddressTemp表中匹配出该机构的所有论文List<Address>
        List<AddressTemp> addressTemps = paperDao.getAddressTempsByName(aliasName);

        //3、将AddressTemp和companyid插入Address表中
        updateAddress(companyId,addressTemps);

        //4、将AddressTemp中对应aliasName记录删除
        paperDao.deleteItemInAddressTempByOrganization(aliasName);

        //5、向CompanyAlias中插入企业别名和对应companyid
        enterpriseDao.updateCompanyAlias(companyId,aliasName);

        //注意他的数据库里id字段不是自增的
        //往匹配记录表MatchRecord里记录匹配数据，以备后序撤销匹配操作
        matchDao.updateMatchRecord(companyId,aliasName,"paper");
        updateExpertInPaper(companyId,aliasName);
    }

    @Transactional
    public List<String> updateExpertInPaper(int companyId,String aliasName){
        //0.获取companyI对应的企业name
        String enterpriseName = enterpriseDao.getEnterpriseNameById(String.valueOf(companyId));

        //1.去AuthorTemp中取出所有full_address为aliasName的作者名list<display_name>
        List<String> displayNameList = authorDao.getDisplayNameListByFullAddress(aliasName);

        //2.以enterprisename和display_name为主键插入Expert表中,newExpertIdList记录新插入的专家的id可以用于回滚
        List<String> newExpertIdList = expertDao.insertByEnterpriseNameAndName(enterpriseName,displayNameList);

        //3.以enterprisename和display_name查询expertid插入Author中
        List<String> expertIdList = new ArrayList<>();
        for (String name : displayNameList) {
            expertIdList.add(expertDao.getExpertIdByNameAndEnterpriseName(name,enterpriseName));
        }
        authorDao.insertFromAuthorTemp(aliasName);
        authorDao.deleteFormAuthorTemp(aliasName);

        //4.插入expert2enterprise
        expert2EnterpriseDao.insertByExpertIdAndEnterpriseId(String.valueOf(companyId),expertIdList);
        //可以记录新插入的专家Id,用于回滚
        return newExpertIdList;
    }


    /**
     * @param name
     * @param source
     */
    @Transactional
    public void insertNewEnterprise(String name,String source){
        //插入新的企业进基准表
        enterpriseDao.insertNewEnterprise(name);
        Enterprise e = new Enterprise();
        e.setName(name);
        //添加到内存中
        enterpriseList.add(e);
        int id = enterpriseDao.getEnterpriseIdByName(name);
        if(source.equals("paper"))updatePaper(id,name);
        else if(source.equals("patent"))updatePatent(id,name);
    }

    /**
     * @param companyId 对选出来的数据进行插入
     * @param addressTemps
     * @return
     */
    public void updateAddress(int companyId,List<AddressTemp> addressTemps){
        for (AddressTemp addressTemp:
             addressTemps) {
            paperDao.updateAddress(companyId,addressTemp);
        }
    }

    public List<String> getWaitForMatch(String source){
        List<String> names = new ArrayList<>();
        if(source.equals("paper")){
            names = paperDao.getAddressTempNames();
        }
        return names;
    }

    /**
     * @param companyId 对曾今插入过的数据进行回滚
     * @param aliasName
     * @param source
     */
    @Transactional
    public void rollbackMatch(int companyId,String aliasName,String source){
        if(source.equals("paper")){
            //可能这个机构对应的多条论文都被错插了，所以aliasName，companyId在Address中可能会匹配到多条一致的AddressTemp需要插入
            //1、从Address中获取对应的AddressTemp数据
            List<AddressTemp> addressTemps = paperDao.getAddressTempsInAddress(companyId,aliasName);
            //2、将获取到的数据逐条插入到Temp表中
            updateAddressTemp(addressTemps);
            //3、将Address对应的原有的数据删除
            paperDao.deleteItemByCompanyIdAndAliasname("Address",companyId,aliasName);
            //4、将CompanyAlias中的对应数据删除
            matchDao.deleteItemByCompanyIdAndAliasname("CompanyAlias",companyId,aliasName);
            //5、MatchRecord表中去除该匹配记录
            matchDao.deleteItemByCompanyIdAndAliasname("MatchRecord",companyId,aliasName);
        }else if(source.equals("patent")){
            //1.根据aliasName(即专利中的企业名)去patentForMatchBackup表中找所有patentid
            List<String> patentIdList = patentDao.getPatentIdListByEnterpriseName("patentForMatchBackup", aliasName);
            for (String patentId : patentIdList) {
                //2.将patentid,alisasName重新插回patentForMatch表(当前还是patentForMatchNew)中去
                patentDao.updatePatentForMatch(patentId,aliasName);
                //3.根据patentid和companyId去patent2enterprise中删除
                patentDao.deleteItemInPatent2Enterprise(patentId,companyId);
            }
            //4、将aliasName和companyId从companyAlias表中删除
            matchDao.deleteItemByCompanyIdAndAliasname("CompanyAlias",companyId,aliasName);
            //5、将MatchRecord中companyid,aliasName删除
            matchDao.deleteItemByCompanyIdAndAliasname("MatchRecord",companyId,aliasName);
        }
    }

    public void updateAddressTemp(List<AddressTemp> addressTemps){
        for (AddressTemp addressTemp : addressTemps) {
            paperDao.updateAddressTemp(addressTemp);
        }
    }


    public void getPatentForMatchToTable(){
        //1、从Patent表中找出所有的patentid和enterpriseName
        //用一个Map<String,List<String>>来接受查询结果，MapRow中将enterpriseName分割放入
        List<PatentIdAndEnterpriseNames> patentIdAndEnterpriseNamesList = patentDao.getpatentIdAndEnterpriseNames();
        //2、对每个patentid去patent2enterprise中查找对应的List<enterpriseid> ，
        for (PatentIdAndEnterpriseNames patentIdAndEnterpriseNames:patentIdAndEnterpriseNamesList) {
            updatePatentForMatch(patentIdAndEnterpriseNames);
        }
        // 如果存在：
        // 看enterpriseid 的size 和 enterpriseName 的size 是否一致，一致说明这个专利已经被匹配过了，对下一条进行判断；
        // 若不一致，说明有的被匹配过有的没有，找出enterpriseid对应name,查找缺失的那个插入数据库表
        //如果不存在，说明这一整条都没被识别过，将enterpriseName和patentid作为一条记录插入待匹配表
    }

    public void updatePatentForMatch(PatentIdAndEnterpriseNames patentIdAndEnterpriseNames){
        String[] enterpriseNames = patentIdAndEnterpriseNames.getEnterpriseNames();
        //1、根据他的patentId去patent2enterprise中查找对应的List<enterpriseid>
        List<String> enterpriseids = patentDao.getEnterpriseIdListByPatentId(patentIdAndEnterpriseNames.getPatentId());
        //2、如果没有查到对应的id,那说明整条专利都没被匹配过，把他们放入待匹配表中
        if(enterpriseids.size()==0){
            for (String name:enterpriseNames) {
                patentDao.putPatentToPatentForMatchTable(patentIdAndEnterpriseNames.getPatentId(),name);
            }
        }
        //3、如果查到了
        else{
            //看一下查找到的id数量是否和enterpriseName数量相等
            // 如果相等，说明这条专利都被匹配过了，跳过这条
            if(enterpriseids.size() >= enterpriseNames.length)
                return;
            // 如果不相等
            else{
                //说明这条专利有的机构名被匹配过了，有的还没有
                for (String enterpriseId:enterpriseids) {
                    //找出每一个匹配过的名字，标记出来，剩下的就是没有匹配过的
                    String enterpriseName = enterpriseDao.getEnterpriseNameById(enterpriseId);
                    for(int i = 0;i < enterpriseNames.length;i++){
                        if(enterpriseName.equals(enterpriseNames[i])){
                            enterpriseNames[i] = "made";
                        }
                    }
                }
                //对于每一个没有被标记过的，即还没有匹配的，插入待匹配表中
                for (String name:enterpriseNames) {
                    if(!name.equals("made")){
                        patentDao.putPatentToPatentForMatchTable(patentIdAndEnterpriseNames.getPatentId(),name);
                    }
                }
            }
        }
    }

    @Transactional
    public void updatePatent(int companyId,String aliasName){
        //1、根据aliasName去patentForMatch表中查到所有该机构的专利id List<id>
        List<String> patentIdList = patentDao.getPatentIdListByEnterpriseName("patentForMatch",aliasName);
        //2、将List<id>中每一个patentid和companyId插入patent2Enterprise表中
        for (String id : patentIdList) {
            patentDao.updatePatent2Enterprise(id,companyId);
        }
        //3、将patentForMatch表中对应alisaName数据删除
        patentDao.deleteItemByEnterpriseName("patentForMatch",aliasName);
        //4、将aliasName和companyId置入companyAlias表中
        matchDao.updateCompanyAlias(companyId,aliasName);
        //5、记录在MatchRecord中
        matchDao.updateMatchRecord(companyId,aliasName,"patent");
    }


    private  class ValueComparator implements Comparator<Map.Entry<Enterprise,Integer>>
    {
        public int compare(Map.Entry<Enterprise,Integer> m,Map.Entry<Enterprise,Integer> n)
        {
            return n.getValue()-m.getValue();
        }
    }


    /**
     * 新来的数据入库后，先将能对上的数据对上，新的数据都在temp中
     */
    @Transactional
    public void preMatchPaper(){
        //1.先将AddressTemp中的数据跟companyAlias对上，插入到Address中，即将论文数据的机构对上
        addressDao.preMatchAddress();
        //2.将AuthouTemp中的数据和Address的数据对上（uid和full_address），得到新论文数据中企业能被对上
        // 的论文的作者信息，得到Author和对应企业id
        List<Expert> preExpertList =  authorDao.getAuthorForNewExpertList();
        //3.将Expert和expert2enterprise联合得到每个专家和企业id
        List<Expert> expertList = expertDao.getExpertList();
        //4.将所有2中得到的和3中得到的进行比较，新的插入Expert,Expert2Enterprise
        System.out.println("===============开始去重removeAll====================");
        //preExpertList.removeAll(expertList);
        preExpertList = matchUtil.removeAll(preExpertList,expertList);
        //去重
        System.out.println("===============开始去重set====================");
        HashSet<Expert> expertSet = new HashSet<>(preExpertList);
        preExpertList.clear();
        preExpertList.addAll(expertSet);
        expertDao.insertNewExpert(preExpertList);
        //5.将AuthorTemp和Expert联合查找，插入Author中，并删除AuthorTemp中的数据
        authorDao.updateAuthorAndDeleteInTemp();
    }

    /**
     *人工匹配之后，将无法匹配上的机构、直接在enterpriseInfo中新建
     */
    @Transactional
    public void postMatchPaper(){
        //1.从AddressTemp中取出所有机构名字，这些都是无法匹配的机构，建立为新的基准机构，即插入Enterprise中
        addressDao.insertNewEnterpriseFromAddressTemp();
        //2.插入CompanyAlias表
        matchDao.insertNewCompanyAlias();
        //3.重新执行preMatch
        preMatchPaper();
    }

    /**
     *在此之前已经生成patentForMatch    还需插入第一作者
     */
    @Transactional
    public void preMatchPatent(){

        //2、对patentForMatch表和companyAlias联合查找，找到可以直接对上的机构，插入patent2enterprise
        patentDao.insertNewPatent2Enterprise();
        //3、对于能对上的patent，找到对应id和机构名对应的专家，插入新的专家，删除patentForMatch中的项
        List<Expert> preExpertList =  patentDao.getExpertPreMatch();
        //拿到现有的专家
        List<Expert> expertList = expertDao.getExpertList();

        //去掉已经存在的，得到新的插入
        preExpertList = matchUtil.removeAll(preExpertList,expertList);
        //preExpertList.removeAll(expertList);
        //去重

        HashSet<Expert> expertSet = new HashSet<>(preExpertList);
        preExpertList.clear();
        preExpertList.addAll(expertSet);
        expertDao.insertNewExpert(preExpertList);
        //插入新的专家之后将新的专家和专利对上
        patentDao.insertNewPatent2Expert();
        patentDao.deleteItemInPatentForMatch();
    }
    @Transactional
    public void postMatchPatent(){
        patentDao.insertNewEnterpriseFromPatentForMatch();
        patentDao.insertNewCompanyAlias();
        preMatchPatent();
    }


    /**
     * 新进来的奖项，要同时抽出内容插入prizetemp表中，然后这边执行抽取出待匹配专利机构
     * getPrizeForMatch() -> preMatchPrize() ->人工 —> postMatchPrize()
     */
    public void getPrizeForMatch(){
        prizeDao.getPrizeForMatch();
    }
    @Transactional
    public void preMatchPrize(){
        //将能对上的先对上，对上后将prizetemp 和 prizeformatch 中的数据删除
        prizeDao.insertNewPrize2Enterprise();
    }
    @Transactional
    public void postMatchPrize(){
        //将人工也对不上的企业插入EnterpriseInfo中
        prizeDao.insertNewEnterpriseFromPrizeForMatch();
        prizeDao.insertNewCompanyAlias();
        preMatchPrize();
    }


    //总的更新口
    @Transactional
    public void whenDataUpdate(){
        getPrizeForMatch();
        preMatchPrize();
        postMatchPrize();
        getPatentForMatchToTable();
        preMatchPatent();
        postMatchPatent();
        preMatchPaper();
        postMatchPaper();
    }

}
