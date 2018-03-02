package com.biit.webforms.gui.webpages.designer;

import com.biit.form.result.QuestionWithValueResult;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.language.LanguageCodes;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

public class WindowCreateAnswerRanges extends WindowAcceptCancel {
	private static final long serialVersionUID = 5008247299399153993L;
	private static final String width = "340px";
	private static final String height = "220px";

	private TextField lowerValue;
	private TextField upperValue;
	private TextField steapRange;

	public WindowCreateAnswerRanges() {
		super();
		setContent(generateContent());
		setResizable(false);
		setDraggable(false);
		setClosable(false);
		setModal(true);
		setWidth(width);
		setHeight(height);
		setCaption(LanguageCodes.WINDOW_ANSWER_RANGE_CAPTION.translation());
	}

	private Component generateContent() {
		lowerValue = new TextField(LanguageCodes.ANSWER_RANGE_LOWER_VALUE.translation());
		lowerValue.focus();
		lowerValue.setWidth("100%");
		lowerValue.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 4953347262492851075L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				lowerValue.isValid();
			}
		});

		lowerValue.setMaxLength(QuestionWithValueResult.MAX_LENGTH);
		lowerValue.addValidator(new ValidatorFloat());

		upperValue = new TextField(LanguageCodes.ANSWER_RANGE_UPPER_VALUE.translation());
		upperValue.setWidth("100%");
		upperValue.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 4953347262492851075L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				upperValue.isValid();
			}
		});
		upperValue.addValidator(new ValidatorFloat());

		steapRange = new TextField(LanguageCodes.ANSWER_RANGE_STEAP_VALUE.translation());
		steapRange.setWidth("100%");
		steapRange.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 4953347262492851075L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				steapRange.isValid();
			}
		});
		steapRange.addValidator(new ValidatorFloat());

		FormLayout layout = new FormLayout();
		layout.setSpacing(true);
		layout.setSizeFull();

		layout.addComponent(lowerValue);
		layout.addComponent(upperValue);
		layout.addComponent(steapRange);
		layout.setComponentAlignment(lowerValue, Alignment.MIDDLE_CENTER);
		layout.setComponentAlignment(upperValue, Alignment.MIDDLE_CENTER);
		layout.setComponentAlignment(steapRange, Alignment.MIDDLE_CENTER);
		return layout;
	}

	public Float getLowerValue() {
		return Float.parseFloat(lowerValue.getValue());
	}

	public Float getUpperValue() {
		return Float.parseFloat(upperValue.getValue());
	}

	public Float getSteapValue() {
		return Float.parseFloat(steapRange.getValue());
	}

}
