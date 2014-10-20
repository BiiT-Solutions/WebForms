package com.biit.webforms.gui.webpages.formmanager;

import com.biit.abcd.persistence.entity.Form;
import com.biit.webforms.gui.common.components.TreeTableProvider;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

public class WindowAbcdForms extends WindowAcceptCancel {
	private static final long serialVersionUID = 4894722469159293545L;
	private static final String WINDOW_WIDTH = "60%";
	private static final String WINDOW_HEIGHT = "80%";

	private final TreeTableAbcdForm table;

	public WindowAbcdForms(TreeTableProvider<Form> treeTableProvider) {
		super();

		table = new TreeTableAbcdForm(treeTableProvider);

		configure();
		setContent(generateContent());
		init();
	}

	private void init() {
		updateAcceptButton();
	}

	/**
	 * Update enabled state of accept button
	 */
	private void updateAcceptButton() {
		getAcceptButton().setEnabled(table.getValue() != null);
	}

	private void configure() {
		setWidth(WINDOW_WIDTH);
		setHeight(WINDOW_HEIGHT);
		setResizable(false);
	}

	private Component generateContent() {
		VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setSizeFull();

		table.setSizeFull();
		table.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -7965120368151082421L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				updateAcceptButton();
			}
		});

		rootLayout.addComponent(table);
		return rootLayout;
	}

	public com.biit.abcd.persistence.entity.Form getForm() {
		return (Form) table.getForm();
	}
}
