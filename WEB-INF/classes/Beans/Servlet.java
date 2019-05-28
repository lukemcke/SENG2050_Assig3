package Beans;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;

@WebServlet(urlPatterns = { "/assignment3" })
public class Servlet extends HttpServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		DataAccess DA = new DataAccess();
		
		if(request.getParameter("report") != null){
			addIssue(DA, request, response);
		}
		try {
			request.setAttribute("categories", DA.getCategories());
		}
		catch(Exception ex){
		}
		RequestDispatcher dispather = getServletContext().getRequestDispatcher("/WEB-INF/Jsps/Issues/addIssue.jsp");
		dispather.forward(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		doGet(request,response);
	}
	
	
	private void addIssue(DataAccess DA, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		try {
			String Title = request.getParameter("issueTitle");
			String Description = request.getParameter("issueDescription");
			String Category = request.getParameter("Category");
			String SubCategory = request.getParameter("SubCategory");
			
			Issue issue = new Issue();
			issue.setTitle(Title);
			issue.setDescription(Description);
			issue.setDateReported(java.time.LocalDate.now().toString());
			
			DA.reportIssue(issue);
		}
		catch(Exception ex){
			
			}
		}
}