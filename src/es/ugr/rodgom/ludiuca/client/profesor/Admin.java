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

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

/**
 * Composite that represents a collection of <code>Task</code> items.
 */
public class Admin extends Composite {

	private Tree tree;

	/**
	 * Constructs a new tree widget.
	 */
	public Admin() {
		tree = new Tree();
		tree.addTextItem("Crear Tarea");
		tree.addTextItem("Configuración");
		

		tree.addSelectionHandler(new SelectionHandler<TreeItem>() {
			@Override
			public void onSelection(SelectionEvent<TreeItem> event) {
				TreeItem item = event.getSelectedItem();
				// expand the selected item
				String text = item.getText();
				CentralPan centralPan = Context.getCentralPan();
				if (text == "Crear Tarea") {
					// Show the corresponding information
					centralPan.updateListBox();
					centralPan.deck.showWidget(Context.NUEVORETO);
					centralPan.setEmptyHeader();
				} else if (text == "Configuración") {
					// Show the corresponding information
					centralPan.deck.showWidget(Context.CONFIGURACION);
					centralPan.setEmptyHeader();
				}
				Context.setCentralPan(centralPan);
			}
		});
		initWidget(tree);
	}

}
