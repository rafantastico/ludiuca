/*******************************************************************************
 * Ludiuca
 *
 * Copyright (C) (c) 2015 Ludiuca
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package es.ugr.rodgom.ludiuca.server;

import java.sql.*;
import java.util.ArrayList;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import es.ugr.rodgom.ludiuca.client.CourseList;
import es.ugr.rodgom.ludiuca.client.Message;
import es.ugr.rodgom.ludiuca.client.MessageService;
import es.ugr.rodgom.ludiuca.client.Student;
import es.ugr.rodgom.ludiuca.client.StudentList;
import es.ugr.rodgom.ludiuca.client.Task;
import es.ugr.rodgom.ludiuca.client.TaskList;
import es.ugr.rodgom.ludiuca.client.Teacher;
import es.ugr.rodgom.ludiuca.client.TeacherList;
import es.ugr.rodgom.ludiuca.client.student.CourseListStud;
import es.ugr.rodgom.ludiuca.shared.Course;

public class MessageServiceImpl extends RemoteServiceServlet implements
		MessageService {

	private static final long serialVersionUID = 1L;

	public Message isSignedIn(String user, String pass, String type) {
		String result = "no";
		Teacher teacher = null;
		Student student = null;
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = (Connection) DriverManager
					.getConnection("jdbc:sqlite:ludiuca.sqlite3");
			c.setAutoCommit(false);
			stmt = c.createStatement();
			ResultSet rs = null;
			if (type.equals("profesor")) {
				rs = stmt
						.executeQuery("SELECT DISTINCT TEACHER.ID, TEACHER.NAME, TEACHER.SURNAME, TEACHER.POINTS,TEACHER.AVATAR FROM LOGINTEACHER,TEACHER WHERE LOGINTEACHER.ID='"
								+ user
								+ "' AND LOGINTEACHER.PASS='"
								+ pass
								+ "' AND LOGINTEACHER.ID=TEACHER.ID;");
				while (rs.next()) {
					result = "yes";
					teacher = new Teacher();
					teacher.setId(rs.getString("id"));
					teacher.setName(rs.getString("name"));
					teacher.setSurname(rs.getString("surname"));
					teacher.setAvatar(rs.getInt("avatar"));
					teacher.setPoints(rs.getInt("points"));
				}
			} else {
				rs = stmt
						.executeQuery("SELECT DISTINCT STUDENT.ID, STUDENT.NAME, STUDENT.SURNAME, STUDENT.POINTS,STUDENT.AVATAR FROM LOGINSTUDENT,STUDENT WHERE LOGINSTUDENT.ID='"
								+ user
								+ "' AND LOGINSTUDENT.PASS='"
								+ pass
								+ "' AND LOGINSTUDENT.ID=STUDENT.ID;");
				while (rs.next()) {
					result = "yes";
					student = new Student();
					student.setId(rs.getString("id"));
					student.setName(rs.getString("name"));
					student.setSurname(rs.getString("surname"));
					student.setAvatar(rs.getInt("avatar"));
					student.setPoints(rs.getInt("points"));
				}
			}
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		Message message = new Message();
		message.setMessage(result);
		if (type.equals("profesor")) {
			message.setTeacher(teacher);
		} else {
			message.setStudent(student);
		}
		return message;
	}

	public Boolean isSignedIn(String input) {
		if (input.equals("rafantastico@gmail.com")) {
			return new Boolean(true);
		} else {
			return new Boolean(false);
		}
	}

	public CourseList getCourses(String idTeacher) {
		ArrayList<Course> listCourses = new ArrayList<Course>();
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = (Connection) DriverManager
					.getConnection("jdbc:sqlite:ludiuca.sqlite3");
			c.setAutoCommit(false);
			stmt = c.createStatement();
			ResultSet rs = null;
			rs = stmt
					.executeQuery("SELECT DISTINCT COURSE.ID, COURSE.NAME, COURSE.YEAR FROM COURSE,TEACHERCOURSE WHERE TEACHERCOURSE.IDTEACHER='"
							+ idTeacher
							+ "' and COURSE.ID=TEACHERCOURSE.IDCOURSE ORDER BY COURSE.NAME;");
			while (rs.next()) {
				listCourses.add(new Course(rs.getInt("id"), rs.getInt("year"),
						rs.getString("name")));
			}
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		CourseList groupList = new CourseList(listCourses);

		return groupList;
	}

	@Override
	public StudentList getStudents(int idCourse) {
		ArrayList<Student> studentList = new ArrayList<Student>();
		Connection c = null;
		Statement stmt = null;
		Statement stmt2 = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = (Connection) DriverManager
					.getConnection("jdbc:sqlite:ludiuca.sqlite3");
			c.setAutoCommit(false);
			stmt = c.createStatement();
			ResultSet rs = null;
			rs = stmt
					.executeQuery("SELECT distinct STUDENT.id, STUDENT.name,STUDENT.surname, STUDENT.points,STUDENT.avatar FROM STUDENTCOURSE,COURSE, STUDENT WHERE STUDENT.id=STUDENTCOURSE.IDSTUDENT and STUDENTCOURSE.IDCOURSE="
							+ idCourse + " ORDER BY STUDENT.surname;");
			while (rs.next()) {
				// TODO get points (sum)
				String idStudent = rs.getString("id");
				stmt2 = c.createStatement();
				ResultSet rs2 = stmt2
						.executeQuery("SELECT SUM(VALUE) FROM ( SELECT STUDENTTASK.VALUE AS VALUE FROM STUDENTTASK, TASK WHERE STUDENTTASK.IDSTUDENT='"
								+ idStudent
								+ "' AND STUDENTTASK.IDTASK=TASK.ID AND TASK.TYPE='Puntuación' AND TASK.COURSEID="
								+ idCourse + ");");
				rs2.next();
				int points = rs2.getInt(1);
				stmt2.close();
				Student student = new Student(idStudent, rs.getString("name"),
						rs.getString("surname"), points, rs.getInt("avatar"));
				studentList.add(student);

			}
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		StudentList sl = new StudentList(studentList);
		return sl;
	}

	@Override
	public void insertTask(Task task) {
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = (Connection) DriverManager
					.getConnection("jdbc:sqlite:ludiuca.sqlite3");
			c.setAutoCommit(false);
			stmt = c.createStatement();
			String sql = "SELECT COUNT(*) FROM TASK;";
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			int ID = rs.getInt(1) + 1;
			c.commit();
			stmt.close();
			stmt = c.createStatement();
			sql = "INSERT INTO TASK (ID,NAME,DESCRIPTION,TYPE,DATE,COURSEID,VALUE) "
					+ "VALUES ('"
					+ Integer.toString(ID)
					+ "','"
					+ task.getName()
					+ "','"
					+ task.getDescription()
					+ "','"
					+ task.getAwardType()
					+ "','"
					+ task.getDate()
					+ "',"
					+ task.getCourseID()
					+ ","
					+ Integer.toString(task.getValue()) + ");";
			stmt.executeUpdate(sql);
			c.commit();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}

	}

	@Override
	public TaskList getTask(int courseId) {
		ArrayList<Task> taskArray = new ArrayList<Task>();
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = (Connection) DriverManager
					.getConnection("jdbc:sqlite:ludiuca.sqlite3");
			c.setAutoCommit(false);
			stmt = c.createStatement();
			String sql = "SELECT DISTINCT TASK.COURSEID,TASK.DATE,TASK.DESCRIPTION,TASK.ID,TASK.NAME,TASK.TYPE,TASK.VALUE FROM TASK, COURSE WHERE TASK.COURSEID=COURSE.ID AND COURSE.ID="
					+ Integer.toString(courseId) + ";";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Task task = new Task(rs.getInt("ID"), rs.getString("NAME"),
						rs.getString("DESCRIPTION"), rs.getString("DATE"),
						rs.getInt("COURSEID"), rs.getString("TYPE"),
						rs.getInt("VALUE"));
				taskArray.add(task);
			}
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		TaskList taskList = new TaskList(taskArray);
		return taskList;
	}

	@Override
	public void givePoints(int idTask, StudentList studentList, int value) {
		ArrayList<Student> studentArray = studentList.getStudentList();
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = (Connection) DriverManager
					.getConnection("jdbc:sqlite:ludiuca.sqlite3");
			c.setAutoCommit(false);
			stmt = c.createStatement();
			for (Student s : studentArray) {
				// TODO only insert if this task has not received points
				// SELECT STUDENTTASK.IDSTUDENT,STUDENTTASK.IDTASK FROM
				// STUDENTTASK WHERE STUDENTTASK.IDSTUDENT='irene@mail' AND
				// STUDENTTASK.IDTASK=2;
				// Insert the rewards into studenttask
				String sql = "INSERT INTO STUDENTTASK (IDTASK,IDSTUDENT,VALUE) "
						+ "VALUES ("
						+ Integer.toString(idTask)
						+ ",'"
						+ s.getId() + "'," + Integer.toString(value) + ");";
				stmt.executeUpdate(sql);
			}
			c.commit();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}
	
	
	//RPC FOR STUDENTS
	public CourseListStud getCoursesStud(String idStudent){
		ArrayList<Course> listCourses = new ArrayList<Course>();
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = (Connection) DriverManager
					.getConnection("jdbc:sqlite:ludiuca.sqlite3");
			c.setAutoCommit(false);
			stmt = c.createStatement();
			ResultSet rs = null;
			rs = stmt
					.executeQuery("SELECT DISTINCT COURSE.ID, COURSE.NAME, COURSE.YEAR FROM COURSE, STUDENTCOURSE WHERE STUDENTCOURSE.IDSTUDENT='"
							+ idStudent
							+ "' and COURSE.ID=STUDENTCOURSE.IDCOURSE ORDER BY COURSE.NAME;");
			while (rs.next()) {
				listCourses.add(new Course(rs.getInt("id"), rs.getInt("year"),
						rs.getString("name")));
			}
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		CourseListStud groupList = new CourseListStud(listCourses);

		return groupList;
	}
	
	public TeacherList getTeachers(int idCourse){
		ArrayList<Teacher> teacherList = new ArrayList<Teacher>();
		Connection c = null;
		Statement stmt = null;
		Statement stmt2 = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = (Connection) DriverManager
					.getConnection("jdbc:sqlite:ludiuca.sqlite3");
			c.setAutoCommit(false);
			stmt = c.createStatement();
			ResultSet rs = null;
			rs = stmt
					.executeQuery("SELECT distinct TEACHER.ID, TEACHER.NAME,TEACHER.SURNAME, TEACHER.POINTS,TEACHER.AVATAR FROM TEACHERCOURSE,COURSE, TEACHER WHERE TEACHER.ID=TEACHERCOURSE.IDTEACHER and TEACHERCOURSE.IDCOURSE="
							+ idCourse + " ORDER BY TEACHER.surname;");
			while (rs.next()) {
				// TODO get points (sum)
				String idTeacher = rs.getString("id");
				stmt2 = c.createStatement();
				ResultSet rs2 = stmt2
						.executeQuery("SELECT SUM(VALUE) FROM ( SELECT TEACHERAWARD.VALUE AS VALUE FROM TEACHERAWARD, AWARDTEACHER WHERE TEACHERAWARD.IDTEACHER='"
								+ idTeacher
								+ "' AND TEACHERAWARD.IDAWARD=AWARDTEACHER.ID AND AWARDTEACHER.COURSEID="
								+ idCourse + ");");
				rs2.next();
				int points = rs2.getInt(1);
				stmt2.close();
				Teacher teacher = new Teacher(idTeacher, rs.getString("name"),
						rs.getString("surname"), points, rs.getInt("avatar"));
				teacherList.add(teacher);

			}
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		TeacherList tl = new TeacherList(teacherList);
		return tl;
	}
	
	public TaskList getAwardTeacher(int courseId){
		ArrayList<Task> taskArray = new ArrayList<Task>();
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = (Connection) DriverManager
					.getConnection("jdbc:sqlite:ludiuca.sqlite3");
			c.setAutoCommit(false);
			stmt = c.createStatement();
			String sql = "SELECT DISTINCT AWARDTEACHER.COURSEID,AWARDTEACHER.DATE,AWARDTEACHER.DESCRIPTION,AWARDTEACHER.ID,AWARDTEACHER.NAME,AWARDTEACHER.TYPE,AWARDTEACHER.VALUE FROM AWARDTEACHER, COURSE WHERE AWARDTEACHER.COURSEID=COURSE.ID AND COURSE.ID="
					+ Integer.toString(courseId) + ";";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Task task = new Task(rs.getInt("ID"), rs.getString("NAME"),
						rs.getString("DESCRIPTION"), rs.getString("DATE"),
						rs.getInt("COURSEID"), rs.getString("TYPE"),
						rs.getInt("VALUE"));
				taskArray.add(task);
			}
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		TaskList taskList = new TaskList(taskArray);
		return taskList;
	}
	
	public void givePointsStud(int id, TeacherList selectedTeachers, int value){
		ArrayList<Teacher> teacherArray = selectedTeachers.getTeacherList();
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = (Connection) DriverManager
					.getConnection("jdbc:sqlite:ludiuca.sqlite3");
			c.setAutoCommit(false);
			stmt = c.createStatement();
			for (Teacher t : teacherArray) {
				// TODO only insert if this task has not received points
				// SELECT STUDENTTASK.IDSTUDENT,STUDENTTASK.IDTASK FROM
				// STUDENTTASK WHERE STUDENTTASK.IDSTUDENT='irene@mail' AND
				// STUDENTTASK.IDTASK=2;
				// Insert the rewards into teacheraward
				String sql = "INSERT INTO TEACHERAWARD (IDAWARD,IDTEACHER,VALUE) "
						+ "VALUES ("
						+ Integer.toString(id)
						+ ",'"
						+ t.getId() + "'," + Integer.toString(value) + ");";
				stmt.executeUpdate(sql);
			}
			c.commit();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}
	
	/*public TaskList getAllTaskList(String idStudent){
		ArrayList<Task> taskArray = new ArrayList<Task>();
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = (Connection) DriverManager
					.getConnection("jdbc:sqlite:ludiuca.sqlite3");
			c.setAutoCommit(false);
			stmt = c.createStatement();
			String sql = "SELECT DISTINCT TASK.COURSEID, TASK.DATE, TASK.DESCRIPTION, TASK.ID, TASK.NAME, TASK.TYPE, STUDENTTASK.VALUE FROM TASK, STUDENTTASK WHERE STUDENTTASK.IDTASK=TASK.ID AND STUDENTTASK.IDSTUDENT='"+idStudent+"';";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Task task = new Task(rs.getInt("ID"), rs.getString("NAME"),
						rs.getString("DESCRIPTION"), rs.getString("DATE"),
						rs.getInt("COURSEID"), rs.getString("TYPE"),
						rs.getInt("VALUE"));
				taskArray.add(task);
				System.out.println("Tarea: "+task.getName());
			}
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		TaskList taskList = new TaskList(taskArray);
		return taskList;
	}*/
	
	public TaskList getFilterTask(String idStudent, String awardType, int idCourse, int possible){
		ArrayList<Task> taskArray = new ArrayList<Task>();
		ArrayList<String> courseNames = new ArrayList<String>();
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = (Connection) DriverManager
					.getConnection("jdbc:sqlite:ludiuca.sqlite3");
			c.setAutoCommit(false);
			stmt = c.createStatement();
			String sql;
			if(possible == 1){
				//Without studentID
				sql = "SELECT DISTINCT TASK.COURSEID, TASK.DATE, TASK.DESCRIPTION, TASK.ID, TASK.NAME, TASK.TYPE, TASK.VALUE, COURSE.NAME FROM TASK, COURSE WHERE COURSE.ID=TASK.COURSEID AND ";
			}else{
				//With studentID
				sql = "SELECT DISTINCT TASK.COURSEID, TASK.DATE, TASK.DESCRIPTION, TASK.ID, TASK.NAME, TASK.TYPE, STUDENTTASK.VALUE, COURSE.NAME FROM TASK, STUDENTTASK, COURSE WHERE COURSE.ID=TASK.COURSEID AND STUDENTTASK.IDTASK=TASK.ID AND STUDENTTASK.IDSTUDENT='"+idStudent+"' AND ";
			}
			if (!awardType.equals("Todos")){
				sql += "TASK.TYPE='"+awardType+"' AND";
			}
			if (idCourse == -1){
				//All the courses
				sql = sql.substring(0, sql.length()-4);
				sql += ";";
			}
			else{
				sql += " TASK.COURSEID="+idCourse+";";
			}
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Task task = new Task(rs.getInt("ID"), rs.getString(5),
						rs.getString("DESCRIPTION"), rs.getString("DATE"),
						rs.getInt("COURSEID"), rs.getString("TYPE"),
						rs.getInt("VALUE"));
				taskArray.add(task);
				courseNames.add(rs.getString(8));
				System.out.println(rs.getString(8));
				System.out.println("Tarea: "+task.getName());
			}
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		TaskList taskList = new TaskList(taskArray,courseNames);
		return taskList;
	}
	

	public StudentList getClassification(String idStudent0, String filter){
		ArrayList<Student> studentArray = new ArrayList<Student>();
		Connection c = null;
		Statement stmt = null;
		Statement stmt2 = null;
		System.out.println("Entra en getClassification");
		try {
			Class.forName("org.sqlite.JDBC");
			c = (Connection) DriverManager
					.getConnection("jdbc:sqlite:ludiuca.sqlite3");
			c.setAutoCommit(false);
			stmt = c.createStatement();
			ResultSet rs = null;
			if (filter.equals("General")){
				rs = stmt
						.executeQuery("SELECT distinct STUDENT.id, STUDENT.name,STUDENT.surname, STUDENT.points,STUDENT.avatar FROM STUDENTCOURSE,COURSE, STUDENT WHERE STUDENT.id=STUDENTCOURSE.IDSTUDENT ORDER BY STUDENT.surname;");
			}else{
				rs = stmt
						.executeQuery("SELECT distinct STUDENT.id, STUDENT.name,STUDENT.surname, STUDENT.points,STUDENT.avatar FROM STUDENTCOURSE,COURSE, STUDENT WHERE STUDENT.id=STUDENTCOURSE.IDSTUDENT and STUDENTCOURSE.IDCOURSE="
								+ filter + " ORDER BY STUDENT.surname;");
			}
			
			while (rs.next()) {
				// TODO get points (sum)
				String idStudent = rs.getString("id");
				stmt2 = c.createStatement();
				ResultSet rs2 = null; 
				if (filter.equals("General")){
					rs2 = stmt2
							.executeQuery("SELECT SUM(VALUE) FROM ( SELECT STUDENTTASK.VALUE AS VALUE FROM STUDENTTASK, TASK WHERE STUDENTTASK.IDSTUDENT='"
									+ idStudent
									+ "' AND STUDENTTASK.IDTASK=TASK.ID AND TASK.TYPE='Puntuación');");
				}else{
					rs2 = stmt2
							.executeQuery("SELECT SUM(VALUE) FROM ( SELECT STUDENTTASK.VALUE AS VALUE FROM STUDENTTASK, TASK WHERE STUDENTTASK.IDSTUDENT='"
									+ idStudent
									+ "' AND STUDENTTASK.IDTASK=TASK.ID AND TASK.TYPE='Puntuación' AND TASK.COURSEID="
									+ filter + ");");
				}
						
				rs2.next();
				int points = rs2.getInt(1);
				stmt2.close();
				Student student = new Student(idStudent, rs.getString("name"),
						rs.getString("surname"), points, rs.getInt("avatar"));
				studentArray.add(student);
				System.out.println("Estudiante: "+idStudent);

			}
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		StudentList studentList = new StudentList(studentArray);
		return studentList;
	}

	@Override
	public TaskList getPoints(String idTeacher, int idCourse, int possible) {
		ArrayList<Task> taskArray = new ArrayList<Task>();
		ArrayList<String> courseNames = new ArrayList<String>();
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = (Connection) DriverManager
					.getConnection("jdbc:sqlite:ludiuca.sqlite3");
			c.setAutoCommit(false);
			stmt = c.createStatement();
			String sql;
			if(possible == 1){
				//Without studentID
				sql = "SELECT DISTINCT AWARDTEACHER.COURSEID, AWARDTEACHER.DATE, AWARDTEACHER.DESCRIPTION, AWARDTEACHER.ID, AWARDTEACHER.NAME, AWARDTEACHER.TYPE, AWARDTEACHER.VALUE, COURSE.NAME FROM AWARDTEACHER, COURSE WHERE COURSE.ID=AWARDTEACHER.COURSEID AND ";
			}else{
				//With studentID
				sql = "SELECT DISTINCT AWARDTEACHER.COURSEID, AWARDTEACHER.DATE, AWARDTEACHER.DESCRIPTION, AWARDTEACHER.ID, AWARDTEACHER.NAME, AWARDTEACHER.TYPE, TEACHERAWARD.VALUE, COURSE.NAME FROM AWARDTEACHER, TEACHERAWARD, COURSE WHERE COURSE.ID=AWARDTEACHER.COURSEID AND TEACHERAWARD.IDAWARD=AWARDTEACHER.ID AND TEACHERAWARD.IDTEACHER='"+idTeacher+"' AND ";
			}
			if (idCourse == -1){
				//All the courses
				sql = sql.substring(0, sql.length()-4);
				sql += ";";
			}
			else{
				sql += " AWARDTEACHER.COURSEID="+idCourse+";";
			}
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Task task = new Task(rs.getInt("ID"), rs.getString(5),
						rs.getString("DESCRIPTION"), rs.getString("DATE"),
						rs.getInt("COURSEID"), rs.getString("TYPE"),
						rs.getInt("VALUE"));
				taskArray.add(task);
				courseNames.add(rs.getString(8));
				System.out.println(rs.getString(8));
				System.out.println("Tarea: "+task.getName());
			}
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		TaskList taskList = new TaskList(taskArray,courseNames);
		return taskList;
	}
	
	public TeacherList getClassificationTeacher(){
		ArrayList<Teacher> teacherArray = new ArrayList<Teacher>();
		Connection c = null;
		Statement stmt = null;
		Statement stmt2 = null;
		System.out.println("Entra en getClassificationTeacher");
		try {
			Class.forName("org.sqlite.JDBC");
			c = (Connection) DriverManager
					.getConnection("jdbc:sqlite:ludiuca.sqlite3");
			c.setAutoCommit(false);
			stmt = c.createStatement();
			ResultSet rs = null;
			rs = stmt
					.executeQuery("SELECT distinct TEACHER.id, TEACHER.name,TEACHER.surname, TEACHER.points,TEACHER.avatar FROM TEACHERCOURSE,COURSE, TEACHER WHERE TEACHER.id=TEACHERCOURSE.IDTEACHER ORDER BY TEACHER.surname;");
			while (rs.next()) {
				// TODO get points (sum)
				String idTeacher = rs.getString("id");
				stmt2 = c.createStatement();
				ResultSet rs2 = null; 
				rs2 = stmt2
						.executeQuery("SELECT SUM(VALUE) FROM ( SELECT TEACHERAWARD.VALUE AS VALUE FROM TEACHERAWARD, AWARDTEACHER WHERE TEACHERAWARD.IDTEACHER='"
								+ idTeacher
								+ "' AND TEACHERAWARD.IDAWARD=AWARDTEACHER.ID AND AWARDTEACHER.TYPE='Puntuación');");
				rs2.next();
				int points = rs2.getInt(1);
				stmt2.close();
				Teacher teacher = new Teacher(idTeacher, rs.getString("name"),
						rs.getString("surname"), points, rs.getInt("avatar"));
				teacherArray.add(teacher);
				System.out.println("Profesor: "+idTeacher);

			}
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		TeacherList teacherList = new TeacherList(teacherArray);
		return teacherList;
	}

}