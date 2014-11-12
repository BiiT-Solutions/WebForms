package com.biit.webforms.gui.common.components;

import java.util.Collection;

import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;

public interface TreeTableProvider<T> {
	public Collection<T> getAll() throws UnexpectedDatabaseException;
}