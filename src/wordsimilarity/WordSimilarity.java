package wordsimilarity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * ����Ϊ����Ŀ����Ҫ�ļ����ṩ����������ƶȵ�һЩ������ʽ����Ϊ��̬��
 * �����̰߳�ȫ�����Զ��̵߳��á�
 * �����㷨�ο����ģ� �����ڣ�֪�����Ĵʻ��������ƶȼ��㡷����.pdf
 */
public class WordSimilarity {
    // �ʿ������еľ���ʣ�������ԭ
    private static Map<String, List<Word>> ALLWORDS = new HashMap<String, List<Word>>();
    /**
     * sim(p1,p2) = alpha/(d+alpha)
     */
    private static double alpha = 1.6;
    /**
     * ����ʵ�ʵ����ƶȣ�������������ԭȨ��
     */
    private static double beta1 = 0.5;
    /**
     * ����ʵ�ʵ����ƶȣ�������������ԭȨ��
     */
    private static double beta2 = 0.2;
    /**
     * ����ʵ�ʵ����ƶȣ���������ϵ��ԭȨ��
     */
    private static double beta3 = 0.17;
    /**
     * ����ʵ�ʵ����ƶȣ���������ϵ������ԭȨ��
     */
    private static double beta4 = 0.13;
    /**
     * ���������ԭ�����ƶ�һ�ɴ���Ϊһ���Ƚ�С�ĳ���. ����ʺ;���ʵ����ƶȣ������������ͬ����Ϊ1������Ϊ0.
     */
    private static double gamma = 0.2;
    /**
     * ����һ�ǿ�ֵ���ֵ�����ƶȶ���Ϊһ���Ƚ�С�ĳ���
     */
    private static double delta = 0.2;
    /**
     * �����޹���ԭ֮���Ĭ�Ͼ���
     */
    private static int DEFAULT_PRIMITIVE_DIS = 20;
    /**
     * ֪���е��߼�����
     */
    private static String LOGICAL_SYMBOL = ",~^";
    /**
     * ֪���еĹ�ϵ����
     */
    private static String RELATIONAL_SYMBOL = "#%$*+&@?!";
    /**
     * ֪���е�������ţ���ʣ�������
     */
    private static String SPECIAL_SYMBOL = "{";
    /**
     * Ĭ�ϼ����ļ�
     */
    static {
        loadGlossary();
    }

    /**
     * ���� glossay.dat �ļ�
     */
    public static void loadGlossary() {
        String line = null;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("dict/glossary.dat.utf8"));
            line = reader.readLine();
            while (line != null) {
                // parse the line
                // the line format is like this:
                // �������� N place|�ط�,capital|����,ProperName|ר,(the United Arab Emirates|����������������)
                line = line.trim().replaceAll("\\s+", " ");
                String[] strs = line.split(" ");
                String word = strs[0];
                String type = strs[1];
                // ��Ϊ�ǰ��ո񻮷֣����һ���ֵļӻ�ȥ
                String related = strs[2];
                for (int i = 3; i < strs.length; i++) {
                    related += (" " + strs[i]);
                }
                // Create a new word
                Word w = new Word();
                w.setWord(word);
                w.setType(type);
                parseDetail(related, w);
                // save this word.
                addWord(w);
                // read the next line
                line = reader.readLine();
            }
        } catch (Exception e) {
            System.out.println("Error line: " + line);
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * �����������֣��������Ľ������<code>Word word</code>.
     * 
     * @param related
     */
    public static void parseDetail(String related, Word word) {
        // spilt by ","
        String[] parts = related.split(",");
        boolean isFirst = true;
        boolean isRelational = false;
        boolean isSimbol = false;
        String chinese = null;
        String relationalPrimitiveKey = null;
        String simbolKey = null;
        for (int i = 0; i < parts.length; i++) {
            // ����Ǿ���ʣ��������ſ�ʼ�ͽ�β: (Bahrain|����)
            if (parts[i].startsWith("(")) {
                parts[i] = parts[i].substring(1, parts[i].length() - 1);
                // parts[i] = parts[i].replaceAll("\\s+", "");
            }
            // ��ϵ��ԭ��֮��Ķ��ǹ�ϵ��ԭ
            if (parts[i].contains("=")) {
                isRelational = true;
                // format: content=fact|����
                String[] strs = parts[i].split("=");
                relationalPrimitiveKey = strs[0];
                String value = strs[1].split("\\|")[1];
                word.addRelationalPrimitive(relationalPrimitiveKey, value);

                continue;
            }
            String[] strs = parts[i].split("\\|");
            // ��ʼ�ĵ�һ���ַ���ȷ���Ƿ�Ϊ��ԭ������������ϵ��
            int type = getPrimitiveType(strs[0]);
            // �������Ĳ��ֵĴ���,�������û�����Ľ���
            if (strs.length > 1) {
                chinese = strs[1];
            }
            if (chinese != null
                    && (chinese.endsWith(")") || chinese.endsWith("}"))) {
                chinese = chinese.substring(0, chinese.length() - 1);
            }
            // ��ԭ
            if (type == 0) {
                // ֮ǰ��һ����ϵ��ԭ
                if (isRelational) {
                    word
                            .addRelationalPrimitive(relationalPrimitiveKey,
                                    chinese);
                    continue;
                }
                // ֮ǰ��һ���Ƿ�����ԭ
                if (isSimbol) {
                    word.addRelationSimbolPrimitive(simbolKey, chinese);
                    continue;
                }
                if (isFirst) {
                    word.setFirstPrimitive(chinese);
                    isFirst = false;
                    continue;
                } else {
                    word.addOtherPrimitive(chinese);
                    continue;
                }
            }
            // ��ϵ���ű�
            if (type == 1) {
                isSimbol = true;
                isRelational = false;
                simbolKey = Character.toString(strs[0].charAt(0));
                word.addRelationSimbolPrimitive(simbolKey, chinese);
                continue;
            }
            if (type == 2) {
                // ���
                if (strs[0].startsWith("{")) {
                    // ȥ����ʼ��һ���ַ� "{"
                    String english = strs[0].substring(1);
                    // ȥ���а벿�� "}"
                    if (chinese != null) {
                        word.addStructruralWord(chinese);
                        continue;
                    } else {
                        // ���û�����Ĳ��֣���ʹ��Ӣ�Ĵ�
                        word.addStructruralWord(english);
                        continue;
                    }
                }
            }
        }
    }

    /**
     * <p>
     * ��Ӣ�Ĳ���ȷ�������ԭ�����
     * </p>
     * <p>
     * 0-----Primitive<br/> 1-----Relational<br/> 2-----Special
     * </p>
     * 
     * @param english
     * @return һ������������������ֵΪ1��2��3��
     */
    public static int getPrimitiveType(String str) {
        String first = Character.toString(str.charAt(0));
        if (RELATIONAL_SYMBOL.contains(first)) {
            return 1;
        }
        if (SPECIAL_SYMBOL.contains(first)) {
            return 2;
        }
        return 0;
    }

    /**
     * ����������������ƶ�
     */
    public static double simWord(String word1, String word2) {
        if (ALLWORDS.containsKey(word1) && ALLWORDS.containsKey(word2)) {
            List<Word> list1 = ALLWORDS.get(word1);
            List<Word> list2 = ALLWORDS.get(word2);
            double max = 0;
            for (Word w1 : list1) {
                for (Word w2 : list2) {
                    double sim = simWord(w1, w2);
                    max = (sim > max) ? sim : max;
                }
            }
            return max;
        }
        //System.out.println("�����д�û�б���¼");
        return 0.0;
    }
    
    public static boolean hasWord(String word) {
        if (ALLWORDS.containsKey(word)) {
           
            return true;
        }
        //System.out.println("�����д�û�б���¼");
        return false;
    }

    /**
     * ����������������ƶ�
     * @param w1
     * @param w2
     * @return
     */
    public static double simWord(Word w1, Word w2) {
        // ��ʺ�ʵ�ʵ����ƶ�Ϊ��
        if (w1.isStructruralWord() != w2.isStructruralWord()) {
            return 0;
        }
        // ���
        if (w1.isStructruralWord() && w2.isStructruralWord()) {
            List<String> list1 = w1.getStructruralWords();
            List<String> list2 = w2.getStructruralWords();
            return simList(list1, list2);
        }
        // ʵ��
        if (!w1.isStructruralWord() && !w2.isStructruralWord()) {
            // ʵ�ʵ����ƶȷ�Ϊ4������
            // ������ԭ���ƶ�
            String firstPrimitive1 = w1.getFirstPrimitive();
            String firstPrimitive2 = w2.getFirstPrimitive();
            double sim1 = simPrimitive(firstPrimitive1, firstPrimitive2);
            // ����������ԭ���ƶ�
            List<String> list1 = w1.getOtherPrimitives();
            List<String> list2 = w2.getOtherPrimitives();
            double sim2 = simList(list1, list2);
            // ��ϵ��ԭ���ƶ�
            Map<String, List<String>> map1 = w1.getRelationalPrimitives();
            Map<String, List<String>> map2 = w2.getRelationalPrimitives();
            double sim3 = simMap(map1, map2);
            // ��ϵ�������ƶ�
            map1 = w1.getRelationSimbolPrimitives();
            map2 = w2.getRelationSimbolPrimitives();
            double sim4 = simMap(map1, map2);
            double product = sim1;
            double sum = beta1 * product;
            product *= sim2;
            sum += beta2 * product;
            product *= sim3;
            sum += beta3 * product;
            product *= sim4;
            sum += beta4 * product;
            return sum;
        }
        return 0.0;
    }

    /**
     * map�����ƶȡ�
     * 
     * @param map1
     * @param map2
     * @return
     */
    public static double simMap(Map<String, List<String>> map1,
            Map<String, List<String>> map2) {
        if (map1.isEmpty() && map2.isEmpty()) {
            return 1;
        }
        int total =map1.size() + map2.size();
        double sim = 0;
        int count = 0;
        for (String key : map1.keySet()) {
            if (map2.containsKey(key)) {
                List<String> list1 = map1.get(key);
                List<String> list2 = map2.get(key);
                sim += simList(list1, list2);
                count++;
            }
        }
        return (sim + delta * (total-2*count))
                / (total-count);
    }

    /**
     * �Ƚ��������ϵ����ƶ�
     * 
     * @param list1
     * @param list2
     * @return
     */
    public static double simList(List<String> list1, List<String> list2) {
        if (list1.isEmpty() && list2.isEmpty())
            return 1;
        int m = list1.size();
        int n = list2.size();
        int big = m > n ? m : n;
        int N = (m < n) ? m : n;
        int count = 0;
        int index1 = 0, index2 = 0;
        double sum = 0;
        double max = 0;
        while (count < N) {
            max = 0;
            for (int i = 0; i < list1.size(); i++) {
                for (int j = 0; j < list2.size(); j++) {
                    double sim = innerSimWord(list1.get(i), list2.get(j));
                    if (sim > max) {
                        index1 = i;
                        index2 = j;
                        max = sim;
                    }
                }
            }
            sum += max;
            if(index1 < list1.size())list1.remove(index1);
            if(index2 < list2.size())list2.remove(index2);
            count++;
        }
        return (sum + delta * (big - N)) / big;
    }

    /**
     * �ڲ��Ƚ������ʣ�������Ϊ����ʣ�Ҳ��������ԭ
     * 
     * @param word1
     * @param word2
     * @return
     */
    private static double innerSimWord(String word1, String word2) {
        boolean isPrimitive1 = Primitive.isPrimitive(word1);
        boolean isPrimitive2 = Primitive.isPrimitive(word2);
        // ������ԭ
        if (isPrimitive1 && isPrimitive2)
            return simPrimitive(word1, word2);
        // �����
        if (!isPrimitive1 && !isPrimitive2) {
            if (word1.equals(word2))
                return 1;
            else
                return 0;
        }
        // ��ԭ�;���ʵ����ƶ�, Ĭ��Ϊgamma=0.2
        return gamma;
    }

    /**
     * @param primitive1
     * @param primitive2
     * @return
     */
    public static double simPrimitive(String primitive1, String primitive2) {
        int dis = disPrimitive(primitive1, primitive2);
        return alpha / (dis + alpha);
    }

    /**
     * ����������ԭ֮��ľ��룬���������ԭ���û�й�ͬ�ڵ㣬���������ǵľ���Ϊ20��
     * 
     * @param primitive1
     * @param primitive2
     * @return
     */
    public static int disPrimitive(String primitive1, String primitive2) {
        List<Integer> list1 = Primitive.getParents(primitive1);
        List<Integer> list2 = Primitive.getParents(primitive2);
        for (int i = 0; i < list1.size(); i++) {
            int id1 = list1.get(i);
            if (list2.contains(id1)) {
                int index = list2.indexOf(id1);
                return index + i;
            }
        }
        return DEFAULT_PRIMITIVE_DIS;
    }

    /**
     * ����һ������
     * 
     * @param word
     */
    public static void addWord(Word word) {
        List<Word> list = ALLWORDS.get(word.getWord());

        if (list == null) {
            list = new ArrayList<Word>();
            list.add(word);
            ALLWORDS.put(word.getWord(), list);
        } else {
            list.add(word);
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub
    	FileInputStream fis = new FileInputStream("dict/WHOLE.DAT"); 
        InputStreamReader isr = new InputStreamReader(fis, "GBK"); 
        BufferedReader reader = new BufferedReader(isr); 
        
        FileOutputStream fos = new FileOutputStream("dict/WHOLE.DAT.utf8"); 
        OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8"); 
        //BufferedReader reader = new BufferedReader(new FileReader(
         //       "dict/glossary.dat"));
        Set<String> set = new HashSet<String>();
        String line = reader.readLine();
        while (line != null) {
            //System.out.println(line);
            //line = line.replaceAll("\\s+", " ");
            //String[] strs = line.split(" ");
            //for (int i = 0; i < strs.length; i++) {
               // System.out.print(" " + strs[i]);
            //}
            //System.out.println();
            //set.add(strs[1]);
        	osw.write(line + "\n");
            line = reader.readLine();
        }
        osw.flush();
        osw.close();
        //System.out.println(set.size());
        for (String name : set) {
            System.out.println(name);
        }
        String x = "人";
        String y = "狗";
        String strx=new String(x.getBytes("utf-8"),"latin1");
        String stry=new String(y.getBytes("utf-8"),"latin1");
        System.out.println(strx);
        double simval=simWord(strx, stry);
        System.out.println(simval);
    }
}
