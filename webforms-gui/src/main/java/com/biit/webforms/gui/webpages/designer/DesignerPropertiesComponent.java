package com.biit.webforms.gui.webpages.designer;

import com.biit.webforms.gui.common.components.PropertiesComponent;

public class DesignerPropertiesComponent extends PropertiesComponent{
	private static final long serialVersionUID = 1920009271620445225L;

	public DesignerPropertiesComponent(){
		super();
		registerPropertiesComponent(new PropertiesForm());
		registerPropertiesComponent(new PropertiesCategory());
		registerPropertiesComponent(new PropertiesGroup());
		registerPropertiesComponent(new PropertiesQuestion());
		registerPropertiesComponent(new PropertiesText());
		registerPropertiesComponent(new PropertiesSystemField());
		registerPropertiesComponent(new PropertiesAnswer());		
	}
}
