<%-- Import the Core taglib--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE>

<html>
	<head>
		<title>Knowledge Menu </title>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.0/jquery.min.js"></script>
		<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>
	</head>
<body>
<%@include file="../UserMaster.jsp"%>

<h1> Knowledge Menu </h1>
<form action="Knowledge" method="POST">
	<div class="container">
		<div class="row">
			<div class="col-md-12">
				<input type="submit" name="addArticle" value="Add An Article"/>
			</div>
			<br /><br />
			<div class="col-xs-12">
				<input type="submit" name="viewArticles" value="View Articles"/>
			</div>
		</div>
	</div>
</form>
</body>
</html>