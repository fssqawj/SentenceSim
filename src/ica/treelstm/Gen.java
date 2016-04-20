package ica.treelstm;

import java.awt.print.Printable;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import ica.fssqawj.*;

public class Gen {

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
		while((temp = bufferedReader.readLine()) != null){
			String[] token = temp.split("\t####\t");
			double t = Math.random();
			if(t < 0.05)writer.write(idx + "\t" + change(token[0]) + "\t" + change(token[1]) + "\t1.0\tNEUTRAL\n");
			else if(t >= 0.05 && t < 0.1){
				writerdev.write(idx + "\t" + change(token[0]) + "\t" + change(token[1]) + "\t1.0\tNEUTRAL\n");
			}
			else if(t >= 0.15 && t < 0.2){
				writertest.write(idx + "\t" + change(token[0]) + "\t" + change(token[1]) + "\t1.0\tNEUTRAL\n");
			}
			idx ++;
			if(idx % 50 == 27){
				if(pre != ""){
					writer.write(idx + "\t" + change(token[0]) + "\t" + change(pre) + "\t0.0\tNEUTRAL\n");
				}
				pre = token[1];
			}
			if(idx % 50 == 37){
				if(predev != ""){
					writerdev.write(idx + "\t" + change(token[0]) + "\t" + change(predev) + "\t0.0\tNEUTRAL\n");
				}
				predev = token[1];
			}
			if(idx % 50 == 47){
				if(pretest != ""){
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
