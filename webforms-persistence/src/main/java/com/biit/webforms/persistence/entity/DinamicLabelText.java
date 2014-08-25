package com.biit.webforms.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "label_element_texts")
public class DinamicLabelText extends DinamicLabelElement{
	public static final int MAX_LABEL_LENGTH = 1000;
	
	@Column(length = MAX_LABEL_LENGTH)
	private String label;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
}
