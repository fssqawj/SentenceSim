package ica.fssqawj;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.xml.ws.handler.MessageContext.Scope;

public class DataSet {
    public static List<Question> hList = new ArrayList<Question>();
    public static List<Question> rList = new ArrayList<Question>();
    public static Map<String, Integer> hMap = new HashMap<String, Integer>();

    public static Map<String, Integer> rMap = new HashMap<String, Integer>();

    public static Map<Integer, List<Integer>> matchQuestion = new HashMap<Integer, List<Integer>>();

    public static Set<String> iSet = new HashSet<String>();
    public static Set<String> jSet = new HashSet<String>();
    public static Set<String> kSet = new HashSet<String>();
    
    public static int hit = 0;
    
    public static void Init(String dataFile) throws IOException{
    	System.out.println("DataSet Init... ...");
    	BufferedReader bufferedReader = null;
        File trainFile = new File(dataFile);
		
		bufferedReader = new BufferedReader(new FileReader(trainFile));
		
		//BufferedWriter writer = new BufferedWriter(new FileWriter(new File("train.txt")));
		
		String temp = "";
		
        int hcnt = 1;
        int rcnt = 1;
        int tcnt = 1;
        
        //int idx = 0;
        while((temp = bufferedReader.readLine()) != null){
            String[] qtem = temp.split("\t####\t");
            //System.out.println(temp);
            List<String> t = CommonFunction.getTerm(qtem[0]);
			for(String key : t){
				jSet.add(key);
			}
			t = CommonFunction.getTerm(qtem[1]);
			for(String key : t){
				jSet.add(key);
			}
            String hq = qtem[0];
            String rq = qtem[1];
            if(!hMap.containsKey(hq)){
                hMap.put(hq, hcnt ++);
                Question tq = new Question();
                tq.setContent(hq);
                hList.add(tq);
            }
            if(!rMap.containsKey(rq)){
                rMap.put(rq, rcnt ++);
                Question tq = new Question();
                tq.setContent(rq);
                rList.add(tq);
            }

            int hid = hMap.get(hq);
            int rid = rMap.get(rq);

            if(!matchQuestion.containsKey(hid)){
                List<Integer> tem = new ArrayList<Integer>();
                tem.add(rid);
                matchQuestion.put(hid, tem);
            }
            else {
                List<Integer> tem = matchQuestion.get(hid);
                tem.add(rid);
                matchQuestion.put(hid, tem);
            }
            //idx ++;
            if(Math.random() < 0.05){
                iSet.add(hq);
                kSet.add(rq);
                //writer.write(idx + "\t" + hq + "\t" + rq + "\t1.0\tNEUTRAL\n");
                
                
                tcnt = tcnt + 1;
            }
            
        }
        bufferedReader.close();
        System.out.println("DataSet Init Done.");

    }
    public static void Run(String method, int K) throws Exception{
    	int cnt = 0;
        if(null != CommonFunction.binFile){
        	CommonFunction.Initword2vec(jSet);
        }
        if(null != CommonFunction.sdpFile){
        	CommonFunction.Initsdp();
        }
    	for(String hq : iSet){
            for(int j = 0;j < rList.size();j ++){
                String rq = rList.get(j).getContent();
                if(!kSet.contains(rq))continue;
                rList.get(j).setSrc(getSrc(hq, rq, method));
            }
            
            System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
            
            Collections.sort(rList);
            
            int hid = hMap.get(hq);
            
            for(int k = 0;k < K;k ++){
            	int id = rMap.get(rList.get(k).getContent());
                if(matchQuestion.get(hid).contains(id)){
                    hit ++;
                    break;
                }
            }
            System.out.print("case " + cnt ++ + " solved.\r");
            //if(flag == 1){
            //	System.out.println(hq);
            //	for(int k = 0;k < 3;k ++){
            //		System.out.println(k+"-------\n" + rList.get(k).getContent() + " " + rList.get(k).getSrc());
            //	}
            //}

        }
        System.out.println("total case : " + iSet.size());
        System.out.println("hit : " + hit + " precision : " + 1.0 * hit / iSet.size());
    }
    public static void InitWord2vec(String binFile){
    	CommonFunction.binFile = binFile;
    }
    public static void InitSdp(String sdpFile){
    	CommonFunction.sdpFile = sdpFile;
    }
    public static double getSrc(String hq, String rq, String method) throws Exception{
    	if(method.equals("hownet")){
    		return CommonFunction.SentenceSimHownetVal(hq, rq);
    	}
    	if(method.equals("onehot")){
    		return CommonFunction.SentenceSimOnehotVal(hq, rq);
    	}
    	if(method.equals("word2vec")){
    		return CommonFunction.sentenceSimWord2vecVal(hq, rq);
    	}
    	if(method.equals("sdp")){
    		return CommonFunction.sentenceSimSdpVal(hq, rq);
    	}
    	if(method.equals("word2vec-sdp")){
    		return CommonFunction.sentenceSimWord2vecSdpVal(hq, rq);
    	}
    	throw new Exception("Unknown Method!");
    	//return 0.0;
    }
}
