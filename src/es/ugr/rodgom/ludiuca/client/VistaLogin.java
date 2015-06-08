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

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.Label;

import es.ugr.rodgom.ludiuca.client.profesor.Context;
import es.ugr.rodgom.ludiuca.client.profesor.ProfesorGUI;
import es.ugr.rodgom.ludiuca.client.student.ContextStudent;
import es.ugr.rodgom.ludiuca.client.student.StudentGUI;

public class VistaLogin extends Composite {

	private static VistaLoginUiBinder uiBinder = GWT
			.create(VistaLoginUiBinder.class);
	//final private MessageService ludiucaService = GWT.create(MessageService.class);
	private MessageServiceAsync messageService = GWT.create(MessageService.class);
	interface VistaLoginUiBinder extends UiBinder<Widget, VistaLogin> {
	}

	public VistaLogin() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiField
	Button buttonProfesor;
	@UiField
	Button buttonAlumno;
	@UiField
	PasswordTextBox password;
	@UiField
	TextBox nombre;
	@UiField
	Label answerServer;
	

	@UiHandler("buttonProfesor")
	void onClickProfesor(ClickEvent e) {
	    AsyncCallback<Message> callback = new AsyncCallback<Message>() {
		      public void onFailure(Throwable caught) {
		        // TODO: Do something with errors.
		    	  Window.alert("ERROR");
		      }

		      public void onSuccess(Message result) {
		    	  if (result.getMessage().equals("yes")){
		    		  RootPanel.get().clear();
		    		  Context.setTeacher(result.getTeacher());
		    		  new ProfesorGUI();
		    	  }
		    	  else{
		    		  answerServer.setText("Nombre de usuario o contraseña equivocado. ¿Te has logueado en el sistema?");
		    	  }
		      }
		    };
			messageService.isSignedIn(nombre.getValue(), password.getValue(),"profesor",
			         callback);
	}
	
	@UiHandler("buttonAlumno")
	void onClickAlumno(ClickEvent e) {
		AsyncCallback<Message> callback = new AsyncCallback<Message>() {
		      public void onFailure(Throwable caught) {
		        // TODO: Do something with errors.
		    	  Window.alert("ERROR");
		      }

		      public void onSuccess(Message result) {
		    	  if (result.getMessage().equals("yes")){
		    		  //RootPanel.get().clear();
		    		  //Context.setStudent(result.getStudent());
		    		  //new ProfesorGUI();
		    		  RootPanel.get().clear();
		    		  ContextStudent.setStudent(result.getStudent());
		    		  new StudentGUI();
		    	  }
		    	  else{
		    		  answerServer.setText("Nombre de usuario o contraseña equivocado. ¿Te has logueado en el sistema?");
		    	  }
		      }
		    };
			messageService.isSignedIn(nombre.getValue(), password.getValue(),"alumno",
			         callback);
	}
	
}
