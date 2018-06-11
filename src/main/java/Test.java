
import com.tech.analysis.Dao.MatchDao;
import com.tech.analysis.entity.AddressTemp;
import com.tech.analysis.util.MatchUtil;

import java.text.DateFormat;
import java.util.*;

/**
 * Created by Administrator on 2018/3/24 0024.
 */
public class Test {

    public static int[] getNums(String s1,String s2){
        HashMap<Character,Integer> mapS1 = new HashMap<>();
        HashMap<Character,Integer> mapS2 = new HashMap<>();
        for(char c : s1.toCharArray()){
            mapS1.put(c,mapS1.getOrDefault(c,0)+1);
        }
        for(char c : s2.toCharArray()){
            mapS2.put(c,mapS2.getOrDefault(c,0)+1);
        }
        int num1 = 0;
        int num2 = 0;
        for(int i = 0;i < 26;i++){

            int low1 = mapS1.getOrDefault((char)('a'+i),0);
            int low2 = mapS2.getOrDefault((char)('a'+i),0);
            num1 += Math.min(low1,low2);
            mapS2.put((char)('a'+i),low2-low1);


            int up1 = mapS1.getOrDefault((char)('A'+i),0);
            int up2 = mapS2.getOrDefault((char)('A'+i),0);
            num1 += Math.min(up1,up2);
            mapS2.put((char)('A'+i),up2-up1);

            int low22 = mapS2.get((char)('a'+i));
            int up22 = mapS2.get((char)('A'+i));
            if(low22*up22<0){
                num2 += Math.max(Math.abs(low22),Math.abs(up22));
            }
        }
        int[] res = {num1,num2};
        return res;
    }

    public static int[][] reconstructQueue(int[][] people){
        Arrays.sort(people, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                return o1[0] == o2[0] ? o1[1] - o2[1] : o2[0] - o1[0];
            }
        });
        int[] a = {1,2,5,4,3};
        return people;
}



    public static void main(String[] args) {
        char[] t = new char[]{'a','b','c','d','e','f','g','h','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};

        //System.out.println(nextGreatestLetter(t,'y'));

    }
}
