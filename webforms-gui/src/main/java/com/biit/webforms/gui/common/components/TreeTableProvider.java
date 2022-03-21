package com.biit.webforms.gui.common.components;

import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;

import java.util.Collection;

public interface TreeTableProvider<T> {
	public Collection<T> getAll() throws UnexpectedDatabaseException;
}