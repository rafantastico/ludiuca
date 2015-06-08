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
import es.ugr.rodgom.ludiuca.client.TaskList;
import es.ugr.rodgom.ludiuca.client.TeacherList;
import es.ugr.rodgom.ludiuca.client.profesor.CentralPan;

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
		tree.addTextItem("Mis Puntos");
		tree.addTextItem("Clasificación");

		tree.addSelectionHandler(new SelectionHandler<TreeItem>() {
			@Override
			public void onSelection(SelectionEvent<TreeItem> event) {
				TreeItem item = event.getSelectedItem();
				// expand the selected item
				String text = item.getText();
				CentralPan centralPan = Context.getCentralPan();
				if (text == "Mis Puntos") {
					// Show the corresponding information
					Context.getCentralPan().updateCoursesListBox();
					loadPoints();
				} else if (text == "Clasificación") {
					// Show the corresponding information
					loadClassification();
				} 
				Context.setCentralPan(centralPan);
			}
		});
		initWidget(tree);
	}
	
	public void loadPoints(){
		AsyncCallback<TaskList> callback = new AsyncCallback<TaskList>() {
			public void onFailure(Throwable caught) {
				Window.alert("ERROR");
			}
	
			public void onSuccess(TaskList result) {
				CentralPan centralPan = Context.getCentralPan();
				centralPan.setAllTaskList(result);
				centralPan.setTextHeaderTasks();
				centralPan.deck.showWidget(Context.PANELPUNTOS);
				Context.setCentralPan(centralPan);
	
			}
		};
		messageService.getPoints(Context.getTeacher().getId(),-1, 0, callback);
	}
	
	public void loadClassification(){
		AsyncCallback<TeacherList> callback = new AsyncCallback<TeacherList>() {
			public void onFailure(Throwable caught) {
				Window.alert("ERROR");
			}
	
			public void onSuccess(TeacherList result) {
				CentralPan centralPan = Context.getCentralPan();
				centralPan.deck.showWidget(Context.CLASIFICACION);
				centralPan.setEmptyHeader();
				result.sortTeacher();
				centralPan.setTeacherList(result);
				centralPan.setTextHeaderClass();
				Context.setCentralPan(centralPan);
	
			}
		};
		messageService.getClassificationTeacher(callback);
	}
	
}
