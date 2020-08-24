package com.azad.learning.jdbc;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;




/**
 * Servlet implementation class TestServlet
 */
@WebServlet("/TestServlet")
public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// Define datasource/connection pool for Resource Injection
	@Resource(name = "jdbc/web_student_tracker")
	private DataSource dataSource;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// setup the printwriter
		PrintWriter out = response.getWriter();
		response.setContentType("text/plain");
		
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		
		try {
			// get a connection to the database
			connection = dataSource.getConnection();
			
			// create a sql statement
			statement = connection.createStatement();
			
			// execute sql query
			String sql = "select * from student";
			resultSet = statement.executeQuery(sql);
			
			// process the result set
			while (resultSet.next()) {
				String email = resultSet.getString("email");
				out.println(email);
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		
		
	}

}
