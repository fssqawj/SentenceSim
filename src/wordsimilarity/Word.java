package wordsimilarity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 代表一个词
 */
public class Word {
    private String word;
    private String type;

    /**
     * 第一基本义原。
     */
    private String firstPrimitive;

    /**
     * 其他基本义原。
     */
    private List<String> otherPrimitives = new ArrayList<String>();

    /**
     * 如果该list非空，则该词是一个虚词。 列表里存放的是该虚词的一个义原，部分虚词无中文虚词解释
     */
    private List<String> structruralWords = new ArrayList<String>();

    /**
     * 该词的关系义原。key: 关系义原。 value： 基本义原|(具体词)的一个列表
     */
    private Map<String, List<String>> relationalPrimitives = new HashMap<String, List<String>>();

    /**
     * 该词的关系符号义原。Key: 关系符号。 value: 属于该挂系符号的一组基本义原|(具体词)
     */
    private Map<String, List<String>> relationSimbolPrimitives = new HashMap<String, List<String>>();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getWord() {
        return word;
    }
    /**
     * 是否为虚词
     * @return
     */
    public boolean isStructruralWord(){
        return !structruralWords.isEmpty();
    }

    /**
     * DOCUMENT ME!
     *
     * @param word
     *            DOCUMENT ME!
     */
    public void setWord(String word) {
        this.word = word;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getType() {
        return type;
    }

    /**
     * DOCUMENT ME!
     *
     * @param type
     *            DOCUMENT ME!
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getFirstPrimitive() {
        return firstPrimitive;
    }

    /**
     * DOCUMENT ME!
     *
     * @param firstPrimitive
     *            DOCUMENT ME!
     */
    public void setFirstPrimitive(String firstPrimitive) {
        this.firstPrimitive = firstPrimitive;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public List<String> getOtherPrimitives() {
        return otherPrimitives;
    }

    /**
     * DOCUMENT ME!
     *
     * @param otherPrimitives
     *            DOCUMENT ME!
     */
    public void setOtherPrimitives(List<String> otherPrimitives) {
        this.otherPrimitives = otherPrimitives;
    }

    /**
     * DOCUMENT ME!
     *
     * @param otherPrimitive
     *            DOCUMENT ME!
     */
    public void addOtherPrimitive(String otherPrimitive) {
        this.otherPrimitives.add(otherPrimitive);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public List<String> getStructruralWords() {
        return structruralWords;
    }

    /**
     * DOCUMENT ME!
     *
     * @param structruralWords
     *            DOCUMENT ME!
     */
    public void setStructruralWords(List<String> structruralWords) {
        this.structruralWords = structruralWords;
    }

    /**
     * DOCUMENT ME!
     *
     * @param structruralWord
     *            DOCUMENT ME!
     */
    public void addStructruralWord(String structruralWord) {
        this.structruralWords.add(structruralWord);
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    public void addRelationalPrimitive(String key, String value) {
        List<String> list = relationalPrimitives.get(key);

        if (list == null) {
            list = new ArrayList<String>();
            list.add(value);
            relationalPrimitives.put(key, list);
        } else {
            list.add(value);
        }
    }
    public void addRelationSimbolPrimitive(String key,String value){
        List<String> list = relationSimbolPrimitives.get(key);

        if (list == null) {
            list = new ArrayList<String>();
            list.add(value);
            relationSimbolPrimitives.put(key, list);
        } else {
            list.add(value);
        }
    }
    public Map<String, List<String>> getRelationalPrimitives() {
        return relationalPrimitives;
    }
    public Map<String, List<String>> getRelationSimbolPrimitives() {
        return relationSimbolPrimitives;
    }
}
