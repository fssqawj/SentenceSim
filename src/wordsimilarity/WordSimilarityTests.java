package wordsimilarity;

public class WordSimilarityTests {
	public static void main(String[] args) {
		 test_loadGlossary();
		//test_disPrimitive();
		//test_simPrimitive();
		test_simWord();
	}

    public static void test_loadGlossary(){
        WordSimilarity.loadGlossary();
    }
    /**
     * test the method {@link WordSimilarity#disPrimitive(String, String)}.
     */
    public static void test_disPrimitive(){
        int dis = WordSimilarity.disPrimitive("����", "����");
        //System.out.println("���� and ���� dis : "+ dis);
    }
    
    public static void test_simPrimitive(){
        double simP = WordSimilarity.simPrimitive("����", "����");
        //System.out.println("���� and ���� sim : "+ simP);
    }
    public static void test_simWord(){
        String word1 = "猪";
        String word2 = "狗";
        double sim = WordSimilarity.simWord(word2, word1);
        System.out.println(sim);
    }
    
}
