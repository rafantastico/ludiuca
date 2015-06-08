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

public class TeacherList implements Serializable  {
	private static final long serialVersionUID = 1L;
	private ArrayList<Teacher> teachers;

	public TeacherList() {
		this.setTeacherList(new ArrayList<Teacher>());

	}

	public TeacherList(ArrayList<Teacher> teachers) {
		this.setTeacherList(teachers);
	}

	public ArrayList<Teacher> getTeacherList() {
		return teachers;
	}

	public void setTeacherList(ArrayList<Teacher> teachers) {
		this.teachers = teachers;
	}
	
	public Teacher getTeacher(int index) {
		if (index >= teachers.size()) {
			return null;
		}
		return teachers.get(index);
	}

	public void sortTeacher() {
		Collections.sort(teachers, new CustomComparatorTeacher());
	}
}
