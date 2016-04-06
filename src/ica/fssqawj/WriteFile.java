package ica.fssqawj;

import java.io.*;

/**
 * Created by fssqa on 2015/12/7.
 */
public class WriteFile {
    public static String writeFileName = null;
    public static BufferedWriter bufferedWriter = null;
    public WriteFile(String fileName){
        writeFileName = fileName;
        File outFile = new File(fileName);
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(outFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public WriteFile(String fileName, String codingType){
        File outFile = new File(fileName);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(outFile);
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(out, codingType));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    public void writeLine(String content){
        try {
            bufferedWriter.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void close(){
        try {
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
