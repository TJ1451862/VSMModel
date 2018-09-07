package controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OutputEveryDocVector extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        File file = new File("E:\\code\\idea\\VSMWeb1\\text");
        Map<Integer,List> vector_map=new HashMap<>();
        Map<String, Double> idfmap=countIdf.countIdf();

        if (file.exists())
        {
            File[] files = file.listFiles();
            int ID=10;
            for (File file2 : files) {
                String FilePath=file2.getAbsolutePath();
                document doc=new document(FilePath,ID);
                double[] vector=doc.setVector(idfmap);
                List<Double> vector_List=new ArrayList<>();
                for (int i = 0; i <vector.length ; i++) {
                    vector_List.add(vector[i]);
                }
                vector_map.put(ID,vector_List);
                ID++;
            }
        }
        out.write(vector_map.toString());
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
        doGet(request,response);
    }
}
