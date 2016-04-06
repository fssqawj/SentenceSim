package ica.fssqawj;

import java.io.*;

/**
 * Created by fssqa on 2015/12/7.
 */
public class ReadFile {
    public static String readFileName = null;
    public static BufferedReader bufferedReader = null;
    public ReadFile(String fileName){
        readFileName = fileName;
        File inFile = new File(fileName);
        try {
            bufferedReader = new BufferedReader(new FileReader(inFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public ReadFile(String fileName, String codingType){
        File inFile = new File(fileName);
        FileInputStream in = null;
        try {
            in = new FileInputStream(inFile);
            bufferedReader = new BufferedReader(new InputStreamReader(in, codingType));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    public String readLine(){
        try {
            if(bufferedReader.readLine() == null)return null;

            return bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }
    public void close(){
        try {
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
