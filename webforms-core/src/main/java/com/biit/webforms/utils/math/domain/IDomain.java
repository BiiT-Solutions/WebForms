package com.biit.webforms.utils.math.domain;

import java.util.HashMap;

import com.biit.webforms.persistence.entity.Question;

public interface IDomain {

	public boolean isComplete();

	public IDomain union(IDomain domain);

	public IDomain intersect(IDomain domain);

	public IDomain inverse();

	public boolean isEmpty();

	public HashMap<Question, String> generateRandomValue();
}
