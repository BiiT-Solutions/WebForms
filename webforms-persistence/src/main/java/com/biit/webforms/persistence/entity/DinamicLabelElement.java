package com.biit.webforms.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.persistence.entity.StorableObject;

@Entity
@Table(name = "label_elements")
public abstract class DinamicLabelElement extends StorableObject{

}
