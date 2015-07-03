package com.biit.webforms.security;

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
	
	APPLICATION_ADMIN("webforms2_application-administration");
	
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
