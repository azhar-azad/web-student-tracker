package com.azad.learning.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.azad.learning.jdbc.models.Student;

public class StudentDbUtil {

	private DataSource dataSource;

	public StudentDbUtil(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public List<Student> getStudents() throws Exception {
		
		List<Student> students = new ArrayList<Student>();
		
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		
		try {
			// get a connection
			connection = dataSource.getConnection();
			
			// create a sql statement
			statement = connection.createStatement();
			
			// execute the sql query
			String sql = "select * from student order by last_name";
			resultSet = statement.executeQuery(sql);
			
			// process result set
			while (resultSet.next()) {
				
				// retrieve data from result set row
				int id = resultSet.getInt("id");
				String firstName = resultSet.getString("first_name");
				String lastName = resultSet.getString("last_name");
				String email = resultSet.getString("email");
				
				// create new Student object
				Student student = new Student(id, firstName, lastName, email);
				
				// add it to the list of students
				students.add(student);
			}
			
			return students;
		} 
		finally {
			// close JDBC objects
			close(connection, statement, resultSet);
		}
	}
	
	public void addStudent(Student student) throws Exception {
		
		Connection connection = null;
		PreparedStatement statement = null;
		
		try {
			// get db connection
			connection = dataSource.getConnection();
			
			// create sql for insert
			String sql = "insert into student "
						+ "(first_name, last_name, email) "
						+ "values (?, ?, ?)";
			statement = connection.prepareStatement(sql);
			
			// set the param values for the student
			statement.setString(1, student.getFirstName());
			statement.setString(2, student.getLastName());
			statement.setString(3, student.getEmail());
			
			// execute the sql insert
			statement.execute();
		}
		finally {
			// clean up JDBC objects 
			close(connection, statement, null);
		}
		
	}
	
	public Student getStudent(String studentId) throws Exception {
		
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		Student student = null;
		
		try {
			int id = Integer.parseInt(studentId);
			
			connection = dataSource.getConnection();
			
			String sql = "select * from student where id=?";
			statement = connection.prepareStatement(sql);
			
			statement.setInt(1, id);
			resultSet = statement.executeQuery();
			
			if (resultSet.next()) {
				String firstName = resultSet.getString("first_name");
				String lastName = resultSet.getString("last_name");
				String email = resultSet.getString("email");
				
				student = new Student(id, firstName, lastName, email); 
			}
			else {
				throw new Exception("Could not found student id: " + id);
			}
			
			return student;
		}
		finally {
			close(connection, statement, resultSet);
		}
	}
	
	public void updateStudent(Student student) throws Exception {
		
		Connection connection = null;
		PreparedStatement statement = null;
		
		try {
			connection = dataSource.getConnection();
			
			String sql = "update student "
					+ "set first_name=?, last_name=?, email=? "
					+ "where id=?";
			statement = connection.prepareStatement(sql);
			
			statement.setString(1, student.getFirstName());
			statement.setString(2, student.getLastName());
			statement.setString(3, student.getEmail());
			statement.setInt(4, student.getId());
			
			statement.execute();
		}
		finally {
			close(connection, statement, null);
		}
	}

	public void deleteStudent(String studentId) throws Exception {

		Connection connection = null;
		PreparedStatement statement = null;
		
		try {
			int id = Integer.parseInt(studentId);
			
			connection = dataSource.getConnection();
			
			String sql = "delete from student where id=?";
			statement = connection.prepareStatement(sql);
			
			statement.setInt(1, id);
			statement.execute();
		}
		finally {
			close(connection, statement, null);
		}
	}

	private void close(Connection connection, Statement statement, ResultSet resultSet) {
		
		try {
			if (resultSet != null) {
				resultSet.close();
			}
			
			if (statement != null) {
				statement.close();
			}
			
			if (connection != null) {
				connection.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	

	
}
