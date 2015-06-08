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

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

import es.ugr.rodgom.ludiuca.client.MessageService;
import es.ugr.rodgom.ludiuca.client.MessageServiceAsync;
import es.ugr.rodgom.ludiuca.client.StudentList;
import es.ugr.rodgom.ludiuca.client.TaskList;

/**
 * A component that displays a list of contacts.
 */
public class Panel extends Composite {

	private Tree tree;
	private MessageServiceAsync messageService = GWT
			.create(MessageService.class);

	/**
	 * Constructs a new tree widget.
	 */
	public Panel() {
		tree = new Tree();
		tree.addTextItem("Mis Logros");
		tree.addTextItem("Clasificación");

		tree.addSelectionHandler(new SelectionHandler<TreeItem>() {
			@Override
			public void onSelection(SelectionEvent<TreeItem> event) {
				TreeItem item = event.getSelectedItem();
				// expand the selected item
				String text = item.getText();
				CentralPan centralPan = ContextStudent.getCentralPan();
				switch (text){
					case "Mis Logros":
						// Show the corresponding information
						ContextStudent.getCentralPan().updateCoursesListBox();
						loadAwards();
						break;
					case "Clasificación":
						// Show the corresponding information
						ContextStudent.getCentralPan().updateFilterListBox();
						loadClassification();
						break;
				}
				ContextStudent.setCentralPan(centralPan);
			}
		});
		initWidget(tree);
	}
	
	public void loadClassification(){
		AsyncCallback<StudentList> callback = new AsyncCallback<StudentList>() {
			public void onFailure(Throwable caught) {
				Window.alert("ERROR");
			}
	
			public void onSuccess(StudentList result) {
				CentralPan centralPan = ContextStudent.getCentralPan();
				centralPan.deck.showWidget(ContextStudent.CLASIFICACION);
				centralPan.setEmptyHeader();
				result.sortStudents();
				centralPan.setStudentList(result);
				centralPan.setTextHeaderClass();
				ContextStudent.setCentralPan(centralPan);
	
			}
		};
		messageService.getClassification(ContextStudent.getStudent().getId(),"General",callback);
	}
	
	public void loadAwards(){
		AsyncCallback<TaskList> callback = new AsyncCallback<TaskList>() {
			public void onFailure(Throwable caught) {
				Window.alert("ERROR");
			}
	
			public void onSuccess(TaskList result) {
				CentralPan centralPan = ContextStudent.getCentralPan();
				centralPan.setAllTaskList(result);
				centralPan.setTextHeaderTasks();
				centralPan.deck.showWidget(ContextStudent.LOGROS);
				ContextStudent.setCentralPan(centralPan);
	
			}
		};
		messageService.getFilterTask(ContextStudent.getStudent().getId(),"Todos",-1,0, callback);
	}
}
