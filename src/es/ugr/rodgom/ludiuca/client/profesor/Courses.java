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

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

import es.ugr.rodgom.ludiuca.client.CourseList;
import es.ugr.rodgom.ludiuca.client.MessageService;
import es.ugr.rodgom.ludiuca.client.MessageServiceAsync;
import es.ugr.rodgom.ludiuca.client.StudentList;
import es.ugr.rodgom.ludiuca.shared.Course;

/**
 * A tree displaying a set of email folders.
 */
public class Courses extends Composite {

	/**
	 * Specifies the images that will be bundled for this Composite and specify
	 * that tree's images should also be included in the same bundle.
	 */
	/*
	 * public interface Images extends Tree.Resources { ImageResource clase();
	 * 
	 * @Override
	 * 
	 * @Source("noimage.png") ImageResource treeLeaf(); }
	 */

	private Tree tree;
	private MessageServiceAsync messageService = GWT
			.create(MessageService.class);
	private List<Course> coursesList;
	private int idSelectedCourse;

	public Courses() {
		// Images images = GWT.create(Images.class);
		this.coursesList = new ArrayList<Course>();
		obtenerMisGrupos(Context.getTeacher().getId());
		tree = new Tree();

		initWidget(tree);
		tree.addSelectionHandler(new SelectionHandler<TreeItem>() {
			@Override
			public void onSelection(SelectionEvent<TreeItem> event) {
				GWT.log("Seleccionado un elemento");
				GWT.log("Antes de getselectedItem");
				TreeItem item = event.getSelectedItem();
				GWT.log("Antes de getparentItem");
				String itemText = item.getText();
				int indexCourse = getIndexCourse(itemText);
				GWT.log("Antes de id del curso");
				setIdSelectedCourse(coursesList.get(indexCourse).getId());
				GWT.log("Id del curso: "+coursesList.get(indexCourse).getId());
				updateSelectedItem();
			}
		});
		Context.setCourses(this);
	}

	private void obtenerMisGrupos(String idTeacher) {

		AsyncCallback<CourseList> callback = new AsyncCallback<CourseList>() {
			public void onFailure(Throwable caught) {
				// TODO: Do something with errors.
				Window.alert("ERROR");
			}

			public void onSuccess(CourseList result) {
				setList(result.getList());

			}
		};
		messageService.getCourses(idTeacher, callback);
	}

	public void setList(List<Course> coursesList) {
		this.coursesList = coursesList;
		Context.setCoursesList(coursesList);
		for (Course i : this.coursesList) {
			tree.addTextItem(i.getName());
		}
	}

	public List<Course> getCoursesList() {
		return this.coursesList;
	}

	public void setCoursesList(List<Course> coursesList) {
		this.coursesList = coursesList;
	}

	public int getIdSelectedCourse() {
		return idSelectedCourse;
	}

	public void setIdSelectedCourse(int idSelectedCourse) {
		Context.setCourseId(idSelectedCourse);
		this.idSelectedCourse = idSelectedCourse;
	}
	
	public void updateSelectedItem(){
		AsyncCallback<StudentList> callback = new AsyncCallback<StudentList>() {
			public void onFailure(Throwable caught) {
				Window.alert("ERROR");
			}
	
			public void onSuccess(StudentList result) {
				CentralPan centralPan = Context.getCentralPan();
				centralPan.setStudentList(result);
				centralPan.setTextHeader();
				centralPan.deck.showWidget(Context.MYCOURSES);
				Context.setCentralPan(centralPan);
	
			}
		};
		messageService.getStudents(getIdSelectedCourse(), callback);
		Context.getCentralPan().unselectAllRows();
	}
	
	public int getIndexCourse(String itemText){
		
		for (int i=0;i<tree.getItemCount();i++){
			if(itemText.equals(coursesList.get(i).getName())){
				return i;
			}
		}
		return -1;
	}
}
