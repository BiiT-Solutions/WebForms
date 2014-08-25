package com.biit.webforms.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.biit.form.TreeObject;

@Entity
@Table(name = "label_element_references")
public class DinamicLabelReference extends DinamicLabelElement {

	@ManyToOne(fetch = FetchType.EAGER)
	private TreeObject reference;

	public TreeObject getReference() {
		return reference;
	}

	public void setReference(TreeObject reference) {
		this.reference = reference;
	}
}
