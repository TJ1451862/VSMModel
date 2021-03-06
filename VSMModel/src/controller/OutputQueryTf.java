package controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import service.Query;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import static util.ReadDoc.readDoc;

public class OutputQueryTf extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        Query query=new Query(readDoc("E:\\code\\idea\\VSMWeb3\\VSMModel\\Query.txt"));
        Map<String, Double> wordTf =query.countWordTf();
        String s = JSON.toJSONString(wordTf);
        //System.out.println(s);
        out.print(s);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
        doGet(request,response);
    }

}
