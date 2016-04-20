package ica.fssqawj;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;

import wordsimilarity.WordSimilarity;

public class CommonFunction {
	public static Map< String, ArrayList<Double> > iMap = new HashMap<String, ArrayList<Double> >();
	public static String binFile = null;
	public static String sdpFile = null;
	public static Map<String, String> qdep = new HashMap<String, String>();
	public static int notincnt = 0;
	public static Set<String> sdpkey = new HashSet<>();
	public static double wordSimVal(String wordx, String wordy){
        if(wordx.equals(wordy)){
        	//System.out.println("here");
        	return 1;
        }
        return WordSimilarity.simWord(wordx, wordy);
        //return 0;
    }
    public static double SentenceSimHownetVal(String senx,String seny){
        List<String> senxTerm = getTerm(senx);
        List<String> senyTerm = getTerm(seny);
        int lenx = senxTerm.size();
        int leny = senyTerm.size();
        double res = 0;
        for(String iKey : senxTerm){
            for(String jKey : senyTerm){
                res += wordSimVal(iKey, jKey);
            }
        }
        return res / (lenx * leny);
    }

    public static double SentenceSimOnehotVal(String senx, String seny){
        List<String> senxTerm = getTerm(senx);
        List<String> senyTerm = getTerm(seny);
        int lenx = senxTerm.size();
        int leny = senyTerm.size();
        double res = 0;
        for(String iKey : senxTerm){
            for(String jKey : senyTerm){
                if(iKey.equals(jKey))res += 1;
            }
        }
        return res / Math.sqrt(lenx * leny);
    }

    public static void Initword2vec(Set<String> word) throws NumberFormatException, IOException{
    	System.out.println("Init word2vec... ...");
    	BufferedReader bufferedReader = new BufferedReader(new FileReader(binFile));
		boolean f = false;
		String temp = "";
		int in = 0;
		while((temp = bufferedReader.readLine()) != null){
			if(f){
				String[] tem = temp.split(" ");
				if(word.contains(tem[0])){
					ArrayList<Double> t = new ArrayList<Double>();
					for(int i = 1;i < tem.length;i ++){
						t.add(Double.parseDouble(tem[i]));
					}
					iMap.put(tem[0], t);
					in ++;
				}
			}
			f = true;
			
		}
		bufferedReader.close();
		System.out.println(in + " / " + word.size() + " has vector.");
		System.out.println("Init word2vec done.");
    }
    
    public static void Initsdp() throws IOException{
    	System.out.println("Init sdp... ...");
    	BufferedReader bufferedReader = new BufferedReader(new FileReader(sdpFile));
		
		String depL = ""; 
		
		String tq = "";
		
		String temp = "";
		qdep.clear();
		
		while((temp = bufferedReader.readLine()) != null){
			if(temp.contains("http")){
				if(tq != ""){
					qdep.put(tq, depL);
				}
				tq = temp.substring(temp.lastIndexOf('=') + 1, temp.length() - 1);
				depL = "";
			}
			else {
				depL += temp + "\t###\t";
			}
		}
		bufferedReader.close();
		System.out.println("Init sdp done.");
    }
    
    public static double cos(List<Double> x, List<Double> y){
		double res = 0;
		double resx = 0;
		double resy = 0;
		//System.out.println(x);
		for(int i = 0;i < x.size();i ++){
			res += x.get(i) * y.get(i);
			resx += x.get(i) * x.get(i);
			resy += y.get(i) * y.get(i);
		}
		return res / (Math.sqrt(resx) * Math.sqrt(resy));
	}
	
	public static double sentenceSimWord2vecVal(String x, String y){
		List<String> xTerm = getTerm(x);
		List<String> yTerm = getTerm(y);
		double res = 0.0;
		for(String xkey : xTerm){
			for(String ykey : yTerm){
				if(xkey.equals(ykey))res += 1;
				else {
					if(!iMap.containsKey(ykey) || !iMap.containsKey(xkey)){
						continue;
					}
					double t = cos(iMap.get(xkey), iMap.get(ykey));
					if(t >= 0.8)res += t;
				}
			}
		}
		return res / Math.sqrt(xTerm.size() * yTerm.size());
	}
	
    public static List<String> getTerm(String str){
        List<String> res = new ArrayList<String>();
        List<Term> iTerm = ToAnalysis.parse(str);
        for(Term key : iTerm){
            String tem = key.toString();
            String[] ary = tem.split("/");
            if(ary.length > 1){
            //if(ary.length > 1){
            //System.out.println(ary[0]);
            	//if(ary[1].contains("mw"))iRes.add(ary[0] + "mw");
            	res.add(ary[0]);
            }
        }
        return res;
    }
    
    public static double sentenceSimSdpVal(String x, String y){
		
		if(!qdep.containsKey(x) || !qdep.containsKey(y)){
			System.out.println(notincnt + " Sentence Not In!");
			if(!qdep.containsKey(x))System.out.println("not found : " + x);
			if(!qdep.containsKey(y))System.out.println("not found : " + y);
			return 0;
		}
		String[] xL = qdep.get(x).split("\t###\t");
		String[] yL = qdep.get(y).split("\t###\t");
		
		
		
		double res = 0.0;
		
		for(String xKey : xL){
			for(String yKey : yL){
				res += _calc(xKey, yKey);
			}
		}
		return res / Math.max(xL.length, yL.length);
		
	}
    
    public static double sentenceSimWord2vecSdpVal(String x, String y){
		
		if(!qdep.containsKey(x) || !qdep.containsKey(y)){
			System.out.println(notincnt + " Sentence Not In!");
			if(!qdep.containsKey(x))System.out.println("not found : " + x);
			if(!qdep.containsKey(y))System.out.println("not found : " + y);
			return 0;
		}
		String[] xL = qdep.get(x).split("\t###\t");
		String[] yL = qdep.get(y).split("\t###\t");
		
		
		
		double res = 0.0;
		
		for(String xKey : xL){
			for(String yKey : yL){
				res += calc(xKey, yKey);
			}
		}
		return res / Math.max(xL.length, yL.length);
		
	}
	
    public static double _calc(String x, String y){
		String[] xs = x.split(" ");
		String[] ys = y.split(" ");
		if(xs.length < 3 || ys.length < 3){
			//System.out.println(x);
			//System.out.println(y);
			return 0;
		}
		//if(xs[2].charAt(0) >= 'A' && xs[2].charAt(0) <= 'Z'){
			if(xs[0].equals(ys[0]) && xs[1].equals(ys[1])){
				return 1.0;
			}
			if(xs[0].equals(ys[0]) || xs[1].equals(ys[1])){
				return .5;
			}
		//}
		
		return 0.0;
    }
    
	public static double calc(String x, String y){
		String[] xs = x.split(" ");
		String[] ys = y.split(" ");
		if(xs.length < 3 || ys.length < 3){
			//System.out.println(x);
			//System.out.println(y);
			return 0;
		}
		//if(xs[2].charAt(0) >= 'A' && xs[2].charAt(0) <= 'Z'){
			if(consim(xs[0], ys[0]) && consim(xs[1], ys[1])){
				return 1.0;
			}
			if(consim(xs[0], ys[0]) || consim(xs[1], ys[1])){
				return .5;
			}
		//}
		
		return 0.0;
	}
	
	public static Boolean consim(String x, String y){
		String[] xs = x.split("_");
		String[] ys = y.split("_");
		//System.out.println(xs[0] + "----" + ys[0]);
		if(xs[0].equals(ys[0]))return true;
		if(iMap.containsKey(xs[0]) && iMap.containsKey(ys[0]) && cos(iMap.get(xs[0]), iMap.get(ys[0]))> 0.8)return true;
		return false;
	}
}
