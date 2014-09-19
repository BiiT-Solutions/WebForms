package com.biit.webforms.gui.webpages.floweditor;

import com.biit.form.TreeObject;
import com.biit.webforms.authentication.UserSessionHandler;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Answer;
import com.biit.webforms.persistence.entity.exceptions.ReferenceNotPertainsToForm;
import com.biit.webforms.utils.lexer.TokenTypes;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.RichTextArea;

public class ConditionEditor extends CustomComponent {
	private static final long serialVersionUID = -4957758105459476797L;

	private String value;
	private RichTextArea richTextArea;
	private ConditionEditorControls controls;

	public ConditionEditor() {
		super();
		setSizeFull();
		setCompositionRoot(generateComposition());
		initializeComposition();
	}

	/**
	 * Initialize composition default values
	 */
	private void initializeComposition() {
		// TODO Auto-generated method stub
	}

	/**
	 * Generate Vaadin composition
	 * 
	 * @return
	 */
	private Component generateComposition() {
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSizeFull();
		horizontalLayout.setSpacing(true);

		richTextArea = new RichTextArea();
		richTextArea.setSizeFull();
		richTextArea.addStyleName("richTextArea-noControls");

		controls = new ConditionEditorControls();
		controls.setSizeFull();
		controls.addInsertTokenListener(new InsertTokenListener() {

			@Override
			public void insert(TreeObject currentTreeObjectReference) {
				append(currentTreeObjectReference);
			}

			@Override
			public void insert(TokenTypes type) {
				append(type.getStringForm());
			}
		});

		horizontalLayout.addComponent(richTextArea);
		horizontalLayout.addComponent(controls);

		horizontalLayout.setExpandRatio(richTextArea, 0.70f);
		horizontalLayout.setExpandRatio(controls, 0.30f);
		return horizontalLayout;
	}

	private void append(String value) {
		this.value += value;
		richTextArea.setValue(this.value);
	}

	private void append(TreeObject reference) {
		// If reference is a Answer, we insert a equals statement
		if(reference instanceof Answer){
			append(reference.getParent());
			append(TokenTypes.WHITESPACE.getStringForm()+TokenTypes.EQ.getStringForm()+TokenTypes.WHITESPACE.getStringForm());
			append("<"+reference.getName()+">");
		}else{
			try {
				UserSessionHandler.getController().getFormInUse().getReference(reference);
			} catch (ReferenceNotPertainsToForm e) {
				// Impossible
				WebformsLogger.errorMessage(this.getClass().getName(), e);
			}
		}		
	}

	public void clean() {
		// TODO Auto-generated method stub
		richTextArea.setValue("");
	}

}
