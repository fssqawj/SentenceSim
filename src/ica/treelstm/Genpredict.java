package ica.treelstm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ica.fssqawj.CommonFunction;

public class Genpredict {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		BufferedReader reader = new BufferedReader(new FileReader(new File("test.txt")));
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File("out.txt")));
		
		String temp = "";
		
		List<Double> res = new ArrayList<Double>();
		while((temp = reader.readLine()) != null){
			String tokenx = temp.split("\t")[1].replace(" ", "");
			String tokeny = temp.split("\t")[2].replace(" ", "");
			res.add(CommonFunction.sentenceSimWord2vecVal(tokenx, tokeny));
		}
		nomorlize(res);
		for(int i = 0;i < res.size();i ++){
			writer.write(res.get(i) + "\n");
		}
		writer.flush();
		writer.close();
		reader.close();
	}	
	
	public static void nomorlize(List<Double> x){
		double maxx = -1e10, minn = 1e10;
		for(double k : x){
			maxx = Math.max(maxx, k);
			minn = Math.min(minn, k);
		}
		for(int i = 0;i < x.size();i ++){
			x.set(i, (x.get(i) - minn) / maxx - minn);
		}
	}

}
