package it.kk_crm.dispatcher;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.rmi.ServerException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "Dispatcher", urlPatterns = {"/Dispatcher"})
public class Dispatcher extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            String controllerAction = request.getParameter("controllerAction");
            Cookie[] cookies = request.getCookies();
            String logged = null;
            if (cookies != null) {
                for (int i = 0; i < cookies.length && logged == null; i++) {
                    if (cookies[i].getName().equals("loggedUser")) {
                        logged = cookies[i].getValue();
                    }
                }
            }
            if(logged == null && controllerAction != null && !controllerAction.equals("LoginController.logon")){
                controllerAction="LoginController.view";
            } else {
                if(controllerAction == null) {
                    controllerAction = "LoginController.view";
                } else {
                    controllerAction=request.getParameter("controllerAction");
                    String piva = request.getParameter("piva");
                }

            }

            String[] splittedAction=controllerAction.split("\\.");
            Class<?> controllerClass=Class.forName("it.kk_crm.controller."+splittedAction[0]);
            Method controllerMethod=controllerClass.getMethod(splittedAction[1],HttpServletRequest.class,HttpServletResponse.class);
            controllerMethod.invoke(null,request,response);

            String viewUrl=(String)request.getAttribute("viewUrl");
            RequestDispatcher view=request.getRequestDispatcher("jsp/"+viewUrl+".jsp");
            view.forward(request,response);


        } catch (Exception e) {
            e.printStackTrace(out);
            throw new ServerException("Dispacther Servlet Error",e);

        } finally {
            out.close();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}

