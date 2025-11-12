package com.biit.webforms.gui.common.components;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (GUI)
 * %%
 * Copyright (C) 2014 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;

public class StatusLabel extends Label{
	private static final long serialVersionUID = 7295517549680582545L;

	public enum StatusType{
		NORMAL,
		ERROR,
		WARNING,
		OK,
	}

	public StatusLabel() {
		super();
		setContentMode(ContentMode.HTML);
	}
	
	public StatusLabel(String content) {
		super();
		setContentMode(ContentMode.HTML);
		setNormalText(content);
	}
	
	public void setValue(StatusType status,String content){
		setValue(generateContent(status,content));
	}
	
	public void setNormalText(String content){
		setValue(StatusType.NORMAL,content);
	}
	
	public void setErrorText(String content){
		setValue(StatusType.ERROR,content);
	}
	
	public void setWarningText(String content){
		setValue(StatusType.WARNING,content);
	}
	
	public void setOkText(String content){
		setValue(StatusType.OK,content);
	}
	
	public String generateContent(StatusType status,String content){
		String color = null;
		switch(status){
		case NORMAL:
			color = "rgb(0,0,0)";
			break;
		case ERROR:
			color = "rgb(194, 64, 64)";
			break;
		case WARNING:
			color = "rgb(192, 199, 0)";
			break;
		case OK:
			color = "rgb(74, 158, 85)";
			break;
		}
		return "<p style=\"margin:0px;font-size:14px;color:"+color+";\">"+content+"</p>";
	}
}
