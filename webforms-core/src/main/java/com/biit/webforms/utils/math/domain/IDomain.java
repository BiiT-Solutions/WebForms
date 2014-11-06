package com.biit.webforms.utils.math.domain;

public interface IDomain {

	public boolean isComplete();

	public IDomain union(IDomain domain);

	public IDomain intersect(IDomain domain);

	public IDomain inverse();

	public boolean isEmpty();
}
