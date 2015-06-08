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
package es.ugr.rodgom.ludiuca.client.student;

import java.util.List;

import es.ugr.rodgom.ludiuca.shared.Course;
import es.ugr.rodgom.ludiuca.client.Student;

public final class ContextStudent {

	private static Shortcuts shortCuts;
	private static CentralPan centralPan;
	private static NavBar navBar;
	private static TopPanel topPanel;
	private static Student student;
	private static List<Course> coursesList;
	private static int courseId;
	private static Courses courses;
	
	public static final int MYCOURSES = 0;
	public static final int LOGROS = 1;
	public static final int CLASIFICACION = 2;
	public static final int CONFIGURACION = 3;
	public static final int EMPTY = 4;
	public static final int GIVEPOINTS =5;

	private ContextStudent() {

	}

	public static void setShortCuts(Shortcuts shortCuts) {
		ContextStudent.shortCuts = shortCuts;
	}

	public static void setCentralPan(CentralPan centralPan) {
		ContextStudent.centralPan = centralPan;
	}

	public static void setNavBar(NavBar navBar) {
		ContextStudent.navBar = navBar;
	}

	public static void setTopPanel(TopPanel topPanel) {
		ContextStudent.topPanel = topPanel;
	}

	public static Shortcuts getShortCuts() {
		return ContextStudent.shortCuts;
	}

	public static CentralPan getCentralPan() {
		return ContextStudent.centralPan;
	}

	public static NavBar getNavBar() {
		return ContextStudent.navBar;
	}

	public static TopPanel getTopPanel() {
		return ContextStudent.topPanel;
	}

	public static Student getStudent() {
		return student;
	}

	public static void setStudent(Student student) {
		ContextStudent.student = student;
	}

	public static List<Course> getCoursesList() {
		return coursesList;
	}

	public static void setCoursesList(List<Course> coursesList) {
		ContextStudent.coursesList = coursesList;
	}

	public static int getCourseId() {
		return courseId;
	}

	public static void setCourseId(int courseId) {
		ContextStudent.courseId = courseId;
	}

	public static Courses getCourses() {
		return courses;
	}

	public static void setCourses(Courses courses) {
		ContextStudent.courses = courses;
	}
}
