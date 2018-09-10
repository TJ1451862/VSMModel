package controller;

import service.DocSet;
import service.Query;
import util.ReadDoc;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class OutputSimilarities extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        DocSet docSet=new DocSet("E:\\code\\idea\\VSMWeb3\\VSMModel\\text");
        String queryString=ReadDoc.readDoc("E:\\code\\idea\\VSMWeb3\\VSMModel\\Query.txt");
        Query query=new Query(queryString);
        double[] queryVector =query.countQueryVector(docSet.getWordIdf());
        Map<String,Double> Similarities=docSet.Similarities(queryVector);
        out.print(Similarities);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
        doGet(request,response);
    }

}
