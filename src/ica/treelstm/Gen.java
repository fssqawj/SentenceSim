package ica.treelstm;

import java.awt.print.Printable;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ica.fssqawj.*;

public class Gen {
	public static double train = 0.3;
	public static double dev = 0.5;
	public static double test = 0.7;
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		File trainFile = new File("corpus_utf8_del.txt");
		BufferedReader bufferedReader = null;
		bufferedReader = new BufferedReader(new FileReader(trainFile));
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File("train.txt")));
		BufferedWriter writerdev = new BufferedWriter(new FileWriter(new File("dev.txt")));
		BufferedWriter writertest = new BufferedWriter(new FileWriter(new File("test.txt")));
		String temp = "";
		int idx = 0;
		String pre = "";
		String predev = "";
		String pretest = "";
		Set<String> iSet = new HashSet<String>();
		
		while((temp = bufferedReader.readLine()) != null){
			iSet.add(temp);
		}
		bufferedReader.close();
		bufferedReader = new BufferedReader(new FileReader(trainFile));
		while((temp = bufferedReader.readLine()) != null){
			String[] token = temp.split("\t####\t");
			double t = Math.random();
			if(t < train)writer.write(idx + "\t" + change(token[0]) + "\t" + change(token[1]) + "\t1.0\tNEUTRAL\n");
			else if(t >= train && t < dev){
				writerdev.write(idx + "\t" + change(token[0]) + "\t" + change(token[1]) + "\t1.0\tNEUTRAL\n");
			}
			else if(t >= dev && t < test){
				writertest.write(idx + "\t" + change(token[0]) + "\t" + change(token[1]) + "\t1.0\tNEUTRAL\n");
			}
			idx ++;
			if(idx % 7 == 2){
				if(pre != ""&& !iSet.contains(token[0] + "\t####\t" + pre)){
					writer.write(idx + "\t" + change(token[0]) + "\t" + change(pre) + "\t0.0\tNEUTRAL\n");
				}
				pre = token[1];
			}
			if(idx % 7 == 4){
				if(predev != ""&& !iSet.contains(token[0] + "\t####\t" + predev)){
					writerdev.write(idx + "\t" + change(token[0]) + "\t" + change(predev) + "\t0.0\tNEUTRAL\n");
				}
				predev = token[1];
			}
			if(idx % 7 == 6){
				if(pretest != "" && !iSet.contains(token[0] + "\t####\t" + pretest)){
					writertest.write(idx + "\t" + change(token[0]) + "\t" + change(pretest) + "\t0.0\tNEUTRAL\n");
				}
				pretest = token[1];
			}
		}
		writerdev.flush();
		writerdev.close();
		writertest.flush();
		writertest.close();
		writer.flush();
		writer.close();
		bufferedReader.close();
	}
	public static String change(String x){
		x = x.replaceAll("[\\pP‘’“”]", ""); 
		List<String>t = CommonFunction.getTerm(x);
		String res = "";
		for(String key : t){
			res += key + " ";
		}
		//System.out.println(res.length());
		//System.out.println(res);
		return res.substring(0, res.length() - 1);
	}
}
