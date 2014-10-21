package com.biit.webforms.gui.webpages.formmanager;

import com.biit.abcd.persistence.entity.SimpleFormView;
import com.biit.webforms.gui.common.components.TreeTableProvider;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

public class WindowSimpleViewAbcdForms extends WindowAcceptCancel {
	private static final long serialVersionUID = 4894722469159293545L;
	private static final String WINDOW_WIDTH = "60%";
	private static final String WINDOW_HEIGHT = "80%";

	private final TreeTableSimpleViewAbcdForm table;

	protected TreeTableSimpleViewAbcdForm getTable() {
		return table;
	}

	public WindowSimpleViewAbcdForms(TreeTableProvider<SimpleFormView> treeTableProvider) {
		super();

		table = new TreeTableSimpleViewAbcdForm(treeTableProvider);

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

	protected Component generateContent() {
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

	public SimpleFormView getForm() {
		return (SimpleFormView) table.getForm();
	}

	public void setValue(SimpleFormView linkedSimpleAbcdForm) {
		table.setValue(linkedSimpleAbcdForm);
	}
}
