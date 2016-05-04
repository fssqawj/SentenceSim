# SentenceSim
 中文短文句相似度的几种方法，主要包括基于知网的，onehot向量模型，基于word2vec，基于哈工大sdp及其融合算法,LSTM算法
 
 中文问句相似度计算在问答系统中有着极其重要的作用，在人工智能还未能实现自动答案产出的现阶段，利用已有的问题-答案集，通过短问句的相似度计算方法，发现和用户查询意图最近接的问句，是问答系统研究的一个重要方向。
## 当前版本
 v1.1
## 主要内容
 给出了基于知网、传统词向量、word2vec以及语义依存分析的短问句相似度算法，并根据实验结果分析了不同方法的优缺点。基于传统词向量的one-hot向量表示方法没有考虑问句中的语义信息，在计算效果上存在一定局限性，由于知网专业领域词汇的收录不全，也不能在此数据集上取得很好效果。通过word2vec方法训练中文wiki数据以及结合哈工大LTP平台的语义依存分析，以及结合专业领域的词汇表，能够取得不错的效果。使用Stanford LSTM开源代码，实现在中文文本上的相似读计算，取得比word2vec更好的效果
## 代码应用
 `Sensim.java` 是整个项目的入口，在main函数里面可以配置数据文件目录，以及词向量文件、sdp分析结果文件。
 ``` java
 DataSet.Init("corpus_utf8_del.txt");
 DataSet.InitWord2vec("vecmodel.bin");
 DataSet.InitSdp("sdpresy.txt");
 DataSet.Run("word2vec", 10);
 ```
 DataSet类可以通过Init函数配置数据文件的路径，InitWord2vec函数配置训练好的词向量文件路径，InitSdp函数配置sdp语法分析结果的文件路径。Run函数有两个参数，第一个是要使用的算法的名字：
 * hownet -> 应用知网
 * onehot -> onehot词向量表示
 * word2vec -> 应用word2vec训练的Wiki词向量
 * sdp -> 应用哈工大的sdp语法分析
 * word2vec-sdp -> 两种方法融合

第二个参数是确定候选集的大小，一般可取1,3,5,10


_ _ _
从百度网盘 [http://pan.baidu.com/s/1c1UyEJY](http://pan.baidu.com/s/1c1UyEJY) 下载data文件并解压到treelstm目录下，data文件夹下包含glove词向量文件（中文词向量文件已经预处理好torch需要的格式）、finacial中文金融领域问答数据。从百度网盘 [http://pan.baidu.com/s/1pLCcpOR](http://pan.baidu.com/s/1pLCcpOR) 下载stanford-nlp文件，并将解压出来的两个文件(stanford-parser, stanford-tagger)夹放到treelstm/lib目录下。若需要更改train、dev和test内容，可清空finacial文件下内容，然后在该目录下按如下格式生成三个文件train.txt、dev.txt、test.txt
``` lua
8	什么 是 市值 申购	市值 申购 的 服务 有 哪些	1.0	NEUTRAL
25	什么 是 港股 直 通车	我 不 是 很 清楚 你们 的 港股 直 通车 的 情况 能 说明 一下 吗	1.0	NEUTRAL
```
用 '\t' 分隔第一列ID，第二列分词之后的第一个问句（空格分隔），第三列为第二个问句，第四列为相似读（0或1），最后一列在本实验中没有实际用途。
确保机器环境能够运行touch/lua(https://github.com/torch/torch7) ,并安装LSTM所需要的package(https://github.com/stanfordnlp/treelstm) , 可以把train、dev和test文件放到项目的根目录下，然后运行 run.sh， 此脚本作用是编译lib下的java文件， 清空financial目录，并在financial目录下按照特定格式生成数据文件，包括对中文数据的依存分析和token提取， 运行：
``` lua
th relatedness/main.lua --model lstm --epochs <num_epochs>
```
可在predictions文件夹下生成对test数据的相似读预测分值。使用eval/precision.py评测结果（如果test有打分的话，在data/finacial/test/sim.txt） 输入的第一个参数是预测文件， 第二个参数是sim.txt, 第三个参数是eps值（小于eps判定为不相似，大于eps判定为相似）。

### 中文修改
由于本项目是在LSTM源码上进行的修改，存在数据集不同，分类方式不同的问题，主要改动的地方是relatedness/main.lua，LSTMSim.lua更改分类数目，及解决中文上的bug问题。LSTM算法没有用到句子的语法树信息（虽然parse过了），data/finacial/[train|test|dev]下的.parent是没有实际意义的。
在此实验中（测试集中正负样本比例在7：1）LSTM方法的准确率达到了0.93， 而word2vec的准确率在0.90
## 数据格式
 需要计算相似度的两个问句在一行中用\t####\t分隔，corpus_utf8_del.txt:
 ```
 新股的购买需要税金和手续费用吗?	####	告诉我新股申购的手续费是多少
 新股的购买需要税金和手续费用吗?	####	告诉我新股购买的手续费是多少
 新股申购市值包含哪些？	####	新股申购市值可以提供哪些内容
 关于新股相关服务事项说明	####	新股联系业务事项可以提供哪些内容
 ```
 word2vec训练好的词向量文件，第一行为词条数目和向量维度，之后每一行，第一列为词条，之后n列为其向量表示。
 
 sdp语法分析文件，调用哈工大LTP API结果：
 ```
 http://api.ltp-cloud.com/analysis/?api_key=yourKey&pattern=sdp&format=plain&text=网上交易为什么要选择 “绿色通道”？:
 网上_0 交易_1 Agt
 交易_1 -1 Root
 为什么_2 选择_4 mTone
 要_3 选择_4 mMod
 选择_4 交易_1 dCont
 “_5 通道_7 mPunc
 绿色_6 通道_7 Feat
 通道_7 选择_4 Cont
 ”_8 通道_7 mPunc
 ？_9 交易_1 mPunc
 ```
LSTM的训练和测试文件如前一节所提。
## 更新
* 2016/1/15 上传SentenceBaseWord2vec项目
* 2016/4/6 上传此项目，此项目后续更新
* 2016/4/20 增加LSTM代码

## 引用
1. [知网](http://www.keenage.com/html/c_index.html)

2. [https://github.com/daishengdong/WordSimilarity](https://github.com/daishengdong/WordSimilarity)

3. [Mikolov, T.; Chen, K.; Corrado, G.; and Dean, J. 2013a.Efficient estimation of word representations in vector space.Proceedings of ICLR.](http://arxiv.org/abs/1301.3781)

4. [http://www.ltp-cloud.com/](http://www.ltp-cloud.com/)

5. [http://www.52nlp.cn/中英文维基百科语料上的word2vec实验/comment-page-1](4. http://www.52nlp.cn/中英文维基百科语料上的word2vec实验/comment-page-1)

6. [https://github.com/stanfordnlp/treelstm](https://github.com/stanfordnlp/treelstm)

## Connect
ICA fssqawj fssqawj@gmail.com
