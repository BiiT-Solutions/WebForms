package com.biit.webforms.authentication;

import com.biit.liferay.security.IActivity;

/**
 * Activities are used for authorization system to define what a user can do and cannot do. The string for each activity
 * must be unique.
 */
public enum WebformsActivity implements IActivity {
	READ("Read"),
	// Edit or create BB
	BUILDING_BLOCK_EDITING("BuildingBlockEditing"),

	BUILDING_BLOCK_FLOW_EDITING("BuildingBlockFLowEditing"),

	BUILDING_BLOCK_ADD_FROM_FORM("BuildingBlockAdd"),
	// Edit or create F
	FORM_EDITING("FormEditing"),
	// Edit flow
	FORM_FLOW_EDITING("FormFlowEditing"),

	FORM_STATUS_UPGRADE("FormStatusUpgrade"),

	FORM_STATUS_DOWNGRADE("FormStatusDowngrade"),

	// FORM_CHANGE_GROUP("ChangeFormGroup"),

	FORM_NEW_VERSION("CreateNewFormVersion"),

	XFORMS_EXPORT("XFormsExport"),

	XFORMS_PUBLISH("XFormsPublish"),

	XML_VALIDATOR_AGAINST_FORM("XmlValidatorAgainstForm"),

	FORM_ANALYSIS("FormAnalysis"),

	ADMIN_RIGHTS("AdminRights"),

	EVICT_CACHE("EvictCache");

	private String tag;

	WebformsActivity(String tag) {
		this.tag = tag;
	}

	@Override
	public String getTag() {
		return tag;
	}

}
