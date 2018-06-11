package com.tech.analysis.util;

import com.tech.analysis.entity.Enterprise;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2018/3/22 0022.
 */
@Component
public class MatchUtil {

    public static boolean isChinese(String str){

        String regEx = "[\\u4e00-\\u9fa5]+";

        Pattern p = Pattern.compile(regEx);

        Matcher m = p.matcher(str);

        if(m.find())

            return true;

        else

            return false;

    }

    public int editDistance(String name1,String name2){
        /**
         * 根据编辑距离算法，计算两个字符串的相似度
         * */
        int m = name1.length();
        int n = name2.length();

        int[][] cost = new int[m + 1][n + 1];
        for(int i = 0; i <= m; i++)
            cost[i][0] = i;
        for(int i = 1; i <= n; i++)
            cost[0][i] = i;

        for(int i = 0; i < m; i++) {
            for(int j = 0; j < n; j++) {
                if(name1.charAt(i) == name2.charAt(j))
                    cost[i + 1][j + 1] = cost[i][j];
                else {
                    int a = cost[i][j] + 2;
                    int b = cost[i][j + 1] + 1;
                    int c = cost[i + 1][j] + 1;
                    cost[i + 1][j + 1] = a < b ? (a < c ? a : c) : (b < c ? b : c);
                }
            }
        }
        return cost[m][n];
    }

    public int longestCommonSubString(String s1, String s2){
        if(s1.length() == 0 || s2.length() == 0)
            return 0;
        char[] str1 = s1.toCharArray();
        char[] str2 = s2.toCharArray();
        int[][] results = new int[str2.length][str1.length];
        //最大长度
        int maxLength = 0;
        for(int i=0;i<str1.length;i++){
            results[0][i] = (str2[0] == str1[i] ? 1 : 0);
            for(int j=1;j<str2.length;j++){
                results[j][0] = (str1[0] == str2[j] ? 1 : 0);
                if(i>0 && j>0){
                    if(str1[i] == str2[j]){
                        results[j][i] = results[j-1][i-1] + 1;
                    }
                }
                if(maxLength < results[j][i]){
                    maxLength = results[j][i];
                }
            }
        }
        return maxLength;
    }

    public int longestCommonSubsequence(String A, String B) {
        int n = A.length();
        int m = B.length();
        int f[][] = new int[n + 1][m + 1];
        for(int i = 1; i <= n; i++){
            for(int j = 1; j <= m; j++){
                f[i][j] = Math.max(f[i - 1][j], f[i][j - 1]);
                if(A.charAt(i - 1) == B.charAt(j - 1))
                    f[i][j] = f[i - 1][j - 1] + 1;
            }
        }
        return f[n][m];
    }


    /**
     * @param enterpriseName 将企业名去除公有字符，数字替换为汉字，获得关键字符
     * @return
     */
    public String clearEnterpriseName(String enterpriseName){
        enterpriseName = enterpriseName.replace("中国","");
        enterpriseName = enterpriseName.replace("第","");
        enterpriseName = enterpriseName.replace("公司","");
        enterpriseName = enterpriseName.replace("研究","");
        enterpriseName = enterpriseName.replace("所","");
        enterpriseName = enterpriseName.replace("中心","");
        enterpriseName = enterpriseName.replace("电科","电子科技");
        String num = getNumbers(enterpriseName);
        if(num.length()!=0){
            enterpriseName = enterpriseName.replace(num,toChinese(num));
        }
        //防止三零三所，303所情况
        enterpriseName = enterpriseName.replace("百","");
        enterpriseName = enterpriseName.replace("十","");
        return enterpriseName;
    }

    public boolean isEqualNumber(String name1,String name2){
        int index1 = name1.indexOf("十");
        int index2 = name2.indexOf("十");
        String a = "";
        String b = "";

        String c = "";
        String d = "";
        if(index1!=-1 && index2!=-1){
            a = String.valueOf(name1.charAt(index1+1));
            b = String.valueOf(name2.charAt(index2+1));
            if(index1>0&&index2>0){
                c = String.valueOf(name1.charAt(index1-1));
                d = String.valueOf(name2.charAt(index2-1));
            }
            return a.equals(b)&&c.equals(d);
        }
        return true;
    }

    /**
     * @param enterpriseName 执行到这里时，一定是中文的机构名
     * @param enterpriseList 在enterpriseList中找与enterpriseName相似的名称，放在列表中返回
     * @return 企业实体列表
     */
    public List<Enterprise> getSimEnterpriseList(String enterpriseName, List<Enterprise> enterpriseList){

        List<Enterprise> simEnterpriseList = new ArrayList<>();
        for (Enterprise enterprise: enterpriseList) {
            //对每个企业，与给定企业名称进行匹配
            //可以进行一些粗加工，把字符串转换一下，使得匹配更精准
            if(!isEqualNumber(enterpriseName,enterprise.getName()))
                continue;
            String clearotherName = clearEnterpriseName(enterprise.getName());
            String clearName = clearEnterpriseName(enterpriseName);
            if(editDistance(clearName,clearotherName)<4&&longestCommonSubsequence(clearName,clearotherName)>=5)
                simEnterpriseList.add(enterprise);
        }
        return  simEnterpriseList;
    }

    /**
     * @param content 找到字符串中第一个数字字符串
     * @return
     */
    public String getNumbers(String content) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }

    /**
     * @param string 将一个数字字符串转换成汉字字符串
     * @return  15 -> 十五 301 -> 三百零一
     */
    public String toChinese(String string) {
        String[] s1 = { "零", "一", "二", "三", "四", "五", "六", "七", "八", "九" };
        String[] s2 = { "十", "百", "千", "万", "十", "百", "千", "亿", "十", "百", "千" };
        String result = "";
        int n = string.length();
        for (int i = 0; i < n; i++) {
            int num = string.charAt(i) - '0';
            if (i != n - 1 && num != 0) {
                result += s1[num] + s2[n - 2 - i];
            } else {
                result += s1[num];
            }
        }
        return result;
    }


    /**
     * @param englishName
     * @return  将英文名字翻译为中文名字
     */
    public String translationEnglishName(String englishName){
        String chinenseName = "";
        if(englishName.contains("China Electronics Technology")){
            Pattern p = Pattern.compile("\\d+");
            Matcher m = p.matcher(englishName);
            String num = "";
            if (m.find()) {
                num = toChinese(m.group(0));
            }
            chinenseName = "中国电子科技集团公司第"+num+"研究所";
        }
        return chinenseName;
    }

    public  List removeAll(List a,List b){
        LinkedList c = new LinkedList(a);//大集合用LinkedList
        HashSet s = new HashSet(b);//小集合用HashSet
        Iterator iter=c.iterator();
        while(iter.hasNext()){
            if(s.contains(iter.next())){
                iter.remove();
            }
        }
        return c;
    }
//    public static void main(String[] args){
//        String enterpriseName = "中国电科集团公司13所";
//        String enterpriseName2 = "电子科技集团公司第十三所";
//        MatchUtil util = new MatchUtil();
//        System.out.print(util.longestCommonSubsequence(enterpriseName,enterpriseName2));
//        enterpriseName = util.clearEnterpriseName(enterpriseName);
//        enterpriseName2 = util.clearEnterpriseName(enterpriseName2);
//        System.out.print(util.editDistance(enterpriseName,enterpriseName2));
//        System.out.print(enterpriseName);
//        System.out.print(enterpriseName2);
//    }
}
