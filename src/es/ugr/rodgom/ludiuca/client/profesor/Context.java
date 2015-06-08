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
package es.ugr.rodgom.ludiuca.client.profesor;

import java.util.List;

import es.ugr.rodgom.ludiuca.client.Student;
import es.ugr.rodgom.ludiuca.client.Teacher;
import es.ugr.rodgom.ludiuca.shared.Course;

public final class Context {

	private static Shortcuts shortCuts;
	private static CentralPan centralPan;
	private static NavBar navBar;
	private static TopPanel topPanel;
	private static Teacher teacher;
	private static Student student;
	private static List<Course> coursesList;
	private static int courseId;
	private static Courses courses;
	
	public static final int MYCOURSES = 0;
	public static final int PANELPUNTOS = 1;
	public static final int CLASIFICACION = 2;
	public static final int NUEVORETO = 3;
	public static final int CONFIGURACION = 4;
	public static final int EMPTY = 5;
	public static final int GIVEPOINTS =6;

	private Context() {

	}

	public static void setShortCuts(Shortcuts shortCuts) {
		Context.shortCuts = shortCuts;
	}

	public static void setCentralPan(CentralPan centralPan) {
		Context.centralPan = centralPan;
	}

	public static void setNavBar(NavBar navBar) {
		Context.navBar = navBar;
	}

	public static void setTopPanel(TopPanel topPanel) {
		Context.topPanel = topPanel;
	}

	public static void setTeacher(Teacher teacher) {
		Context.teacher = teacher;
	}

	public static Shortcuts getShortCuts() {
		return Context.shortCuts;
	}

	public static CentralPan getCentralPan() {
		return Context.centralPan;
	}

	public static NavBar getNavBar() {
		return Context.navBar;
	}

	public static TopPanel getTopPanel() {
		return Context.topPanel;
	}

	public static Teacher getTeacher() {
		return Context.teacher;
	}

	public static Student getStudent() {
		return student;
	}

	public static void setStudent(Student student) {
		Context.student = student;
	}

	public static List<Course> getCoursesList() {
		return coursesList;
	}

	public static void setCoursesList(List<Course> coursesList) {
		Context.coursesList = coursesList;
	}

	public static int getCourseId() {
		return courseId;
	}

	public static void setCourseId(int courseId) {
		Context.courseId = courseId;
	}

	public static Courses getCourses() {
		return courses;
	}

	public static void setCourses(Courses courses) {
		Context.courses = courses;
	}
}
