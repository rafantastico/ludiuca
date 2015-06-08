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

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HTMLTable.Cell;

import es.ugr.rodgom.ludiuca.client.Student;
import es.ugr.rodgom.ludiuca.client.StudentList;
import es.ugr.rodgom.ludiuca.client.Task;
import es.ugr.rodgom.ludiuca.client.TaskList;
import es.ugr.rodgom.ludiuca.client.MessageService;
import es.ugr.rodgom.ludiuca.client.MessageServiceAsync;
import es.ugr.rodgom.ludiuca.client.Teacher;
import es.ugr.rodgom.ludiuca.client.TeacherList;
import es.ugr.rodgom.ludiuca.shared.Course;

public class CentralPan extends Composite {

	public interface Listener {
		void onItemSelected(Teacher item);
	}

	interface Binder extends UiBinder<Widget, CentralPan> {
	}

	interface SelectionStyle extends CssResource {
		String selectedRow();
		String goldRow();
		String silverRow();
		String bronzeRow();
		String boldRow();
	}

	private static final Binder binder = GWT.create(Binder.class);
	private MessageServiceAsync messageService = GWT
			.create(MessageService.class);
	static final int VISIBLE_ALUMNO_COUNT = 20;
	static final int VISIBLE_TASK_COUNT = 20;

	@UiField
	FlexTable header;
	@UiField
	FlexTable table;
	@UiField
	FlexTable navBarTable;
	@UiField
	SelectionStyle selectionStyle;
	@UiField
	Button puntosButton;
	@UiField
	DeckPanel deck;
	@UiField
	Button backButton;
	@UiField
	FlexTable selectedTable;
	@UiField
	ListBox listBoxTask;
	@UiField
	ListBox pointsListBox;
	@UiField
	Button givePointsButton;
	@UiField
	FlexTable taskTable;
	@UiField
	FlexTable classificationTable;
	@UiField
	ListBox listBoxCourse;
	@UiField
	Button filtButton;
	@UiField
	Button filtButtonClas;
	@UiField
	ListBox listBoxFilt;
	@UiField
	ListBox listBoxType;
	@UiField
	RadioButton possibleRadio;
	@UiField
	RadioButton achievedRadio;
	
	private Listener listener;
	private int startIndex, selectedRow = -1;
	private ArrayList<Integer> selectedRows;
	private NavBar navBarHed;
	private NavBar navBar;
	private TeacherList teacherList;
	private TeacherList selectedTeachers;
	private StudentList studentList;
	private TaskList taskList;
	private TaskList allTaskList;

	public CentralPan() {
		initWidget(binder.createAndBindUi(this));
		navBar = new NavBar(this);
		navBarHed = new NavBar(this);
		teacherList = new TeacherList();
		selectedRows = new ArrayList<Integer>();
		deck.showWidget(ContextStudent.EMPTY);
		initTable();
		// update();
	}

	public void setTeacherList(TeacherList teacherList) {
		this.teacherList = teacherList;
	}

	public TeacherList getTeacherList() {
		return this.teacherList;
	}
	
	public TaskList getAllTaskList(){
		return this.allTaskList;
	}
	
	public void setAllTaskList(TaskList allTaskList){
		this.allTaskList = allTaskList;
	}

	public void setListener(Listener listener) {
		this.listener = listener;
	}

	@Override
	protected void onLoad() {
		// Select the first row if none is selected.
		if (selectedRow == -1) {
			selectRow(0);
		}
	}

	void newer() {
		// Move back a page.
		startIndex -= VISIBLE_ALUMNO_COUNT;
		if (startIndex < 0) {
			startIndex = 0;
		} else {
			styleRow(selectedRow, false);
			selectedRow = -1;
			update();
		}
	}

	void older() {
		// Move forward a page.
		startIndex += VISIBLE_ALUMNO_COUNT;
		if (startIndex >= teacherList.getTeacherList().size()) {
			startIndex -= VISIBLE_ALUMNO_COUNT;
		} else {
			styleRow(selectedRow, false);
			selectedRow = -1;
			update();
		}
	}

	@UiHandler("table")
	void onTableClicked(ClickEvent event) {
		// Select the row that was clicked (-1 to account for header row).
		Cell cell = table.getCellForEvent(event);
		if (cell != null) {
			int row = cell.getRowIndex();
			selectRow(row);
		}
	}

	private void initTable() {

		// setTextHeader();
		setEmptyHeader();

		// Initialize the table.;
		table.getColumnFormatter().setWidth(0, "60px");
		table.getColumnFormatter().setWidth(1, "192px");
		table.getColumnFormatter().setWidth(2, "120px");
		// table.getColumnFormatter().setWidth(3, "35px");
		
		classificationTable.getColumnFormatter().setWidth(0, "60px");
		classificationTable.getColumnFormatter().setWidth(1, "192px");
		classificationTable.getColumnFormatter().setWidth(2, "120px");

		selectedTable.getColumnFormatter().setWidth(0, "60px");
		selectedTable.getColumnFormatter().setWidth(1, "192px");
		selectedTable.getColumnFormatter().setWidth(2, "120px");

		taskTable.getColumnFormatter().setWidth(0, "60px");
		taskTable.getColumnFormatter().setWidth(1, "120px");
		taskTable.getColumnFormatter().setWidth(2, "120px");
		taskTable.getColumnFormatter().setWidth(3, "400px");
	}

	public void setEmptyHeader() {
		// Initialize the header.
		header.getColumnFormatter().setWidth(0, "60px");
		header.getColumnFormatter().setWidth(1, "192px");
		header.getColumnFormatter().setWidth(2, "120px");
		header.getColumnFormatter().setWidth(3, "35px");
		// Set the text of the header
		header.setText(0, 0, "");
		header.setText(0, 1, "");
		header.setText(0, 2, "");
		header.setText(0, 3, "");
		header.setText(0, 4, "");
		header.getCellFormatter().setHorizontalAlignment(0, 4,
				HasHorizontalAlignment.ALIGN_RIGHT);
	}

	public void setTextHeader() {
		// Initialize the header.
		header.getColumnFormatter().setWidth(0, "60px");
		header.getColumnFormatter().setWidth(1, "192px");
		header.getColumnFormatter().setWidth(2, "120px");
		header.getColumnFormatter().setWidth(3, "35px");
		// Set the text of the header
		header.setText(0, 0, "");
		header.setText(0, 1, "Apellidos");
		header.setText(0, 2, "Nombre");
		header.setText(0, 3, "Puntos");
		header.setWidget(0, 4, navBarHed);
		header.getCellFormatter().setHorizontalAlignment(0, 0,
				HasHorizontalAlignment.ALIGN_CENTER);
		header.getCellFormatter().setHorizontalAlignment(0, 4,
				HasHorizontalAlignment.ALIGN_RIGHT);
		navBarTable.setWidget(0, 0, navBar);
		navBarTable.getCellFormatter().setHorizontalAlignment(0, 0,
				HasHorizontalAlignment.ALIGN_CENTER);
		GWT.log("Antes del update");
		update();
		GWT.log("Después del update");
	}
	
	public void setTextHeaderClass() {
		// Initialize the header.
		header.getColumnFormatter().setWidth(0, "60px");
		header.getColumnFormatter().setWidth(1, "192px");
		header.getColumnFormatter().setWidth(2, "120px");
		header.getColumnFormatter().setWidth(3, "35px");
		// Set the text of the header
		header.setText(0, 0, "");
		header.setText(0, 1, "Apellidos");
		header.setText(0, 2, "Nombre");
		header.setText(0, 3, "Puntos");
		header.setWidget(0, 4, navBarHed);
		header.getCellFormatter().setHorizontalAlignment(0, 0,
				HasHorizontalAlignment.ALIGN_CENTER);
		header.getCellFormatter().setHorizontalAlignment(0, 4,
				HasHorizontalAlignment.ALIGN_RIGHT);
		navBarTable.setWidget(0, 0, navBar);
		navBarTable.getCellFormatter().setHorizontalAlignment(0, 0,
				HasHorizontalAlignment.ALIGN_CENTER);
		GWT.log("Antes del update");
		updateClass();
		GWT.log("Después del update");
	}

	public void setTextHeaderPoints() {
		// Initialize the header.
		header.getColumnFormatter().setWidth(0, "60px");
		header.getColumnFormatter().setWidth(1, "192px");
		header.getColumnFormatter().setWidth(2, "120px");
		header.getColumnFormatter().setWidth(3, "35px");
		// Set the text of the header
		header.setText(0, 0, "");
		header.setText(0, 1, "Apellidos");
		header.setText(0, 2, "Nombre");
		header.setText(0, 3, "Puntos");
		header.setText(0, 4, "");
		header.getCellFormatter().setHorizontalAlignment(0, 0,
				HasHorizontalAlignment.ALIGN_CENTER);
		header.getCellFormatter().setHorizontalAlignment(0, 4,
				HasHorizontalAlignment.ALIGN_RIGHT);
		navBarTable.getCellFormatter().setHorizontalAlignment(0, 0,
				HasHorizontalAlignment.ALIGN_CENTER);
	}

	public void setTextHeaderTasks() {
		// Initialize the header.
		header.getColumnFormatter().setWidth(0, "60px");
		header.getColumnFormatter().setWidth(1, "120px");
		header.getColumnFormatter().setWidth(2, "120px");
		header.getColumnFormatter().setWidth(3, "400px");
		//header.getColumnFormatter().setWidth(4, "120px");
		// Set the text of the header
		header.setText(0, 0, "");
		header.setText(0, 1, "Asignatura");
		header.setText(0, 2, "Tarea");
		header.setText(0, 3, "Descripción");
		header.setText(0, 4, "Puntos");
		header.getCellFormatter().setHorizontalAlignment(0, 4,
				HasHorizontalAlignment.ALIGN_LEFT);
		//header.getCellFormatter().setHorizontalAlignment(0, 0,
		//		HasHorizontalAlignment.ALIGN_CENTER);
		navBarTable.getCellFormatter().setHorizontalAlignment(0, 0,
				HasHorizontalAlignment.ALIGN_CENTER);
		updateTask();
	}

	/**
	 * Selects the given row (relative to the current page).
	 * 
	 * @param row
	 *            the row to be selected
	 */
	private void selectRow(int row) {
		// When a row (other than the first one, which is used as a header) is
		// selected, display its associated MailItem.
		Teacher item = teacherList.getTeacher(startIndex + row);
		if (item == null) {
			return;
		}
		Integer I = new Integer(row);
		if (selectedRows.contains(I)) {
			int index = selectedRows.indexOf(I);
			selectedRows.remove(index);
			styleRow(row, false);
		} else {
			selectedRows.add(new Integer(row));
			styleRow(row, true);
		}
		GWT.log("Selected Rows: " + selectedRows);
		if (listener != null) {
			listener.onItemSelected(item);
		}
	}

	public void unselectAllRows() {
		selectedRows = new ArrayList<Integer>();
		this.selectedTeachers = new TeacherList();
		for (int i = 0; i < table.getRowCount(); i++) {
			styleRow(i, false);
		}
	}

	private void styleRow(int row, boolean selected) {
		if (row != -1) {
			String style = selectionStyle.selectedRow();

			if (selected) {
				table.getRowFormatter().addStyleName(row, style);
			} else {
				table.getRowFormatter().removeStyleName(row, style);
			}
		}
	}
	
	private void updateTask() {
		// Update the older/newer buttons & label.
		int count = allTaskList.getTaskList().size();
		ArrayList<String> courseNames = allTaskList.getCourseNames();
		int max = startIndex + VISIBLE_TASK_COUNT;
		if (max > count) {
			max = count;
		}

		// Clear the table
		taskTable.removeAllRows();
		// Show the selected students.
		int i = 0;
		for (; i < VISIBLE_TASK_COUNT; ++i) {
			// Don't read past the end.
			if (startIndex + i >= allTaskList.getTaskList().size()) {
				break;
			}
			Task item = allTaskList.getTask(startIndex + i);
			// Add a new row to the table, then set each of its columns to the
			// proper values.
			// CheckBox checkBox = new CheckBox(Integer.toString(i));
			GWT.log("Tarea: "+Integer.toString(i));
			// CheckBox checkBox = new CheckBox();
			// table.setWidget(i, 0, checkBox);
			taskTable.setText(i, 0, "");
			taskTable.setText(i, 1, courseNames.get(startIndex + i));
			taskTable.setText(i, 2, item.getName());
			taskTable.setText(i, 3, item.getDescription());
			if (item.getAwardType()=="Medalla"){
				taskTable.setText(i, 4, item.getStringValue());
			}else{
				taskTable.setText(i, 4, Integer.toString(item.getValue()));
			}
			//taskTable.getCellFormatter().setHorizontalAlignment(i, 4,
			//		HasHorizontalAlignment.ALIGN_RIGHT);
		}
	}
	
	private void updateClass() {
		// Para esto se usa ClaseID
		GWT.log("Antes del studentList.getStudentList().size()");
		// Update the older/newer buttons & label.
		int count = studentList.getStudentList().size();
		int max = startIndex + VISIBLE_ALUMNO_COUNT;
		if (max > count) {
			max = count;
		}

		// Update the nav bar.
		navBar.update(startIndex, count, max);
		navBarHed.update(startIndex, count, max);
		
		
		// Clear the table
		classificationTable.removeAllRows();
		// Show the selected students.
		int i = 0;
		for (; i < VISIBLE_ALUMNO_COUNT; ++i) {
			// Don't read past the end.
			if (startIndex + i >= studentList.getStudentList().size()) {
				break;
			}
			Student item = studentList.getStudent(startIndex + i);
			// Add a new row to the table, then set each of its columns to the
			// proper values.
			// CheckBox checkBox = new CheckBox(Integer.toString(i));
			GWT.log("Antes del checkBox()");
			// CheckBox checkBox = new CheckBox();
			// table.setWidget(i, 0, checkBox);
			classificationTable.setText(i, 0, new Integer(i+1).toString());
			classificationTable.setText(i, 1, item.getSurname());
			classificationTable.setText(i, 2, item.getName());
			classificationTable.setText(i, 3, Integer.toString(item.getPoints()));
			classificationTable.getCellFormatter().setHorizontalAlignment(i, 3,
					HasHorizontalAlignment.ALIGN_LEFT);
			// checkBox.addClickHandler(handlerCheck);
			GWT.log("Después del checkBox()");
			
			if(item.getId().equals(ContextStudent.getStudent().getId())){
				String styleBold = selectionStyle.boldRow();
				classificationTable.getRowFormatter().addStyleName(i, styleBold);
			}
		}

		String styleG = selectionStyle.goldRow();
		String styleS = selectionStyle.silverRow();
		String styleB = selectionStyle.bronzeRow();
		classificationTable.getRowFormatter().addStyleName(0, styleG);
		classificationTable.getRowFormatter().addStyleName(1, styleS);
		classificationTable.getRowFormatter().addStyleName(2, styleB);
	}


	private void update() {
		// Para esto se usa ClaseID
		GWT.log("Antes del studentList.getStudentList().size()");
		// Update the older/newer buttons & label.
		int count = teacherList.getTeacherList().size();
		int max = startIndex + VISIBLE_ALUMNO_COUNT;
		if (max > count) {
			max = count;
		}

		// Update the nav bar.
		navBar.update(startIndex, count, max);
		navBarHed.update(startIndex, count, max);

		// Clear the table
		table.removeAllRows();
		// Show the selected students.
		int i = 0;
		for (; i < VISIBLE_ALUMNO_COUNT; ++i) {
			// Don't read past the end.
			if (startIndex + i >= teacherList.getTeacherList().size()) {
				break;
			}
			Teacher item = teacherList.getTeacher(startIndex + i);
			// Add a new row to the table, then set each of its columns to the
			// proper values.
			// CheckBox checkBox = new CheckBox(Integer.toString(i));
			GWT.log("Antes del checkBox()");
			// CheckBox checkBox = new CheckBox();
			// table.setWidget(i, 0, checkBox);
			table.setText(i, 0, "");
			table.setText(i, 1, item.getSurname());
			table.setText(i, 2, item.getName());
			table.setText(i, 3, Integer.toString(item.getPoints()));
			table.getCellFormatter().setHorizontalAlignment(i, 3,
					HasHorizontalAlignment.ALIGN_LEFT);
			// checkBox.addClickHandler(handlerCheck);
			GWT.log("Después del checkBox()");
		}
	}

	// TODO indicar la selección en la clase alumnos para obtener los alumnos
	// seleccionados
	/*
	 * ClickHandler handlerCheck = new ClickHandler() { public void
	 * onClick(ClickEvent event) { // A�adir a la lista de alumnos clicados
	 * System.out.println("Seleccionado un alumno"); } };
	 */

	// TODO Asignar puntos a los alumnos seleccionados
	@UiHandler("puntosButton")
	void onPuntuarClicked(ClickEvent event) {
		// Obtener los alumnos seleccionados
		ArrayList<Teacher> teachers = new ArrayList<Teacher>();
		for (int i = 0; i < selectedRows.size(); i++) {
			teachers.add(teacherList.getTeacher(selectedRows.get(i)));
		}
		selectedTeachers = new TeacherList(teachers);
		if (teachers.size() == 0) {
			Window.alert("Selecciona algún profesor al que valorar");
			return;
		}
		// Rellenar la tabla con los estudiantes seleccionados
		// Show the selected emails.
		setTextHeaderPoints();
		selectedTable.removeAllRows();
		for (int i = 0; i < selectedTeachers.getTeacherList().size(); ++i) {

			Teacher item = selectedTeachers.getTeacher(i);
			// Add a new row to the table, then set each of its columns to the
			// proper values.
			selectedTable.setText(i, 0, "");
			selectedTable.setText(i, 1, item.getSurname());
			selectedTable.setText(i, 2, item.getName());
			selectedTable.setText(i, 3, Integer.toString(item.getPoints()));
			selectedTable.getCellFormatter().setHorizontalAlignment(i, 3,
					HasHorizontalAlignment.ALIGN_LEFT);
		}

		// Obtener las tareas asociadas a este curso
		// Realizar la llamada RPC
		AsyncCallback<TaskList> callback = new AsyncCallback<TaskList>() {
			public void onFailure(Throwable caught) {
				// TODO: Do something with errors.
				Window.alert("ERROR");
			}

			public void onSuccess(TaskList result) {
				// Rellenar el listbox con las posibles tareas
				updateTaskListBox(result);
				updatePointsListBox(result.getTaskList().get(0));
			}
		};
		messageService.getAwardTeacher(ContextStudent.getCourseId(), callback);
		// Rellenar el listBox de puntos
		// Mostrarlos en una ventana a parte
		deck.showWidget(ContextStudent.GIVEPOINTS);
		// Asignarles los puntos y volver a la ventana anterior
	}

	public void updateTaskListBox(TaskList result) {
		// Set taskList to its new value
		taskList = result;
		// Clear listbox
		listBoxTask.clear();
		// Set content of the listbox
		for (Task i : result.getTaskList()) {
			listBoxTask.addItem(i.getName());
		}
	}
	
	public void updateCoursesListBox(){
		List<Course> courseList = ContextStudent.getCoursesList();
		listBoxCourse.clear();
		listBoxCourse.addItem("Todas");
		for (Course i : courseList){
			listBoxCourse.addItem(i.getName());
		}
		
	}
	
	public void updateFilterListBox(){
		List<Course> courseList = ContextStudent.getCoursesList();
		listBoxFilt.clear();
		listBoxFilt.addItem("General");
		for (Course i : courseList){
			listBoxFilt.addItem(i.getName());
		}
		
	}

	public void updatePointsListBox(Task task) {
		// Clear listbox
		pointsListBox.clear();
		ArrayList<String> pointsList = new ArrayList<String>();
		String type = task.getAwardType();
		int value = task.getValue();
		// Get the posible values of points depending on the type of the award
		switch (type) {
		case "Medalla":
			switch (value) {
			case 1:
				pointsList.add("Oro");
				break;
			case 2:
				pointsList.add("Plata");
				break;
			case 3:
				pointsList.add("Bronce");
				break;
			}
			break;
		case "Puntuación":
			for (int i = value; i >= 0; i--) {
				pointsList.add(Integer.toString(i));
			}
			break;
		}
		// Set content of the listbox
		for (String i : pointsList) {
			pointsListBox.addItem(i);
		}
	}

	@UiHandler("backButton")
	void onBackButtonClicked(ClickEvent event) {
		setTextHeader();
		unselectAllRows();
		deck.showWidget(ContextStudent.MYCOURSES);
	}

	@UiHandler("listBoxTask")
	void onChangelistBoxTask(ChangeEvent event) {
		int index = listBoxTask.getSelectedIndex();
		Task selectedTask = taskList.getTaskList().get(index);
		updatePointsListBox(selectedTask);
	}

	@UiHandler("givePointsButton")
	void onGivePointsButtonClicked(ClickEvent event) {
		// Get selected Task
		int index = listBoxTask.getSelectedIndex();
		Task selectedTask = taskList.getTaskList().get(index);
		GWT.log(Integer.toString(selectedTask.getId()));
		GWT.log(Integer.toString(selectedTeachers.getTeacherList().size()));
		// Make RPC call
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			public void onFailure(Throwable caught) {
				// TODO: Do something with errors.
				Window.alert("ERROR");
			}

			public void onSuccess(Void result) {
				Window.alert("¡Puntos asignados satisfactoriamente!");
				ContextStudent.getCourses().updateSelectedItem();
			}
		};
		int value = new Integer(pointsListBox.getItemText(pointsListBox
				.getSelectedIndex()));
		messageService.givePointsStud(selectedTask.getId(), selectedTeachers,
				value, callback);
	}
	
	@UiHandler("filtButton")
	void onFiltButtonClicked(ClickEvent event){
		//Get the state of the decorator panel
		String awardType = listBoxType.getItemText(listBoxType.getSelectedIndex());
		int indexCourse = listBoxCourse.getSelectedIndex();
		int idCourse;
		if (indexCourse == 0){
			idCourse = -1;
		}else{
			idCourse = ContextStudent.getCoursesList().get(indexCourse-1).getId();
		}
		int possible = 0;
		if (possibleRadio.getValue()){
			possible = 1;
		}
		//Send the RPC
		GWT.log("AwardType: "+awardType);
		GWT.log("idCourse: "+idCourse);
		GWT.log("Possible: "+possible);
		AsyncCallback<TaskList> callback = new AsyncCallback<TaskList>() {
			public void onFailure(Throwable caught) {
				// TODO: Do something with errors.
				Window.alert("ERROR");
			}

			public void onSuccess(TaskList result) {
				//update allTaskList
				allTaskList = result;
				//updateTask()
				updateTask();
			}
		};
		messageService.getFilterTask(ContextStudent.getStudent().getId(), awardType,idCourse,possible,callback);
	}
	
	@UiHandler("filtButtonClas")
	void onFiltButtonClasClicked(ClickEvent event){
		//Get the state of the decorator panel
		String filter = "General";
		int indexCourse = listBoxFilt.getSelectedIndex();
		if (indexCourse != 0){
			filter = new Integer(ContextStudent.getCoursesList().get(indexCourse-1).getId()).toString();
		}
		AsyncCallback<StudentList> callback = new AsyncCallback<StudentList>() {
			public void onFailure(Throwable caught) {
				// TODO: Do something with errors.
				Window.alert("ERROR");
			}

			public void onSuccess(StudentList result) {
				//update studentList
				result.sortStudents();
				studentList = result;
				//updateClass()
				updateClass();
			}
		};
		messageService.getClassification(ContextStudent.getStudent().getId(), filter, callback);
	}

	public StudentList getStudentList() {
		return studentList;
	}

	public void setStudentList(StudentList studentList) {
		this.studentList = studentList;
	}

}
