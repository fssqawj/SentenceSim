package wordsimilarity;

import java.util.List;

public class PrimitiveTests {
    // public void main(String args[]) {
    	// test_getParents();
    // }
    /**
     * test the method {@link Primitive#getParents(String)}.
     */
    public void test_getParents(){
        String primitive = "¹¥´ò";
        List<Integer> list = Primitive.getParents(primitive);
        for(Integer i : list){
            System.out.println(i);
        }
    }
}
