package com.biit.webforms.security;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (Core)
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

/**
 * Liferay roles that can be translated to activities.
 *
 */
public enum WebformsRoles {
	
	/* empty role */
	NULL(""),

	READ("webforms2_read-only"),
	
	FORM_EDIT("webforms2_manage-forms"),

	BLOCK_EDIT("webforms2_manage-building_blocks"),
	
	FORM_ADMIN("webforms2_manage-forms_administration"),
	
	APPLICATION_ADMIN("webforms2_application-administration"),
	
	WEB_SERVICE_USER("webforms2_web-service-user");
	
	private String stringTag;
	
	WebformsRoles(String stringTag){
		this.stringTag = stringTag;
	}
	
	public String getStringTag(){
		return stringTag;
	}
	
	public static WebformsRoles parseTag(String stringTag){
		for(WebformsRoles role: values()){
			if(stringTag.toLowerCase().equals(role.getStringTag())){
				return role;
			}
		}
		return NULL;
	}
}
