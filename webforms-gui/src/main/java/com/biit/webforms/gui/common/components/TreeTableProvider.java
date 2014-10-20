package com.biit.webforms.gui.common.components;

import java.util.Collection;

public interface TreeTableProvider<T> {
	public Collection<T> getAll();
}