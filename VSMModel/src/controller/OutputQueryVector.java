package controller;

import vectorSpaceModel.Query;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import static utils.ReadDoc.readDoc;

public class OutputQueryVector extends HttpServlet {



    public void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        Query query=new Query(readDoc("E:\\code\\idea\\VSMWeb3\\VSMModel\\Query.txt"));
        Map<String,Double> word_vector=query.getWord_vector();
        String output="[0.0,";
        for (String in:word_vector.keySet()) {
            output=output+"……,("+in+")"+word_vector.get(in)+",";
        }
        output=output+"……,0.0]";
        out.write(output);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
        doGet(request,response);
    }

}
