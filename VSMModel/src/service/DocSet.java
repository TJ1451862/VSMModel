package service;

import util.ReadDoc;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class DocSet {

    private String folderPath;
    private Map<String, ArrayList<Integer>> indexMap;
    private Map<String, Double> wordIdf;


    public DocSet(String folderPath){
        this.folderPath=folderPath;
        this.indexMap=setIndexMap();
        this.wordIdf=setWordIdf();
    }

    public String getFolderPath(){
        return folderPath;
    }

    /**
     * 建立倒排索引
     * @return
     */


    public Map<String, ArrayList<Integer>> setIndexMap(){

        Map<String, ArrayList<Integer>> indexMap=new HashMap<>();
        File file = new File(folderPath);
        if (file.exists())
        {
            File[] files = file.listFiles();
            int ID=10;
            for (File file2 : files) {
                String FilePath=file2.getAbsolutePath();
                Document doc=new Document(FilePath,ID);
                //doc.countTf();
                ArrayList<String> sw=doc.removeStopWords();
                for (String string : sw) {
                    ArrayList<Integer> list=new ArrayList<>();
                    if (!indexMap.containsKey(string)) {
                        list.add(ID);
                        indexMap.put(string, list);
                    }else {
                        list=indexMap.get(string);
                        if (!list.contains(ID)) {
                            list.add(ID);
                        }
                    }
                }
                ID++;
            }
        }
        try{
            FileWriter inIn = new FileWriter(new File("VSMModel/intertedIndex.txt"));
            inIn.write(indexMap.toString());
            inIn.close();
        }catch (Exception e){
            System.out.println(e.getStackTrace());
        }
        return indexMap;
    }//建立倒排索引

    public Map<String, ArrayList<Integer>> getIndexMap(){
        return indexMap;
    }

    public Map<String, Double> setWordIdf(){

        Map<String, Double> wordIdf=new HashMap<>();
        File file = new File(folderPath);
        if (file.exists())
        {
            File[] files = file.listFiles();
            int ID=10;
            for (File file2 : files) {
                String FilePath=file2.getAbsolutePath();
                Document doc=new Document(FilePath,ID);
                ArrayList<String> sw=doc.removeStopWords();
                for (String string : sw) {
                    double count;
                    if (!wordIdf.containsKey(string)) {
                        wordIdf.put(string, 1.0);
                    }else {
                        count=wordIdf.get(string)+1;
                        wordIdf.put(string, count);
                    }
                }
                ID++;
            }
        }
        int N=wordIdf.size();
        double idf;
        for (String key:wordIdf.keySet()) {
            idf=Math.log(N/(wordIdf.get(key)));
            wordIdf.put(key,idf);
        }

        try{
            FileWriter inIn = new FileWriter(new File("VSMModel/idfIndex.txt"));
            inIn.write(wordIdf.toString());
            inIn.close();
        }catch (Exception e){
            System.out.println(e.getStackTrace());
        }
        return wordIdf;
        }//求Idf,idf=log(N/df)

    public Map<String, Double> getWordIdf() {
        return wordIdf;
    }

    public Map<Integer,Map<String, Double>> countDocTfs(){

        Map<Integer,Map<String, Double>> docTfs=new HashMap<>();
        File file = new File(folderPath);

        if (file.exists())
        {
            File[] files = file.listFiles();
            int ID=10;
            for (File file2 : files) {
                String FilePath=file2.getAbsolutePath();
                Document doc=new Document(FilePath,ID);
                Map<String, Double> wordTf=doc.getWordTf();
                docTfs.put(ID,wordTf);
                ID++;
            }
        }
        return docTfs;
    }


    public Map<Integer,List> countDocVectors(){

        File file = new File(folderPath);
        Map<Integer,List> docVectors=new HashMap<>();
        if (file.exists())
        {
            File[] files = file.listFiles();
            int ID=10;
            for (File file2 : files) {
                String FilePath=file2.getAbsolutePath();
                Document doc=new Document(FilePath,ID);
                double[] vector=doc.countDocVector(wordIdf);
                List<Double> vector_List=new ArrayList<>();
                for (int i = 0; i <vector.length ; i++) {
                    vector_List.add(vector[i]);
                }
                docVectors.put(ID,vector_List);
                ID++;
            }
        }
        return docVectors;
    }

    public Map<Integer,String> countWordVectors(){

        File file = new File(folderPath);
        Map<Integer,String> wordVectors=new HashMap<>();
        if (file.exists())
        {
            File[] files = file.listFiles();
            int ID=10;
            for (File file2 : files) {
                String FilePath=file2.getAbsolutePath();
                Document doc=new Document(FilePath,ID);
                Map<String,Double> wordVector=doc.countWordVector(wordIdf);
                String output="[0.0,";
                for (String in:wordVector.keySet()) {
                    output=output+"……,("+in+")"+wordVector.get(in)+",";
                }
                output=output+"……,0.0]";
                wordVectors.put(ID,output);
                ID++;
            }
        }
        return wordVectors;
    }

    public double countSimilarity( double[] queryVector,Document doc){
        double[] docVector=doc.countDocVector(wordIdf);
        double similarity=0;
        for (int i = 0; i <docVector.length ; i++) {
            if((docVector[i]!=0)&&(queryVector[i]!=0))
                similarity+=docVector[i]*queryVector[i];
        }
        return similarity;
    }//计算查询与单个文档的相似度

    public Map<String,Double>  Similarities(double[] queryVector) {

        File file = new File(folderPath);
        Map<Integer,Double> id_Similarity=new HashMap<>();
        Map<String,Double> FileName_Similarity=new HashMap<>();

        if (file.exists())
        {
            File[] files = file.listFiles();
            int ID=10;
            double similarity;
            for (File file2 : files) {
                String FilePath=file2.getAbsolutePath();
                String FileName=file2.getName();
                Document doc=new Document(FilePath,ID);
                similarity=countSimilarity(queryVector,doc);
                if(similarity!=0.0){
                    id_Similarity.put(ID,similarity);
                    FileName_Similarity.put(FileName,similarity);
                }
                ID++;
            }
        }

        return FileName_Similarity;
    }//查询与所有文档的相似度

    public List<Map.Entry<String,Double>> sortSimilarities (double[] queryVector){
        Map<String,Double> FileName_Similarity=Similarities(queryVector);
        //将map.entrySet()转换成list
        List<Map.Entry<String, Double>> list = new ArrayList<>(FileName_Similarity.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
            //降序排序
            @Override
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                //return o1.getValue().compareTo(o2.getValue());
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        return list;
    }

    public Map<String, String> result(double[] queryVector){
        List<Map.Entry<String,Double>> list=sortSimilarities(queryVector);
        Map<String, String> fileName_value= new HashMap<>();

        File file = new File("E:\\code\\idea\\VSMWeb1\\text");
        if (file.exists())
        {
            File[] files = file.listFiles();
            for (File file2 : files) {
                for (Map.Entry<String,Double> mapping:list) {
                    if(mapping.getKey().equals(file2.getName())){
                        fileName_value.put(file2.getName(),ReadDoc.readDoc(file2.getPath()));
                    }
                }
            }
        }
        return fileName_value;

    }
        }
