package ica.fssqawj;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.ansj.splitWord.analysis.ToAnalysis;

public class Test {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		/*
		File inFile = new File("test47_50.txt");
		
		BufferedReader bufferedReader = null;
		
		
		//File trainFile = new File("corpus_utf8.txt");
		
		bufferedReader = new BufferedReader(new FileReader(inFile));
		
		String temp = "";
		
		while((temp = bufferedReader.readLine()) != null){
			System.out.println(ToAnalysis.parse(temp));
			//System.out.println(ToAnalysis.parse("交割单尾差表示什么啊"));
			//System.out.println(ToAnalysis.parse("你们的7天期是个什么业务"));
			//System.out.println(ToAnalysis.parse("紫金是不是要手动赎回"));
		}
		*/
		System.out.println(ToAnalysis.parse("客户号定义"));
		System.out.println(ToAnalysis.parse("紫金是不是要手动赎回"));
		System.out.println(ToAnalysis.parse("创业板都是些什么内容啊"));
		System.out.println(ToAnalysis.parse("所有用户度可以开设股权质押式回购嘛"));
	}

}
