package com.biit.webforms.persistence.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.biit.persistence.entity.StorableObject;

@Entity
@Table(name = "labels")
public class DinamicLabel extends StorableObject {

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private List<DinamicLabelElement> element;
 
}
