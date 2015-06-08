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
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import es.ugr.rodgom.ludiuca.client.student.CourseListStud;

@RemoteServiceRelativePath("message")
public interface MessageService extends RemoteService {
   Message isSignedIn(String user, String pass, String type);
   Boolean isSignedIn(String input);
   CourseList getCourses(String idTeacher);
   StudentList getStudents(int idCourse);
   void insertTask(Task task);
   TaskList getTask(int courseId);
   void givePoints(int idTask, StudentList studentList, int value); 
   TaskList getPoints(String idTeacher, int idCourse, int possible);
   TeacherList getClassificationTeacher();
   
   //RPC for students app
   CourseListStud getCoursesStud(String idStudent);
   TeacherList getTeachers(int idCourse);
   TaskList getAwardTeacher(int courseId);
   void givePointsStud(int id, TeacherList selectedTeachers, int value);
   //TaskList getAllTaskList(String id);
   TaskList getFilterTask(String idStudent, String awardType, int idCourse, int possible);
   StudentList getClassification(String idStudent,String filter);
}
