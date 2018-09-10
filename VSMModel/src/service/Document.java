package service;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static util.ReadDoc.readDoc;
import static util.ReadStopWords.readStopWords;


public class Document {

    private String docPath;
    private int docID;
    private Map<String, Double> wordTf;


    public Document(String docPath, int docID) {
        this.docPath = docPath;
        this.docID = docID;
        this.wordTf=setWordTf();
    }

    public List<Term> segment(){

        String docs=readDoc(docPath);
        HanLP.Config.ShowTermNature = false;//停用词性显示
        List<Term> termList = HanLP.segment(docs);
        return termList;

    }//标准分词

    public List<Term> nonPunctuation()//去标点
    {
        List<Term> termListDoc = segment();
        String doc=termListDoc.toString();
        doc=doc.replaceAll("[\\pP\\n\\t\\s]", "");
        doc=doc.replaceAll("　　", "");
        HanLP.Config.ShowTermNature = false;//停用词性显示
        termListDoc = HanLP.segment(doc);
        return termListDoc;
    }//去标点

    public ArrayList removeStopWords()//去停用词
    {

        ArrayList<String> stopWordAL  = readStopWords();
        ArrayList<String> TermList = new ArrayList();
        List<Term> termList =this.nonPunctuation();
        for (int i = 0; i < termList.size(); i++) {
            TermList.add(termList.get(i).toString());
        }
        TermList.removeAll(stopWordAL);
        return TermList;
    }

    public Map<String, Double> setWordTf(){

        Map<String, Double> wordTf = new HashMap<>();
        ArrayList<String> termList = removeStopWords();
        for (String string : termList) {
            if (!wordTf.containsKey(string)) {
                wordTf.put(string, 1.0);
            } else {
                double tf = 1 + Math.log(wordTf.get(string) + 1);
                wordTf.put(string, tf);
            }
        }
        return wordTf;
    }

    public Map<String, Double> getWordTf() {
        return wordTf;
    }//计算tf,tf=1+log(tf)

    public double[] countDocVector(Map<String, Double> wordIdf){

        double[] docVector;
        int idfLength = wordIdf.size();
        docVector=new double[idfLength+1];
        int num = 0;
        for (String in : wordIdf.keySet()) {
            if (wordTf.containsKey(in)) {
                double tf = wordTf.get(in);
                double idf = wordIdf.get(in);
                docVector[num] = tf * idf;
            } else {
                num++;
            }
        }
        double sum = 0;
        for (double j : docVector) {
            sum += Math.pow(j, 2);
        }
        sum = Math.pow(sum, 0.5);
        for (int i = 0; i < docVector.length; i++) {
            docVector[i] = docVector[i] / sum;
        }
        return docVector;
    }

    public Map<String,Double> countWordVector(Map<String, Double> wordIdf){

        Map<String,Double> wordVector=new HashMap<>();
        int idfLength = wordIdf.size();
        double[] vector = new double[idfLength + 1];
        int num = 0;
        for (String in : wordIdf.keySet()) {
            if(wordTf.containsKey(in)){
                double tf = wordTf.get(in);
                double idf = wordIdf.get(in);
                if((tf!=0)&&(idf!=0))
                    vector[num] = tf * idf;
                wordVector.put(in,vector[num]);
                num++;
            }else {
                num++;
            }
        }
        double sum=0;
        for (String in:wordVector.keySet()) {
            sum+=Math.pow(wordVector.get(in),2);
        }
        sum=Math.pow(sum,0.5);
        for (String in:wordVector.keySet()) {
            double value=wordVector.get(in)/sum;
            wordVector.put(in,value);
        }
        return wordVector;

    }

}
