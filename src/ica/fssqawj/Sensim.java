package ica.fssqawj;

import java.io.IOException;

/**
 * Created by fssqa on 2015/12/7.
 */
public class Sensim {

    public static void main(String[] args) throws Exception{
        DataSet.Init("corpus_utf8_del.txt");
        DataSet.InitWord2vec("vecmodel.bin");
        //DataSet.InitSdp("sdpresy.txt");
        DataSet.Run("word2vec", 10);
    }

}
