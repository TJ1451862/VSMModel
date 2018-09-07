package controller;

import service.DocSet;

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
        DocSet docSet=new DocSet("E:\\code\\idea\\VSMWeb3\\VSMModel\\text");
        Map<Integer,List> docVectors=docSet.getDocVectors();
        out.write(docVectors.toString());
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
        doGet(request,response);
    }
}
