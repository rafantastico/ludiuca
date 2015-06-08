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

import com.google.gwt.user.client.rpc.AsyncCallback;

import es.ugr.rodgom.ludiuca.client.student.CourseListStud;

public interface MessageServiceAsync {
   void isSignedIn(String user, String pass, String type, AsyncCallback<Message> callback);
   void isSignedIn(String input, AsyncCallback<Boolean> callback);
   void getCourses(String idTeacher, AsyncCallback<CourseList> callback);
   void getStudents(int idCourse, AsyncCallback<StudentList> callback);
   void insertTask(Task task, AsyncCallback<Void> callback);
   void getTask(int courseId, AsyncCallback<TaskList> callback);
   void givePoints(int idTask, StudentList studentList, int value, AsyncCallback<Void> callback);
   void getPoints(String idTeacher, int idCourse, int possible, AsyncCallback<TaskList> callback);
   void getClassificationTeacher(AsyncCallback<TeacherList> callback);
   
   //RPC for students app
   void getCoursesStud(String idStudent, AsyncCallback<CourseListStud> callback);
   void getTeachers(int idCourse, AsyncCallback<TeacherList> callback);
   void getAwardTeacher(int courseId, AsyncCallback<TaskList> callback);
   void givePointsStud(int id, TeacherList selectedTeachers, int value, AsyncCallback<Void> callback);
   //void getAllTaskList(String id, AsyncCallback<TaskList> callback);
   void getFilterTask(String idStudent, String awardType, int idCourse, int possible,AsyncCallback<TaskList> callback);
   void getClassification(String idStudent,String filter, AsyncCallback<StudentList> callback);


   
}
