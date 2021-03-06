package Beans;

import javax.sql.*;
import java.sql.*;
import java.sql.Date;
import java.util.*;


public class DataAccess {
	
	
	//returns whether the email and passwrod exists in the record by returning a 1 otherwise return false
	public static boolean verifyLogin(String Email, String Password) throws Exception {
		
		int total = 0;
		try {
			Connection connection = Config.getConnection();
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery("SELECT COUNT(*) FROM UserAccount WHERE Email = '"+ Email + "' AND Password = '" + Password + "'");
			result.next();
			total = result.getInt(1);
		}
		catch(SQLException e){
			System.err.println(e.getMessage());
			System.err.println(e.getStackTrace());
		}
		
		return total == 1;
	}
	
	//
	public static List<Issue> getUserIssues(int UserID) throws Exception {
		String query = "SELECT * FROM Issue WHERE UserID = "+ UserID + "";
		List<Issue> Issues = new LinkedList<>();
		try(Connection connection = Config.getConnection();
		Statement statement = connection.createStatement(); 
		ResultSet result = statement.executeQuery(query);){ 
			while(result.next()){ 
				Issue issue = new Issue();

				issue.setIssueid(result.getInt(1));
				issue.setTitle(result.getString(2));
				issue.setDescription(result.getString(3));
				issue.setDatereported(result.getString(5));
				issue.setDateresolved(result.getString(6));
				issue.setStatus(result.getString(7));
				issue.setCategory(result.getString(8));
				issue.setSubcategory(result.getString(9));
				
				Issues.add(issue);
				
			}
		}
		catch(SQLException e){
			System.err.println(e.getMessage());
			System.err.println(e.getStackTrace());
		}
		return Issues;
	}
	//Sets resolve details by IssueID from the notifications
	public void addResolveDetails(int IssueID, String resolvedetails) throws Exception{
		Connection connection = Config.getConnection();
		PreparedStatement statement = connection.prepareStatement("UPDATE Issue SET ResolveDetails = '"+ resolvedetails + "' WHERE IssueID = " + IssueID + "");
		statement.executeUpdate();
		
	}
	
	public static User getUser(String Email) throws Exception {
		String query = "SELECT * FROM UserAccount WHERE Email = '"+ Email + "'";
		User user = new User();
		try(Connection connection = Config.getConnection();
		Statement statement = connection.createStatement(); 
		ResultSet result = statement.executeQuery(query);){ 
			while(result.next()){ 
				user.setUserid(result.getInt(1));
				user.setFirstname(result.getString(2));
				user.setLastname(result.getString(3));
				user.setEmail(result.getString(4));
				user.setPhone(result.getInt(5));
				user.setPassword(result.getString(6));
				user.setIsadmin(result.getBoolean(7));
			}
		}
		catch(SQLException e){
			System.err.println(e.getMessage());
			System.err.println(e.getStackTrace());
		}
			return user;
	}
	
	public static List<String> getCategories() throws Exception{
		List<String> categories = new LinkedList<String>();
		categories.add("Network");
		categories.add("Software");
		categories.add("Hardware");
		categories.add("Email");
		categories.add("Account");
		
		return categories;
	}
	
	//returns specific issue as an object
	public static Issue getIssue(int IssueID) throws Exception{
		String query = "SELECT * FROM Issue WHERE IssueID = "+ IssueID + "";
		Issue issue = new Issue();
		try(Connection connection = Config.getConnection();
		Statement statement = connection.createStatement(); 
		ResultSet result = statement.executeQuery(query);){ 
			while(result.next()){ 
				issue.setIssueid(result.getInt(1));
				issue.setTitle(result.getString(2));
				issue.setDescription(result.getString(3));
				issue.setResolvedetails(result.getString(4));
				issue.setDatereported(result.getString(5));
				issue.setDateresolved(result.getString(6));
				issue.setStatus(result.getString(7));
				issue.setCategory(result.getString(8));
				issue.setSubcategory(result.getString(9));
			}
		}
		catch(SQLException e){
			System.err.println(e.getMessage());
			System.err.println(e.getStackTrace());
		}
			return issue;
	}
	//returns all articles as a List of the knowledge object
	public static List<Knowledge> getAllArticles() throws Exception{
		String query = "SELECT * FROM KnowledgeBase";
		List<Knowledge> articles = new LinkedList<>();
		try(Connection connection = Config.getConnection();
		Statement statement = connection.createStatement(); 
		ResultSet result = statement.executeQuery(query);){ 
			while(result.next()){ 
				Knowledge article = new Knowledge();

				article.setArticleid(result.getInt(1));
				article.setOriginalissue(result.getString(2));
				article.setDescription(result.getString(3));
				article.setResolvedetails(result.getString(4));
				article.setCategory(result.getString(5));
				article.setSubcategory(result.getString(6));
				article.setDatesolved(result.getString(7));
				
				articles.add(article);
				
			}
		}
		catch(SQLException e){
			System.err.println(e.getMessage());
			System.err.println(e.getStackTrace());
		}
		return articles;
	}
	
	
	public List<Issue> getAllIssues() throws Exception{
		String query = "SELECT * FROM Issue";
		List<Issue> Issues = addListData(query);
		
		return Issues;
	}
	
	//Inserts new issue 
	public void reportIssue(Issue issue, int UserID) throws Exception {
		try {
			
			Connection connection = Config.getConnection();
			
			PreparedStatement statement = connection.prepareStatement("INSERT INTO Issue (Title, Description, DateReported, Status, Category, SubCategory, UserID, IsArticle) VALUES (?,?,?,?,?,?,?,?)");
			
			//creates current date and time and adds it to date reported
			long now = System.currentTimeMillis();
			java.sql.Timestamp timestamp = new java.sql.Timestamp(now);
			
			//setting issues from values of the issue parameter
			statement.setString(1, issue.getTitle());
			statement.setString(2, issue.getDescription());
			statement.setTimestamp(3, timestamp);
			statement.setString(4, "New");
			statement.setString(5, issue.getCategory());
			statement.setString(6, issue.getSubcategory());
			statement.setInt(7, UserID);
			statement.setBoolean(8, false);
			
			statement.executeUpdate();
		}
		catch(SQLException e){
			System.err.println(e.getMessage());
			System.err.println(e.getStackTrace());
		}
		
	}
	
	public void addArticle(Issue issue) throws Exception {
		Connection connection = Config.getConnection();
		
		try {
			PreparedStatement statement = connection.prepareStatement("INSERT INTO KnowledgeBase (OriginalIssue, Description, ResolveDetails, Category, SubCategory, DateSolved) VALUES (?,?,?,?,?,?)");
			//changes isArticle to true so it is not displayed in the add article list
			PreparedStatement changeStatement = connection.prepareStatement("UPDATE Issue SET IsArticle = true WHERE IssueID = " + issue.getIssueid() + "");
			
			//setting articles from values of the issue parameter
			statement.setString(1, issue.getTitle());
			statement.setString(2, issue.getDescription());
			statement.setString(3, issue.getResolvedetails());
			statement.setString(4, issue.getCategory());
			statement.setString(5, issue.getSubcategory());
			statement.setString(6, issue.getDateresolved());
			
			changeStatement.executeUpdate();
			statement.executeUpdate();
		}
		catch(SQLException e){
			System.err.println(e.getMessage());
			System.err.println(e.getStackTrace());
		}
			
	}
	
	public List<Issue> searchIssues(boolean isAdmin, int UserID, String keyWord) throws Exception{
		String query = "";
		//changes view of issues depending if the user is an admin or not
		if(isAdmin){
			query = "SELECT * FROM Issue WHERE Title LIKE '%"+ keyWord + "%' OR Description LIKE '%" + keyWord + "%' OR Category LIKE '%"+ keyWord + "%'";
		}
		else {
			query = "SELECT * FROM Issue WHERE Title LIKE '%"+ keyWord + "%' OR Description LIKE '%" + keyWord + "%' OR Category LIKE '%"+ keyWord + "%' AND UserID = " + UserID + "";
		}
		
		List<Issue> Issues = addListData(query);
		
		return Issues;
	}
	
	//returns list of issues that are completed/resolved
	public List<Issue> listIssues() throws Exception{
		String query = "SELECT * FROM Issue WHERE (Status = 'Completed' OR Status = 'Resolved') AND IsArticle = false";
		
		List<Issue> Issues = addListData(query);
		
		return Issues;
	}
	
	public List<Issue> getNotifications(int UserID, boolean isAdmin) throws Exception {
		String query = "";
		//displays notifications issues that are either not accpeted by the user or waiting on user to accept/decline the issue
		if(isAdmin){
			query = "SELECT * FROM Issue WHERE Status = 'Not accepted'";
		}
		else {
			query = "SELECT * FROM Issue WHERE UserID = "+ UserID + " AND Status = 'Waiting on reporter'";
		}
		
		List<Issue> Issues = addListData(query);
		
		return Issues;
	}
	
	public List<Issue> sortByCategory(boolean isAdmin, int UserID, String Category) throws Exception {
		String query = "";
		//IF the user is admin returns all issues by a category otherwise sort issues from that specific user
		if(isAdmin){
			query = "SELECT * FROM Issue ORDER BY Category LIKE '%" + Category + "%' DESC";
		}
		else {
			query =  "SELECT * FROM Issue WHERE UserID = "+ UserID + " ORDER BY Category LIKE '%" + Category + "%' DESC";
		}
		
		List<Issue> Issues = addListData(query);
		
		return Issues;
	}
	
	public List<Issue> sortByStatus(boolean isAdmin, int UserID, String Status) throws Exception {
		String query = "";
		//IF the user is admin returns all issues by a category otherwise sort issues from that specific user
		if(isAdmin){
			query = "SELECT * FROM Issue ORDER BY Status LIKE '%" + Status + "%' DESC";
		}
		else {
			query =  "SELECT * FROM Issue WHERE UserID = "+ UserID + " ORDER BY Status LIKE '%" +Status + "%' DESC";
		}
		
		List<Issue> Issues = addListData(query);
		
		return Issues;
	}
	
	public List<Issue> sortByDate(boolean isAdmin, int UserID, String date) throws Exception {
		String query = "";
		//IF the user is admin returns all issues by a category otherwise sort issues from that specific user
		if(isAdmin){
			query = "SELECT * FROM Issue ORDER BY DateReported " + date + "";
		}
		else {
			query =  "SELECT * FROM Issue WHERE UserID = "+ UserID + " ORDER BY DateReported " + date + "";
		}

		List<Issue> Issues = addListData(query);
		
		return Issues;
	}
	
	//Change status of the issue from user or admin
	public void changeStatus(String Status, int ID) throws Exception{
		Connection connection = Config.getConnection();
		//if equal complete or resolved add dateSolved
		if(Status.equals("Completed") || Status.equals("Resolved")){
			long now = System.currentTimeMillis();
			java.sql.Timestamp timestamp = new java.sql.Timestamp(now);
			PreparedStatement state = connection.prepareStatement("UPDATE Issue SET DateResolved = '"+ timestamp + "' WHERE IssueID = " + ID + "");
			state.executeUpdate();
		}
		PreparedStatement statement = connection.prepareStatement("UPDATE Issue SET Status = '"+ Status + "' WHERE IssueID = " + ID + "");
		statement.executeUpdate();
	}
	
	
	//add comment using comment created in issueController
	public void addComment(Comment comment) throws Exception {
		
		try {
		Connection connection = Config.getConnection();
		PreparedStatement statement = connection.prepareStatement("INSERT INTO IssueComment (Title, Field, IssueID) VALUES (?,?,?)");
		
		statement.setString(1, comment.getTitle());
		statement.setString(2, comment.getField());
		statement.setInt(3, comment.getIssueid());

		statement.executeUpdate();
		}
		catch(SQLException e){
			System.err.println(e.getMessage());
			System.err.println(e.getStackTrace());
		}
		
			
	}
	//add comment using comment created in articleController
	public void addArticleComment(ArticleComment comment) throws Exception {
		
		try {
		Connection connection = Config.getConnection();
		PreparedStatement statement = connection.prepareStatement("INSERT INTO ArticleComment (Title, Field, ArticleID) VALUES (?,?,?)");
		
		statement.setString(1, comment.getTitle());
		statement.setString(2, comment.getField());
		statement.setInt(3, comment.getArticleid());

		statement.executeUpdate();
		}
		catch(SQLException e){
			System.err.println(e.getMessage());
			System.err.println(e.getStackTrace());
		}
		
			
	}
	//returns all issues relating to the specific issue
	public List<Comment> getComments(int IssueID) throws Exception {
		String query = "SELECT ic.Title, ic.Field FROM IssueComment ic, Issue i WHERE "+ IssueID + " = ic.IssueID AND i.IssueID = "+ IssueID + "";
		List<Comment> comments = new LinkedList<>();
		try(Connection connection = Config.getConnection();
		Statement statement = connection.createStatement(); 
		ResultSet result = statement.executeQuery(query);){ 
			while(result.next()){ 
				Comment comment = new Comment();

				comment.setTitle(result.getString(1));
				comment.setField(result.getString(2));
				
				comments.add(comment);
				
			}
		}
		catch(SQLException e){
			System.err.println(e.getMessage());
			System.err.println(e.getStackTrace());
		}
		return comments;
	}
	//returns article comments from the article
	public List<ArticleComment> getArticleComments(int ArticleID) throws Exception {
		String query = "SELECT ic.Title, ic.Field FROM ArticleComment ic, KnowledgeBase i WHERE i.ArticleID = ic.ArticleID AND ic.ArticleID = "+ ArticleID + "";
		List<ArticleComment> comments = new LinkedList<>();
		try(Connection connection = Config.getConnection();
		Statement statement = connection.createStatement(); 
		ResultSet result = statement.executeQuery(query);){ 
			while(result.next()){ 
				ArticleComment comment = new ArticleComment();

				comment.setTitle(result.getString(1));
				comment.setField(result.getString(2));
				
				comments.add(comment);
				
			}
		}
		catch(SQLException e){
			System.err.println(e.getMessage());
			System.err.println(e.getStackTrace());
		}
		return comments;
	}
	//assort via ascending or decending using drop-down list
	public static List<Knowledge> sortArticles(String Category) throws Exception{
		String query =  "SELECT * FROM KnowledgeBase ORDER BY Category LIKE '%" + Category + "%' DESC";
		List<Knowledge> articles = new LinkedList<>();
		try(Connection connection = Config.getConnection();
		Statement statement = connection.createStatement(); 
		ResultSet result = statement.executeQuery(query);){ 
			while(result.next()){ 
				Knowledge article = new Knowledge();

				article.setArticleid(result.getInt(1));
				article.setOriginalissue(result.getString(2));
				article.setDescription(result.getString(3));
				article.setResolvedetails(result.getString(4));
				article.setCategory(result.getString(5));
				article.setSubcategory(result.getString(6));
				article.setDatesolved(result.getString(7));
				
				articles.add(article);
				
			}
		}
		catch(SQLException e){
			System.err.println(e.getMessage());
			System.err.println(e.getStackTrace());
		}
		return articles;
	}
	
	//returns all articles where the keyword is used in the columns
	public static List<Knowledge> searchArticles(String keyWord) throws Exception{
		String query =  "SELECT * FROM KnowledgeBase WHERE OriginalIssue LIKE '%"+ keyWord + "%' OR Description LIKE '%" + keyWord + "%' OR Category LIKE '%"+ keyWord + "%' OR ResolveDetails LIKE '%"+ keyWord + "%'";
		List<Knowledge> articles = new LinkedList<>();
		try(Connection connection = Config.getConnection();
		Statement statement = connection.createStatement(); 
		ResultSet result = statement.executeQuery(query);){ 
			while(result.next()){ 
				Knowledge article = new Knowledge();

				article.setArticleid(result.getInt(1));
				article.setOriginalissue(result.getString(2));
				article.setDescription(result.getString(3));
				article.setResolvedetails(result.getString(4));
				article.setCategory(result.getString(5));
				article.setSubcategory(result.getString(6));
				article.setDatesolved(result.getString(7));
				
				articles.add(article);
				
			}
		}
		catch(SQLException e){
			System.err.println(e.getMessage());
			System.err.println(e.getStackTrace());
		}
		return articles;
	}
	
	
	//used to reduce code by taking query as a parameter where the methods require certain issues
	public List<Issue> addListData(String query) throws Exception {
		List<Issue> Issues = new LinkedList<>();
		try(Connection connection = Config.getConnection();
		Statement statement = connection.createStatement(); 
		ResultSet result = statement.executeQuery(query);){ 
			while(result.next()){ 
				Issue issue = new Issue();

				issue.setIssueid(result.getInt(1));
				issue.setTitle(result.getString(2));
				issue.setDescription(result.getString(3));
				issue.setResolvedetails(result.getString(4));
				issue.setDatereported(result.getString(5));
				issue.setDateresolved(result.getString(6));
				issue.setStatus(result.getString(7));
				issue.setCategory(result.getString(8));
				issue.setSubcategory(result.getString(9));
				
				Issues.add(issue);
				
			}
		}
		catch(SQLException e){
			System.err.println(e.getMessage());
			System.err.println(e.getStackTrace());
		}
		return Issues;
	}
}