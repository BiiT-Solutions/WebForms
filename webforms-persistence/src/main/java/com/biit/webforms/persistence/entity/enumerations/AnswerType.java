package com.biit.webforms.persistence.entity.enumerations;

public enum AnswerType {
	SINGLE_SELECTION_RADIO(null, true, false, true, true),

	SINGLE_SELECTION_LIST(null, true, false, true, false),

	MULTIPLE_SELECTION(null, true, null, null, true),

	// Uses answer format.
	INPUT(AnswerFormat.TEXT, false, null, true, false),

	TEXT_AREA(null, false, null, false, false);

	private AnswerFormat defaultAnswerFormat;
	private boolean childrenAllowed;
	private Boolean defaultHorizontal;
	private Boolean defaultMandatory;
	private boolean nestedAnswerAllowed;

	AnswerType(AnswerFormat defaultAnswerType, boolean childrenAllowed, Boolean defaultHorizontal, Boolean defaultMandatory, boolean nestedAnswerAllowed) {
		this.defaultAnswerFormat = defaultAnswerType;
		this.childrenAllowed = childrenAllowed;
		this.defaultHorizontal = defaultHorizontal;
		this.defaultMandatory = defaultMandatory;
		this.nestedAnswerAllowed = nestedAnswerAllowed;
	}

	public boolean isAnswerFormatEnabled() {
		return defaultAnswerFormat != null;
	}

	public boolean isHorizontalEnabled() {
		return defaultHorizontal!=null;
	}

	public boolean getDefaultHorizontal() {
		if (defaultHorizontal != null) {
			return defaultHorizontal;
		}
		return false;
	}

	public AnswerFormat getDefaultAnswerFormat() {
		return defaultAnswerFormat;
	}

	public boolean isChildrenAllowed() {
		return childrenAllowed;
	}

	public boolean isMandatoryEnabled() {
		return defaultMandatory != null;
	}

	public boolean getDefaultMandatory() {
		if (defaultMandatory!=null) {
			return defaultMandatory;
		}
		return false;
	}

	public boolean isInputField() {
		return this.equals(AnswerType.INPUT);
	}

	public boolean isNestedAnswerAllowed() {
		return nestedAnswerAllowed;
	}
}
