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
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;

/**
 * This application demonstrates how to construct a relatively complex user
 * interface, similar to many common email readers. It has no back-end,
 * populating its components with hard-coded data.
 */
public class ProfesorGUI  extends Composite {

  interface Binder extends UiBinder<DockLayoutPanel, ProfesorGUI> { }

  interface GlobalResources extends ClientBundle {
    @NotStrict
    @Source("global.css")
    CssResource css();
  }

  private static final Binder binder = GWT.create(Binder.class);
  @UiField TopPanel topPanel;
  @UiField CentralPan centralPan;
  @UiField Shortcuts shortcuts;
  

  /**
   * This method constructs the application user interface by instantiating
   * controls and hooking up event handler.
   */
  public ProfesorGUI() {
    // Inject global styles.
    GWT.<GlobalResources>create(GlobalResources.class).css().ensureInjected();
    GWT.log("Antes del binder");
    // Create the UI defined in Mail.ui.xml.
    DockLayoutPanel outer = binder.createAndBindUi(this);
    GWT.log("Despu�s del binder");
    //Cargamos el contexto
    
    Context.setCentralPan(centralPan);
    Context.setTopPanel(topPanel);
    Context.setShortCuts(shortcuts);
    GWT.log("Despu�s de actualizar el contexto");
    // Get rid of scrollbars, and clear out the window's built-in margin,
    // because we want to take advantage of the entire client area.
    Window.enableScrolling(true);
    Window.setMargin("0px");

    // Special-case stuff to make topPanel overhang a bit.
    Element topElem = outer.getWidgetContainerElement(topPanel);
    topElem.getStyle().setZIndex(2);
    topElem.getStyle().setOverflow(Overflow.VISIBLE);

    // Add the outer panel to the RootLayoutPanel, so that it will be
    // displayed.
    RootLayoutPanel root = RootLayoutPanel.get();
    //root.clear();
    root.add(outer);
  }
}

