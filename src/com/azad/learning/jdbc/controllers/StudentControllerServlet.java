package com.azad.learning.jdbc.controllers;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.azad.learning.jdbc.dao.StudentDbUtil;
import com.azad.learning.jdbc.models.Student;

/**
 * Servlet implementation class StudentControllerServlet
 */
@WebServlet("/StudentControllerServlet")
public class StudentControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private StudentDbUtil studentDbUtil;

	@Resource(name = "jdbc/web_student_tracker")
	private DataSource dataSource;

	@Override
	public void init() throws ServletException {
		super.init();
		
		// create our student db util ... and pass in connection pool/datasource
		try {
			studentDbUtil = new StudentDbUtil(dataSource);
		}
		catch (Exception e) {
			throw new ServletException(e);
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		try {
			// read the command parameter
			String command = request.getParameter("command");
			
			// if the command is missing, then default to listing students
			if (command == null) {
				command = "LIST";
			}
			
			// route to the appropriate method
			switch (command) {
			
				case "LIST":
					listStudents(request, response);
					break;
					
				case "ADD":
					addStudent(request, response);
					break;
					
				case "LOAD":
					loadStudent(request, response);
					break;
					
				case "UPDATE":
					updateStudent(request, response);
					break;
					
				case "DELETE":
					deleteStudent(request, response);
					break;
					
				default: 
					listStudents(request, response);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void deleteStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String studentId = request.getParameter("studentId");
		
		studentDbUtil.deleteStudent(studentId);
		
		listStudents(request, response);
	}

	private void updateStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// read student from data
		int id = Integer.parseInt(request.getParameter("studentId"));
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		
		// create new student object
		Student student = new Student(id, firstName, lastName, email);
		
		// add the student to the database
		studentDbUtil.updateStudent(student);
		
		// send back to main page (list page)
		listStudents(request, response);
	}

	private void loadStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		// read student id from link
		String studentId = request.getParameter("studentId");
		
		// get student from db (db util)
		Student student = studentDbUtil.getStudent(studentId);
		
		// place the student in the request attribute
		request.setAttribute("STUDENT", student);
		
		// send to jsp page: update-student-form.jsp
		RequestDispatcher dispatcher = request.getRequestDispatcher("/update-student-form.jsp");
		dispatcher.forward(request, response);
	}

	private void addStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// read student from data
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		
		// create new student object
		Student student = new Student(firstName, lastName, email);
		
		// add the student to the database
		studentDbUtil.addStudent(student);
		
		// send back to main page (list page)
		listStudents(request, response);
	}

	private void listStudents(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// get students from db util
		List<Student> students = studentDbUtil.getStudents();
		
		// add students to request object as attribute
		request.setAttribute("STUDENT_LIST", students);
		
		// send to JSP page (view) via request dispatcher
		RequestDispatcher dispatcher = request.getRequestDispatcher("/list_students.jsp");
		dispatcher.forward(request, response);
	}

}
