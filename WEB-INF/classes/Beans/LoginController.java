package Beans;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.Serializable;
import java.util.*;
import java.text.SimpleDateFormat;

@WebServlet(urlPatterns = { "/Login" })
public class LoginController extends HttpServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		DataAccess DA = new DataAccess();
		HttpSession session = request.getSession();
		RequestDispatcher dispatchIndex = getServletContext().getRequestDispatcher("/WEB-INF/Jsps/Index.jsp");
		RequestDispatcher dispatchLogin = getServletContext().getRequestDispatcher("/WEB-INF/Jsps/Login.jsp");
	
		try {
			//when login button clicked
		if(request.getParameter("login") != null) {
				//verify login if the data user is not stored in the database do nothing
				if(DA.verifyLogin(request.getParameter("email"), request.getParameter("password")))
				{
					session.setAttribute("userLogin", DA.getUser(request.getParameter("email")));
					User user = (User) session.getAttribute("userLogin");
					//display notifications on index page
					request.setAttribute("notifications", DA.getNotifications(user.getUserid(), user.getIsadmin()));
					dispatchIndex.forward(request, response);
				}
					
		}
		}
		catch (Exception ex){
			
		}
		
		dispatchLogin.forward(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		doGet(request,response);
	}
	
}