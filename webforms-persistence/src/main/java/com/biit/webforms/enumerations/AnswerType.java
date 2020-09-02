package com.biit.webforms.enumerations;

public enum AnswerType {
	SINGLE_SELECTION_RADIO(null, true, true, false, true, false),

	SINGLE_SELECTION_LIST(null, true, false, null, true, false),

	SINGLE_SELECTION_SLIDER(null, true, false, false, true, false),

	MULTIPLE_SELECTION(null, true, true, null, true, false),

	// Uses answer format.
	INPUT(AnswerFormat.TEXT, false, false, null, true, true),

	TEXT_AREA(null, false, false, null, false, true);

	private final AnswerFormat defaultAnswerFormat;
	private final boolean childrenAllowed;
	private final boolean subChildrenAllowed;
	private final Boolean defaultHorizontal;
	private final Boolean defaultMandatory;
	private final boolean defaultValueEnabled;

	private AnswerType(AnswerFormat defaultAnswerType, boolean childrenAllowed, boolean subchildrenAllowed, Boolean defaultHorizontal, Boolean defaultMandatory,
			boolean defaultValueEnabled) {
		this.defaultAnswerFormat = defaultAnswerType;
		this.childrenAllowed = childrenAllowed;
		this.defaultHorizontal = defaultHorizontal;
		this.defaultMandatory = defaultMandatory;
		this.subChildrenAllowed = subchildrenAllowed;
		this.defaultValueEnabled = defaultValueEnabled;
	}

	public boolean isAnswerFormatEnabled() {
		return defaultAnswerFormat != null;
	}

	public boolean isHorizontalEnabled() {
		return defaultHorizontal != null;
	}

	public Boolean getDefaultHorizontal() {
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
		if (defaultMandatory != null) {
			return defaultMandatory;
		}
		return false;
	}

	public boolean isSubChildrenAllowed() {
		return subChildrenAllowed;
	}

	public boolean isDefaultValueEnabled() {
		return defaultValueEnabled;
	}
}
