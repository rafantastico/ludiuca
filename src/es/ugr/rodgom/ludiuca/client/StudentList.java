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
package es.ugr.rodgom.ludiuca.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class StudentList implements Serializable {

	private static final long serialVersionUID = 1L;
	private ArrayList<Student> students;

	public StudentList() {
		this.students = new ArrayList<Student>();

	}

	public StudentList(ArrayList<Student> students) {
		this.students = students;
	}

	public ArrayList<Student> getStudentList() {
		return students;
	}

	public void setStudentList(ArrayList<Student> students) {
		this.students = students;
	}

	public Student getStudent(int index) {
		if (index >= students.size()) {
			return null;
		}
		return students.get(index);
	}
	
	public void sortStudents(){
		Collections.sort(students, new CustomComparator());
	}
	
}
