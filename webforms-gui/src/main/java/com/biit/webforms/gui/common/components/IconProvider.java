package com.biit.webforms.gui.common.components;

import com.biit.webforms.gui.common.theme.IThemeIcon;

/**
 * Abstract Icon provider class. This class can be used to determine wich icon
 * has to use on several components.
 * 
 * @author joriz_000
 * 
 * @param <T>
 */
public abstract class IconProvider<T> {

	public abstract IThemeIcon getIcon(T object);
}
