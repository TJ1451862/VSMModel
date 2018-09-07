package utils;

import java.io.*;
import java.util.ArrayList;

public class ReadStopWords {

    public static ArrayList<String> readStopWords(){
        ArrayList<String> stopWordAL  = new ArrayList();
        try{
            FileInputStream stopWordFile = new FileInputStream("E:\\code\\idea\\VSMWeb1\\ChineseStopWord.txt");//停用词
            InputStreamReader input=new InputStreamReader(stopWordFile,"UTF-8");
            BufferedReader stopWordBR = new BufferedReader(input);//构造一个BufferedReader类来读取ChineseStopWord文件
            String stopWord;
            while ((stopWord = stopWordBR.readLine()) != null) {//使用readLine方法，一次读一行 读取停用词
                stopWordAL.add(stopWord);
            }
            stopWordBR.close();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }//读取停用词表
        return stopWordAL;
    }
}//读停用词表
