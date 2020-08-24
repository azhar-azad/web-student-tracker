<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>

<head>
	<title>Student Tracker App</title>
	
	<link type="text/css" rel="stylesheet" href="css/style.css">
</head>

<!-- this stub is not needed as we are using jstl
<% 
	// get the students from the request object (sent by servlet)
	// List<Student> students = (List<Student>) request.getAttribute("STUDENT_LIST");
%>
 -->
 
<body>

	<div id="wrapper">
		<div id="header">
			<h2>FooBar University</h2>
		</div>
	</div>
	
	<div id="container">
	
		<div id="content">
		
			<input type="button" value="Add Student" 
					onClick="window.location.href='add-student-form.jsp'; return false;"
					class="add-student-button">
		
			<table>
			
				<thead>
					<tr>
						<th>First Name</th>
						<th>Last Name</th>
						<th>Email</th>
						<th>Action</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="student" items="${ STUDENT_LIST }">
					
						<!-- set up a link for each student (to update) -->
						<c:url var="link" value="StudentControllerServlet">
							<c:param name="command" value="LOAD"/>
							<c:param name="studentId" value="${ student.id }"/>
						</c:url>
						
						<!-- set up a link for each student (to delete) -->
						<c:url var="deleteLink" value="StudentControllerServlet">
							<c:param name="command" value="DELETE"/>
							<c:param name="studentId" value="${ student.id }"/>
						</c:url>
					
						<tr>
							<td>${ student.firstName }</td>
							<td>${ student.lastName }</td>
							<td>${ student.email }</td>
							<td>
								<a href="${ link }">Update</a> 
								|
								<a href="${ deleteLink }" 
									onclick="if(!(confirm('Are you sure you want to delete this student?'))) return false">Delete</a>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			
			</table>
			
		</div>
		
	</div>

</body>

</html> 