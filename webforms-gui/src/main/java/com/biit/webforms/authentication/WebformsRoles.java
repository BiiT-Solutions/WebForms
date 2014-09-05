package com.biit.webforms.authentication;

public enum WebformsRoles {
	
	/* empty role */
	NULL(""),

	READ("webforms2_read-only"),
	
	FORM_EDIT("webforms2_manage-building_blocks"),
	
	BLOCK_EDIT("webforms2_manage-building_blocks"),
	
	ADMIN("webforms2_manage-forms_administration"),
	
	;
	
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
