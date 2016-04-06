package ica.fssqawj;

import java.util.ArrayList;
import java.util.List;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;


public class SentenceSeg {
	private static String Sentence;
	private static ArrayList<String> iRes = new ArrayList<String>();
	public SentenceSeg(String sentence){
		Sentence = sentence;
		List<Term> iTerm = ToAnalysis.parse(Sentence);
		for(Term key : iTerm){
            String tem = key.toString();
            String[] ary = tem.split("/");
            if(ary.length > 0)iRes.add(ary[0]);
        }
	}
	public static String getSentence() {
		return Sentence;
	}
	public static void setSentence(String sentence) {
		Sentence = sentence;
	}
	public static ArrayList<String> getiRes() {
		return iRes;
	}
	public static void setiRes(ArrayList<String> iRes) {
		SentenceSeg.iRes = iRes;
	}
	
}
