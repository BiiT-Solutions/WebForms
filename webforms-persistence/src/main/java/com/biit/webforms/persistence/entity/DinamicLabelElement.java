package com.biit.webforms.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import com.biit.persistence.entity.StorableObject;

@Entity
@Table(name = "label_elements")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class DinamicLabelElement extends StorableObject {

}
