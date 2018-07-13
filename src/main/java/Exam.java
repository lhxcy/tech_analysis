import com.hankcs.hanlp.HanLP;
import com.tech.analysis.entity.Expert;
import com.tech.analysis.util.MatchUtil;
import org.json.JSONObject;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Math.pow;
import static java.lang.Thread.sleep;

/**
 * Created by Administrator on 2018/4/3 0003.
 */
public class Exam {

    private int a = 0;

    private static long factorial(int n) {
        return (n > 1) ? n * factorial(n - 1) : 1;
    }

    public static long combination(int n, int m) {
        return (n >= m) ? factorial(n) / factorial(n - m) / factorial(m) : 0;
    }

    public static int getLongestCommonSubString(String s1, String s2){
        if(s1.length() == 0||s2.length() == 0)return 0;
        int[][] dp = new int[s1.length()+1][s2.length()+1];
        for(int i = 0;i < dp[0].length;i++){
            dp[s1.length()][i] = 0;
        }
        for(int i = 0;i < dp.length;i++){
            dp[i][s2.length()] = 0;
        }
        int max = 0;
        for(int i = s1.length()-1;i >= 0;i--){
            for(int j = s2.length()-1;j >= 0;j--){
                if(s1.charAt(i) == s2.charAt(j)){
                    dp[i][j] = dp[i+1][j+1] + 1;
                    if(dp[i][j] > max)max = dp[i][j];
                }else
                    dp[i][j] = 0;
            }
        }
        return max;
    }

    public static int getLongestCommonSubSquence(String s1, String s2){
        int l1 = s1.length();
        int l2 = s2.length();
        int[][] dp = new int[l1+1][l2+1];
        for(int i = 1;i <= l1;i++){
            for(int j = 1;j <= l2;j++){
                dp[i][j] = Math.max(dp[i-1][j],dp[i][j-1]);
                if(s1.charAt(i-1) == s2.charAt(j-1)){
                    dp[i][j] = dp[i-1][j-1] + 1;
                }
            }
        }
        return dp[l1][l2];
    }


    public  void testThread() throws InterruptedException {
        CountDownLatch cd = new CountDownLatch(2);
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                a++;
                cd.countDown();
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                a++;
                a++;
                cd.countDown();
            }
        });
        t1.start();t2.start();
        cd.await();
    }
    public void tt(){
        a += 1;
    }

    static class Ticket implements Runnable {

        private int ticket = 10;

        public void run() {
            synchronized (Ticket.class){
                while (ticket > 0) {
                    ticket--;
                    try {
                        sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("当前票数为：" + ticket);
                }
            }
        }
    }

    public  void test(){
        Ticket t = new Ticket();
        new Thread(t).start();
        new Thread(t).start();
    }



    public static double findMedianSortedArrays(int[] A) {
        PriorityQueue<Integer> littleQueue = new PriorityQueue<>();
        PriorityQueue<Integer> bigQueue = new PriorityQueue<>(new Comparator<Integer>(){

            @Override
            public int compare(Integer i1,Integer i2){
                return i2 - i1;
            }

        });
        for(int i : A){
            if(bigQueue.size() == 0 || i < bigQueue.peek()){
                bigQueue.offer(i);
            }else{
                littleQueue.offer(i);
            }
            if(bigQueue.size() > littleQueue.size() + 1){
                littleQueue.offer(bigQueue.poll());
            }
            if(littleQueue.size() > bigQueue.size()){
                bigQueue.offer(littleQueue.poll());
            }
        }
        if(bigQueue.size() == littleQueue.size())return (double)(bigQueue.peek()+littleQueue.peek())/2;
        else return bigQueue.peek();
    }


    public static int getTenTh(int k){
        int i = 1;
        while(k>0){
            i = i * 10;
            k--;
        }
        return i;
    }

    public static int getNum(int k){
        int num = 0;
        while((k-1)>0){
            num *= 10;
            num += 9;
            k--;
        }
        return num;
    }

    public static int getByK(int k,int n){
        int sum = n - getNum(k);
        sum *= k;
//        while(k > 1){
//            k--;
//            sum = sum + k*getTenTh(k-1)*9;
//        }
        return sum;
    }

    public static int getK(int n){
        int k = 0;
        while(n > 0){
            k++;
            n /= 10;
        }
        return k;
    }

    public static int[] getAux(){
        int[] res = new int[11];
        res[0] = 0;
        res[1] = 9;
        for(int i = 2;i < 11;i++){
            res[i] = res[i-1] + getTenTh(i-1)*9*i;
        }
        return res;
    }

    public static char nextGreatestLetter(char[] letters, char target) {
        int start = 0;int end = letters.length-1;
        while(start <= end){
            int mid = start + (end - start)/2;
            if((letters[mid] - 'a' + 1)%26 <= (target - 'a' + 1)%26){
                start = mid + 1;
            }else{
                end = mid - 1;
            }
        }
        return start >= letters.length ? letters[0]:letters[start];
    }

    public class Interval {
      int start;
      int end;
      Interval() { start = 0; end = 0; }
      Interval(int s, int e) { start = s; end = e; }
  }
    public int[] findRightInterval(Interval[] intervals) {
        TreeMap<Integer,Integer> map = new TreeMap<>();
        int i = 0;
        for (Interval interval : intervals) {
            map.put(interval.start,i++);
        }
        int[] res = new int[intervals.length];
        for (Interval interval : intervals) {
            Map.Entry<Integer,Integer> entry = map.ceilingEntry(interval.end);
            res[i] = entry == null ? -1 : entry.getValue();
        }
        return res;
    }
    public static int findDuplicate(int[] nums) {
        int slow = 0;
        int fast = 0;
        slow = nums[slow];
        fast = nums[nums[fast]];
        while(slow!=fast){
            slow = nums[slow];
            fast = nums[nums[fast]];
        }
        fast = 0;
        while(slow!=fast){
            slow = nums[slow];
            fast = nums[fast];
        }
        return slow;
    }
    private static int getGreater(int[] arr,int target){
        int s = 0;int e = arr.length - 1;
        while(s <= e){
            int mid = s + (e - s) / 2;
            if(arr[mid] >= target){
                e = mid - 1;
            }else{
                s = mid + 1;
            }
        }
        if(s >= arr.length)return -1;
        return arr[s];
    }

    public static int jump(int[] nums) {
        int max = 0;
        int mmax = 0;
        int res = 0;
        for(int i = 0;max < nums.length-1;i++){
            if(i < max){
                mmax = Math.max(max,i+nums[i]);
            }else{
                mmax = Math.max(mmax,i+nums[i]);
                max = mmax;
                res++;
            }
        }
        return res;
    }

    public static String getPermutation(int n, int k){
        List<String> number = new ArrayList<>();
        for (int i = 1; i <= n ; i++) {
            number.add(String.valueOf(i));
        }
        int[] fac = new int[n + 1];
        fac[0] = 1;
        for (int i = 1; i <= n; i++) {
            fac[i] = i*fac[i-1];
        }
        String res = "";
        for (int i = n; i >= 1; i--) {
            res += number.get(k/fac[i-1]);
            number.remove(k/fac[i-1]);
            k = k%fac[i-1];
        }
        return res;
    }

    public static String getPermutation2(int n, int k) {
        List<Integer> numbers = new ArrayList<>();
        int[] factorial = new int[n+1];
        StringBuilder sb = new StringBuilder();

        // create an array of factorial lookup
        int sum = 1;
        factorial[0] = 1;
        for(int i=1; i<=n; i++){
            sum *= i;
            factorial[i] = sum;
        }
        // factorial[] = {1, 1, 2, 6, 24, ... n!}

        // create a list of numbers to get indices
        for(int i=1; i<=n; i++){
            numbers.add(i);
        }
        // numbers = {1, 2, 3, 4}

        k--;

        for(int i = 1; i <= n; i++){
            int index = k/factorial[n-i];
            sb.append(String.valueOf(numbers.get(index)));
            numbers.remove(index);
            k-=index*factorial[n-i];
        }

        return String.valueOf(sb);
    }
    public class TreeNode {
      int val;
      TreeNode left;
      TreeNode right;
      TreeNode(int x) { val = x; }
    }
    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        if(root == null)return list;
        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);
        while(!stack.isEmpty()){
            while(stack.peek().left!=null){
                stack.push(stack.peek().left);
            }
            TreeNode r = stack.pop();
            list.add(r.val);
            if (r.right!=null)stack.push(r.right);
        }
        Queue<TreeNode> queue = new LinkedList<>();
        //Collections.reverse(list);
        return list;
    }

    public static boolean isP(String s, int i, int j){
        while(i<j){
            if(s.charAt(i)!=s.charAt(j))return false;
            i++;j--;
        }
        return true;
    }

    public static void backtrack(List<List<String>> res, List<String> cur, String s,int l){
        if(l == s.length()){
            res.add(new ArrayList<>(cur));
            return;
        }
        for(int i = l;i < s.length();i++){
            if(isP(s,l,i)){
                cur.add(s.substring(l,i+1));
                backtrack(res,cur,s,i+1);
                cur.remove(cur.size()-1);
            }
        }
    }


    public static List<List<String>> partition(String s) {
        List<List<String>> res = new ArrayList<>();
        List<String> cur = new ArrayList<>();
        backtrack(res,cur,s,0);
        return res;
    }

    public static void main(String[] args) {
        List<Integer> l = new ArrayList<>();
        l.add(1);
        l.clear();
        l.add(2);


//        Map<Integer,Integer> map = new HashMap<>();
//        map.put(1,2);
//        map.put(3,4);
//        Iterator<Map.Entry<Integer,Integer>> iterator = map.entrySet().iterator();
//        while (iterator.hasNext()){
//            Map.Entry<Integer,Integer> entry = iterator.next();
//            iterator.remove();
//        }
//
//        try{
//            while (true){
//                System.out.print("?");
//            }
//        }catch(Exception e){
//
//        }finally{
//            System.out.println("I can get there!!!!!!!!!!!!!!!!!!!!!!!!");
//        }
//        Pattern p = Pattern.compile("\\d+");
//        Matcher m = p.matcher("Analog IC Design Department, 24th Research Institute of China Electronics Technology Group Corporation");
//        if (m.find()) {
//            System.out.println(m.group(0));
//        }
        String sql = "";
        String authors = "郭东明(大连理工大学)，贾振元(大连理工大学)，康仁科(大连理工大学)，王永青(大连理工大学)，盛贤君(大连理工大学)，余慧龙(航天科工集团二院25所)";
        String[] author = authors.split("，");
        for (String a : author) {
            System.out.println(a);
            String[] authorAndEnterprise = a.split("\\(");
            sql += String.format("('%s','%s'),",authorAndEnterprise[0],authorAndEnterprise[1].substring(0,authorAndEnterprise[1].length()-1));
        }
        System.out.print(sql);
        String a = "eEe";
        String b = "EeE";
        Expert e1 = new Expert();
        e1.setEnterpriseId("1");
        e1.setEnterpriseName("The");
        e1.setName("wo");
        Expert e2 = new Expert();
        e2.setEnterpriseId("1");
        e2.setEnterpriseName("The");
        e2.setName("wo");
        System.out.println(a.toLowerCase().equals(b.toLowerCase()));
        System.out.println(String.format("%%%s%%",a));
        System.out.println("十三香挥发油, GC-MS分析, 自动质谱解卷积定性系统, 色谱保留指数".split(",")[3]);
        System.out.println(HanLP.extractKeyword("为防御椭圆曲线密码系统的侧信道攻击,针对椭圆曲线密码系统的侧信道攻击主要集中在对标量乘运算的攻击,提出了基于Width-wNAF的改进算法RWNAF(refinedWidth-wNAF)和FWNAF(fractionalWidth-wNAF),通过Masking技术隐藏密码算法的真实能量消耗信息,能有效地防御SPA、DPA、RPA与ZPA攻击;通过对密钥d的奇偶性分析,对预计算表进行优化,减少了存储需求和计算开销。FWNAF进一步利用碎片窗口技术,提高了存储资源的利用效率,同时也减少了由于系统资源急剧变化而引发的系统计算性能的抖动现象。",6));
//["椭圆", "系统", "计算", "信道攻击", "密码", "攻击"]  [信道攻击, 偶性, 减少存储, 减少系统, 利用效率, 利用碎片]
    }

    public void backTrack(List<List<Integer>> tri,List<Integer> res,int ceng,int index,int sum){
        if(ceng == tri.size())res.add(sum);
        backTrack(tri, res, ceng+1, index, sum + tri.get(ceng).get(index));
        backTrack(tri, res, ceng+1, index+1, sum + tri.get(ceng).get(index));
    }
    public static int minimumTotal(List<List<Integer>> triangle) {
        if(triangle == null)return 0;
        for(int i = triangle.size() - 2;i>=0;i--){
            for(int j = 0;j < triangle.get(i).size();j++){
                triangle.get(i).set(j,Math.min(triangle.get(i+1).get(j),triangle.get(i+1).get(j+1)));
            }
        }
        return triangle.get(0).get(0);
    }

}


